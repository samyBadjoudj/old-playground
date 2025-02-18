/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   Book.cpp
 * Author: sam
 * 
 * Created on April 20, 2020, 1:45 PM
 */

#include "Book.h"

std::map<std::shared_ptr<BookCategory>,double> const & Book::getCategoryRates() const {
    return categoryRates;
}

std::string const & Book::getTitle() const {
    return title;
}

Book::Book() {
    
}

Book::Book(const Book& orig) {
    title = orig.title;
    categoryRates = orig.categoryRates;
}

Book::~Book() {
}

void Book::addCategories(const std::shared_ptr<BookCategory>& bookCategory, const double rate) {
    this->categoryRates.insert(std::make_pair(bookCategory,rate));
}

