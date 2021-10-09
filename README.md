# Bagging Grocery
![](images/Capture.JPG)

## Problem Description and Requirements
Grocery Bagging is a real-world application of <b>Constraint Satisfaction Problem (CSP)</b>. The goal is to determine a way to bag all groceries that satisfies multiple constraints: (1) what items can be bagged with what(i.e., bread can only be bagged with cookies; meat cannot be pagged with fruits) (2) bag capacity (3) the number of bags available. These constraints will be defined in an input file in this order:<br/> 
<br/><i>
an integer which is the number of available bags<br/>
an integer which is the maximum bag capacity <br/>
all the items, sizes, and item constraints, 1 item per line <i/><br/>

3                   //number of bags available
7                   //bag capacity is 7
bread  3 + rolls    //the size of item0(bread) is 3, it can only be pagged with item1(rolls)  
rolls  2 + bread    //the size of item1(rolls) is 2, it can only be pagged with item0(bread)  
squash 3 - meat     //2nd item size
meat   5            //3rd item size
lima_beans 1 - meat //5th item size

## Compiling and Running
To compile the class file, run:<br />
$ make<br />
After executing the make, run:<br />
$ /bagit.sh <input.txt><br />


Replace <input.txt> with the constrains information. Below is a sample input file. 

such as  I used Backtracking search algorithm (Depth-First-Search) along with arc-consistency, Most Restrictive Value (MRV) and Least Constraining Value (LCV) heuristics for prioritizing and pruning to increase tree traversal performance in large problem sets. This project was developed in Java and executed by Makefile and Shell script.

The file will define the problem description, and will contain (in this order):

an integer which is the number of available bags
an integer which is the maximum bag capacity
all the items, sizes, and item constraints, 1 item per line


Following is an example of what a file might contain:

3                   //number of bags available
7                   //maximum bag size is 7
bread  3 + rolls    //1st item size
rolls  2 + bread    //1st item size
squash 3 - meat     //2nd item size
meat   5            //3rd item size
lima_beans 1 - meat //5th item size
