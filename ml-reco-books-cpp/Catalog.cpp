/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   Catalog.cpp
 * Author: sam
 * 
 * Created on April 21, 2020, 6:47 PM
 */

#include "Catalog.h"
#include "BookCategory.h"
#include <iostream>

Catalog::Catalog() : allCategoriesByName(), allCategories() {
}

Catalog::Catalog(const Catalog& orig) {
}

Catalog::~Catalog() {
}

map<string, std::shared_ptr<BookCategory> > const & Catalog::getAllCategoriesByName() const {
    return allCategoriesByName;
}

set<std::shared_ptr<BookCategory>> const & Catalog::getAllCategories() const {
    return allCategories;
}

std::shared_ptr<BookCategory> Catalog::addCategory(shared_ptr<BookCategory> bookCategory) {
    allCategoriesByName.insert(std::make_pair(bookCategory.get()->getName(), bookCategory));
    allCategories.insert(bookCategory);
    return bookCategory;
}

std::shared_ptr<Book> Catalog::addBook(const std::shared_ptr<Book> & book) {
    allBooks.insert(book);
    allBooksByName.insert(std::make_pair(book->getTitle(), book));
    for (const auto &cat : book->getCategoryRates()) {
        this->addCategory(cat.first);
    }
    return book;
}

set<std::shared_ptr<Book>> const &  Catalog::getAllBooks() const{
    return allBooks;
}

void Catalog::populateCatalog(const nlohmann::json& parsedCatalog) {

    for (const auto &jsonBook : parsedCatalog["catalog"]["books"]) {
        shared_ptr<Book> book = std::make_shared<Book>(Book(jsonBook["title"]));
        for (const auto& catJson : jsonBook["categories"]) {
            shared_ptr<BookCategory> cat;
            if (allCategoriesByName.find(catJson["name"]) != allCategoriesByName.end()) {
                cat = allCategoriesByName.at(catJson["name"]);
            } else {
                cat = std::make_shared<BookCategory>(BookCategory(catJson["name"]));
            }
            book->addCategories(cat, catJson["rate"]);
            addBook(book);
        }
    }
}





