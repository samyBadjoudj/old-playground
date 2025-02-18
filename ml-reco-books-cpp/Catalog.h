/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   Catalog.h
 * Author: sam
 *
 * Created on April 21, 2020, 6:47 PM
 */

#ifndef CATALOG_H
#define CATALOG_H
#include <string>
#include <map>
#include <iostream>
#include <set>
#include <memory>
#include "BookCategory.h"
#include "Book.h"
#include "json.hpp"



class Catalog {
public:
    Catalog();
    Catalog(const Catalog& orig);
    virtual ~Catalog();
    
    map<string, std::shared_ptr<BookCategory>> const & getAllCategoriesByName() const;
    
    std::shared_ptr<Book> const & getBookByName(const string& name){
        return allBooksByName[name];
    }
    
    set<std::shared_ptr<BookCategory>> const & getAllCategories() const;
    set<std::shared_ptr<Book>> const & getAllBooks() const;

    
    std::shared_ptr<BookCategory> addCategory(shared_ptr<BookCategory> bookCategory);
    
    std::shared_ptr<Book> addBook(const std::shared_ptr<Book> & book);
    
    void populateCatalog(const nlohmann::json& parsedCatalog );

    friend std::ostream& operator<<(std::ostream& os, const Catalog& obj) {
        for(const auto &book : obj.allBooks){
            os << *book << endl;
        }
        return os;
    }

private:
    map<string, std::shared_ptr<BookCategory>> allCategoriesByName;
    map<string, std::shared_ptr<Book>> allBooksByName;
    set<std::shared_ptr<BookCategory>> allCategories;
    map<std::shared_ptr<BookCategory>,set<std::shared_ptr<Book>>> booksByCategory;
    set<std::shared_ptr<Book>> allBooks;
};

#endif /* CATALOG_H */

