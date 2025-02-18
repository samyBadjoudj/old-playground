
##Introduction

Gradient descent is an optimization algorithm.
The aim of gradient descent is to converge to a local minimum/maximum of a function.
We call it a first order derivative iterative algorithm.
It is labeled First order derivative, since we use  (latex FOD) during the iterations.
Different variant have been developed.

There are plethora of applications, easy to imagine, as soon as you want to converge a local minimum/maximum (Machine Learning, mecanics, biology...).
Since the algorithm is widely used, you can find it easily. It has been implemented in many libraries.

Here we will go through the basics of the theory, then we will apply it on a earth sun system.


## Conditions to apply it

For all x.. f(x..) has to be differentiable.

For some of function it is not apply (schema and functions)

- 1/x -> goes to infinity when x = 0  
- 1/x³ vertical tangent line x = 0
- x/|x| is not defined for x = 0
- x =0 has a cusp

## Description of the algorithm

Function to work on :

- Let's define f(xn) define on domain R^n and ranged in R --> R^n \mapsto R
- Xn-> data points
- f(xn) -> images Xn
- First partial derivative (latex)  \frac{\partial}{\partial x_{i}} f(x_{i})
- Learning rate alpha
- Starting point 
- Current point
- Direction 
- Number of iterations
- Treshold to stop  
- Condition to stop

##Latex images with all the parameters

Written algorithm 


## Application to the algorithm on orbital speed 

\item Aphelion $152 097 597 km$
\item Perihelion $147 098 450 km$
\item Semi-major axis $149 598 023 km$
\item Eccentricity 0.0167086
Avg orbital speed 29.7827 km

Explanation of the orbital system
Explanation of Ellipse (use the schema on Geo algebra)
Explanation of Orbital speed
Explanation of function OSpeed and formula
Describing the data used (mass of the sun, earth, gravity constant)

Presenting the code briefly
 
### Describing curve fitting on a simple function polynomial regression
Description of the aim to fit
Description of the process with the degree of liberty
Description of the formula with parameters
System of epochs and iterations













