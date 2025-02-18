/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   Book.h
 * Author: sam
 *
 * Created on April 20, 2020, 1:45 PM
 */

#ifndef BOOK_H
#define BOOK_H
#include <string>
#include "BookCategory.h"
#include <set>
#include <map>
#include <memory>
  class Book {
public:
    Book();
    Book(std::string title) :
    title(title){
    }
    Book(std::string title, std::map<shared_ptr<BookCategory>,double> categories) :
    title(title), categoryRates(categories) {
    }
    void addCategories(const std::shared_ptr<BookCategory> & bookCategory, const double rate);
    Book(const Book& orig);
    virtual ~Book();
    std::string const & getTitle() const;
    std::map<shared_ptr<BookCategory>,double> const &  getCategoryRates() const;
    
    friend std::ostream& operator<<(std::ostream& os, const Book& obj) {
        os << obj.title << endl;
        
        for(const auto& cat : obj.categoryRates){
            os << *cat.first << " "  << cat.second <<endl;
        }
        return os;
    }

    bool operator<(const Book& right) const {
        return right.getTitle() > this->getTitle(); // Reuse greater than operator
    }
    
    

private:
  
    std::map<shared_ptr<BookCategory>,double> categoryRates;
    std::string title;
    

};


        
#endif /* BOOK_H */

