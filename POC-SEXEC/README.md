POC-SEXEC
=========

Naive approach to try to understand, challenges to scale a Symbolic Execution.


Symbolic execution can help to test a software, analyse it.

Symbolic execution can be very difficult on complexe softwares, when we want to identify all possible paths.
It fits well on functional languages, but can be difficult to manage on OO programming languages.

Here is a small example of a simple method working on primitives types (compute), that tries to print the variable and
conditions to reach some part of the if.

This example is very limited, with a small depth in paths. It's just here to illustrate that if we want for example,

write some tests to cover all parts of the code we can work on the conditions. 

Identifying on wich assignement or change the conditions values depends on. Here x and y.

This codes needs a proper parser(to identify how values changes, and on wich other values they depend) ,
track in depth, variables inside each path.


It helped me to understand what are the constraints to work on this field, I hope it will help you :).


http://en.wikipedia.org/wiki/Symbolic_execution



```
METHOD : compute 

symbX int
symbY int
--------------------------------------
   
Var [x] depends on : 
x <= symbX+2;
   
Var [symbY] depends on : 

To execute the then part : x>symbY
   
{
    return x * symbY + 4;
}
 no else part  
   
--------------------------------------
--------------------------------------
   
Var [y] depends on : 
y <= symbY+1;
   
Var [x] depends on : 
x <= symbX+2;
x <= x+4;

To execute the then part : y==x
   
{
    return x * y;
}
   
else part
   
{
    return 2 * x + y;
}
   
--------------------------------------
```

Samy Badjoudj
