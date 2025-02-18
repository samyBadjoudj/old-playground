/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   BookCategory.h
 * Author: sam
 *
 * Created on April 20, 2020, 2:00 PM
 */

#ifndef BOOKCATEGORY_H
#define BOOKCATEGORY_H
#include <string>
#include "BookCategory.h"
#include <iostream>

using namespace std;

class BookCategory {
public:
    BookCategory();
    BookCategory(string name);


    string getName() const {
        return name;
    }

    virtual ~BookCategory();
   

    BookCategory(const BookCategory& other)
     {
        this->name = other.name;
    }

    bool operator<(const BookCategory& right) const {
        return right.getName() >  this->getName(); // Reuse greater than operator
    }
        friend std::ostream& operator<<(std::ostream& os, const BookCategory& obj) {
            os << obj.name << " ADRESS OF CAT : " <<  &obj;
        return os;
    }

    

private:
    string name;
   

    

};

#endif /* BOOKCATEGORY_H */

