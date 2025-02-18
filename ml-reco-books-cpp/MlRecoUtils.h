/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   MlRecoUtils.h
 * Author: sam
 *
 * Created on April 24, 2020, 7:43 PM
 */

#ifndef MLRECOUTILS_H
#define MLRECOUTILS_H
#include <set>
#include <math.h>
#include <map>
#include <bits/stdc++.h> 
#include <iostream>
#include "Book.h"
#include "BookCategory.h"
#include "json.hpp"
#include "Catalog.h"
using json = nlohmann::json;
namespace mlrecomutilsmath {
    
    typedef std::function<bool(std::pair<std::shared_ptr<BookCategory>, double>, std::pair<std::shared_ptr<BookCategory>, double>) > Comparator;
    static const Comparator compFunctor =
            [](std::pair<std::shared_ptr<BookCategory>, double> elem1, std::pair<std::shared_ptr<BookCategory>, double> elem2) {
                return elem1.second > elem2.second;
            };

    inline Book computeCentroid(const std::set<std::shared_ptr<Book>>&cluster, const std::set<std::shared_ptr<BookCategory>> &features) {
        Book centroid;
        for (auto& bc : features) {
            const std::shared_ptr<BookCategory> cat = bc;
            double centroidValue = 0;
            for (auto& book : cluster) {
                std::map<std::shared_ptr<BookCategory>, double> cats = book->getCategoryRates();
                double currentRate = cats[cat];
                centroidValue += currentRate;
            }
            centroidValue /= cluster.size();

            centroid.addCategories(cat, centroidValue);
        }
        return centroid;
    }

    double computeDistance(Book& centroidBook, const std::shared_ptr<Book> & book, const std::set<std::shared_ptr<BookCategory>> &features) {
        double distance = 0.0;
        auto catRateCentroid = centroidBook.getCategoryRates();
        auto catRateOtherBook = book->getCategoryRates();
        for (auto& bc : features) {
            double centroidComponent = catRateCentroid.find(bc) != catRateCentroid.end() ? catRateCentroid[bc] : 0;
            double bookComponent = catRateOtherBook.find(bc) != catRateOtherBook.end() ? catRateOtherBook[bc] : 0;
            distance += pow((centroidComponent - bookComponent), 2);
        }
        return sqrt(distance);
    }
    
    std::map<std::shared_ptr<Book>,double>  computeAllDistances(Book& centroidBook, const std::set<std::shared_ptr<Book>> & books, const std::set<std::shared_ptr<BookCategory>> &features){
       std::map<std::shared_ptr<Book>,double> distances;
        for(auto const& currentBook: books){
            distances.insert(std::make_pair(currentBook,mlrecomutilsmath::computeDistance(centroidBook,currentBook,features)));
        }
        return distances;
    }

    double computeMeanByFeature(const std::set<std::shared_ptr<Book>> &books, const std::shared_ptr<BookCategory> &component) {
        double mean = 0.0;
        for (const auto& book : books) {
            auto catRateOtherBook = book->getCategoryRates();
            double bookFeatureValue = catRateOtherBook.find(component) != catRateOtherBook.end() ? catRateOtherBook[component] : 0;
            mean += bookFeatureValue;
        }
        return mean / books.size();
    }

    std::map<std::shared_ptr<BookCategory>, double> computeVarianceByFeature(const std::set<std::shared_ptr<Book>> &books, const std::set<std::shared_ptr<BookCategory>> &features) {
        std::map<std::shared_ptr<BookCategory>, double> varianceByFeatures;
        for (auto& bc : features) {
            double variance = 0.0;
            double meanByFeature = mlrecomutilsmath::computeMeanByFeature(books, bc);
            for (const auto& book : books) {
                auto catRateOtherBook = book->getCategoryRates();
                double featureValue = catRateOtherBook.find(bc) != catRateOtherBook.end() ? catRateOtherBook[bc] : 0;
                variance += (featureValue - meanByFeature) * (featureValue - meanByFeature);
            }
            varianceByFeatures.insert(std::make_pair(bc, variance));
        }
        return varianceByFeatures;
    }

