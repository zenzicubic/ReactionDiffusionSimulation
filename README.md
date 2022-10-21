# ReactionDiffusionSimulation
 This is a simple Java app that simulates the Gray-Scott reaction diffusion equations.

# About
 Reaction-diffusion equations are simple mathematical models of how chemicals react. Many
 exist, but all of them have a few things in common:
 - They're parabolic partial differential equations.
 - They display complex and lifelike behavior.
 - They form wiggly patterns when plotted.

 This program plots the Gray-Scott equations, a pair of two partial differential equations:

 <img src="https://latex.codecogs.com/svg.latex?\partial_t%20A%20=%20r_A%20\nabla^2%20A%20-%20AB^2%20+%20f(1-A)" />

 <img src="https://latex.codecogs.com/svg.latex?\partial_t%20B%20=%20r_B%20\nabla^2%20B%20+%20AB^2%20-%20(f+k)B" />

 These equations express the reactions

 <img src="https://latex.codecogs.com/svg.latex?A+2B\to3B" />

 <img src="https://latex.codecogs.com/svg.latex?B\to%20C" />

 where *f* is the rate that chemical A is added to the mix, *k* is the reaction rate of chemical B becoming chemical C, <i>r<sub>A</sub></i> is the rate of chemical A's diffusion, and <i>r<sub>B</sub></i> is the rate of chemical B's diffusion.

 This Java program solves these equations using Euler integration. A JAR file is supplied in the "bin" folder. You will need to install Java on your computer for this to work. Some examples are located in the "examples" folder.