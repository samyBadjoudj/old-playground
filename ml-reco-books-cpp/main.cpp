/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   main.cpp
 * Author: sam
 *
 * Created on April 20, 2020, 1:44 PM
 */

#include <iostream>
#include <fstream>
#include <set>
#include <vector>
#include <memory>
#include "Book.h"
#include "BookCategory.h"
#include "Catalog.h"
#include "MlRecoUtils.h"
#include "json.hpp"


using json = nlohmann::json;

extern "C" {

    const char * getAllRecoData(char * catalogName) {
        Catalog myCatalog = Catalog();
        std::ifstream ifCatalog(catalogName);
        json jsonCatalog;
        ifCatalog >> jsonCatalog;
        myCatalog.populateCatalog(jsonCatalog);
        std::set<std::shared_ptr < Book>> favoriteBooks;
        for (const auto& favBook : jsonCatalog["favoriteBooks"]) {
            std::shared_ptr < Book> b = myCatalog.getBookByName(favBook);
            favoriteBooks.insert(b);
        }
        Book centroid = mlrecomutilsmath::computeCentroid(favoriteBooks, myCatalog.getAllCategories());
        auto distances = mlrecomutilsmath::computeAllDistances(centroid,myCatalog.getAllBooks(),myCatalog.getAllCategories());
        auto filteredFeaturedBook = mlrecomutilsmath::getBookWithFilteredFeatureByHighVariance(myCatalog);
        json data_computed = mlrecomutilsjson::createJsonResult(centroid, distances, filteredFeaturedBook);
        data_computed["favoritesBooks"] = jsonCatalog["favoriteBooks"];
        return data_computed.dump().c_str();
    }
}

int main(int argc, char** argv) {

    return 0;
}