    std::map<std::shared_ptr<BookCategory>, double> keepTopNVarianceFeature(const std::set<std::shared_ptr<Book>> &books, const std::set<std::shared_ptr<BookCategory>> &features, int n) {

        std::map<std::shared_ptr<BookCategory>, double> varianceByFeaturesFitlered;
        std::vector<std::pair < std::shared_ptr<BookCategory>, double>> filterVariances;

        std::map<std::shared_ptr<BookCategory>, double> varianceByFeatures = mlrecomutilsmath::computeVarianceByFeature(books, features);

        std::copy_if(varianceByFeatures.begin(), varianceByFeatures.end(), std::inserter(filterVariances, filterVariances.end()), [](std::pair<std::shared_ptr<BookCategory>, double> b) {
            return true;
        });
        std::sort(filterVariances.begin(), filterVariances.end(), compFunctor);

        for (int i = 0; (i < filterVariances.size() && i < n); i++) {
            varianceByFeaturesFitlered.insert(filterVariances[i]);
        }
        return varianceByFeaturesFitlered;
    }

    std::set<Book> getBookWithFilteredFeature(const std::set<std::shared_ptr<Book>> &books, const std::set<std::shared_ptr<BookCategory>> &features) {
        std::set<Book> filteredFeatureBook;
        for (const auto &book : books) {
            Book tempBook(book->getTitle());
            for (const auto& component : features) {
                std::map<std::shared_ptr<BookCategory>, double> rates = book->getCategoryRates();
                double featureValue = rates.find(component) != rates.end() ? rates[component] : 0;
                tempBook.addCategories(component, featureValue);
            }
            filteredFeatureBook.insert(tempBook);
        }
        return filteredFeatureBook;
    }
    std::set<Book> getBookWithFilteredFeatureByHighVariance(const Catalog &catalog ) {
        std::map<std::shared_ptr<BookCategory>, double> topVariance = mlrecomutilsmath::keepTopNVarianceFeature(catalog.getAllBooks(),catalog.getAllCategories(),3);
         std::set<std::shared_ptr<BookCategory>> categories;
        for (auto const& imap : topVariance){
            categories.insert(imap.first);
        }
        return getBookWithFilteredFeature(catalog.getAllBooks(),categories);
    }




}

namespace mlrecomutilsjson {

    std::shared_ptr<Book> const createBookFromJson(json& jsonBook, const map<string, std::shared_ptr<BookCategory>> &allCategories) {
        shared_ptr<Book> book = std::make_shared<Book>(Book(jsonBook["title"]));
        for (const auto &catJson : jsonBook["categories"]) {
            shared_ptr<BookCategory> cat;
            if (allCategories.find(catJson["name"]) != allCategories.end()) {
                cat = allCategories.at(catJson["name"]);
            } else {
                cat = std::make_shared<BookCategory>(BookCategory(catJson["name"]));
            }
            book->addCategories(cat, catJson["rate"]);
        }
        return book;
    }
    json const  createJsonBookFromBook( const Book &book) {
          json jsonBook;
          jsonBook["title"] = book.getTitle();
          std::vector<json> jsonCategories;
          for(const auto& cat : book.getCategoryRates()){
              json jsoncat;
              jsoncat["name"]=cat.first.get()->getName();
              jsoncat["rate"]=cat.second;
              jsonCategories.push_back(jsoncat);
          }
          jsonBook["categories"]=jsonCategories;
        return jsonBook;
    }
 
    std::vector<json> const createJsonDistanceFromBookDistances(const std::map<std::shared_ptr<Book>,double> &distances) {
          std::vector<json>  distancesVec;
          for ( const auto &distance : distances ) {
              json jsonDistance;
          
          jsonDistance["book"] = mlrecomutilsjson::createJsonBookFromBook(*distance.first);
          jsonDistance["value"] = distance.second;
          distancesVec.push_back(jsonDistance);
          }
          
        return distancesVec;
    }
    
     json const  createJsonBooksFromBooks(const std::set<Book> books){
        std::vector<json> booksVec;
        
        for( auto& book : books){
            booksVec.push_back(mlrecomutilsjson::createJsonBookFromBook(book));
        }
        return booksVec;
    }    
    json  createJsonResult(const Book& centroid, const  std::map<std::shared_ptr<Book>,double> &distances, const std::set<Book> & featureFilteredBooks){
        json fullcomputedInfo;
        fullcomputedInfo["centroid"] = mlrecomutilsjson::createJsonBookFromBook(centroid);
        fullcomputedInfo["distances"] = mlrecomutilsjson::createJsonDistanceFromBookDistances(distances);
        fullcomputedInfo["booksHighVariances"] = mlrecomutilsjson::createJsonBooksFromBooks(featureFilteredBooks);
        return fullcomputedInfo;
    }
    
    
   
}

#endif /* MLRECOUTILS_H */

