Author: Ruoke Zhang

Files:
├── Assign.class
├── Assign.java
├── Call.class
├── Call.java
├── Comparison.class
├── Comparison.java
├── Condition$1.class
├── Condition.class
├── Condition.java
├── Core.class
├── Core.java
├── Correct
│   ├── 1.code
│   ├── 1.data
│   ├── 1.expected
│   ├── 1.student
│   ├── 10.code
│   ├── 10.data
│   ├── 10.expected
│   ├── 10.student
│   ├── 11.code
│   ├── 11.data
│   ├── 11.expected
│   ├── 11.student
│   ├── 12.code
│   ├── 12.data
│   ├── 12.expected
│   ├── 12.student
│   ├── 13.code
│   ├── 13.data
│   ├── 13.expected
│   ├── 13.student
│   ├── 14.code
│   ├── 14.data
│   ├── 14.expected
│   ├── 14.student
│   ├── 15.code
│   ├── 15.data
│   ├── 15.expected
│   ├── 15.student
│   ├── 16.code
│   ├── 16.data
│   ├── 16.expected
│   ├── 16.student
│   ├── 17.code
│   ├── 17.data
│   ├── 17.expected
│   ├── 17.student
│   ├── 18.code
│   ├── 18.data
│   ├── 18.expected
│   ├── 18.student
│   ├── 19.code
│   ├── 19.data
│   ├── 19.expected
│   ├── 19.student
│   ├── 2.code
│   ├── 2.data
│   ├── 2.expected
│   ├── 2.student
│   ├── 20.code
│   ├── 20.data
│   ├── 20.expected
│   ├── 20.student
│   ├── 21.code
│   ├── 21.data
│   ├── 21.expected
│   ├── 21.student
│   ├── 22.code
│   ├── 22.data
│   ├── 22.expected
│   ├── 22.student
│   ├── 23.code
│   ├── 23.data
│   ├── 23.expected
│   ├── 23.student
│   ├── 24.code
│   ├── 24.data
│   ├── 24.expected
│   ├── 24.student
│   ├── 25.code
│   ├── 25.data
│   ├── 25.expected
│   ├── 25.student
│   ├── 26.code
│   ├── 26.data
│   ├── 26.expected
│   ├── 26.student
│   ├── 27.code
│   ├── 27.data
│   ├── 27.expected
│   ├── 27.student
│   ├── 28.code
│   ├── 28.data
│   ├── 28.expected
│   ├── 28.student
│   ├── 29.code
│   ├── 29.data
│   ├── 29.expected
│   ├── 29.student
│   ├── 3.code
│   ├── 3.data
│   ├── 3.expected
│   ├── 3.student
│   ├── 30.code
│   ├── 30.data
│   ├── 30.expected
│   ├── 30.student
│   ├── 31.code
│   ├── 31.data
│   ├── 31.expected
│   ├── 31.student
│   ├── 4.code
│   ├── 4.data
│   ├── 4.expected
│   ├── 4.student
│   ├── 5.code
│   ├── 5.data
│   ├── 5.expected
│   ├── 5.student
│   ├── 6.code
│   ├── 6.data
│   ├── 6.expected
│   ├── 6.student
│   ├── 7.code
│   ├── 7.data
│   ├── 7.expected
│   ├── 7.student
│   ├── 8.code
│   ├── 8.data
│   ├── 8.expected
│   ├── 8.student
│   ├── 9.code
│   ├── 9.data
│   ├── 9.expected
│   └── 9.student
├── Decl$1.class
├── Decl.class
├── Decl.java
├── DeclSeq.class
├── DeclSeq.java
├── Error
│   ├── 00.code
│   ├── 00.data
│   ├── 01.code
│   ├── 01.data
│   ├── 02.code
│   ├── 02.data
│   ├── 03.code
│   ├── 03.data
│   ├── 04.code
│   ├── 04.data
│   ├── 05.code
│   ├── 05.data
│   ├── 06.code
│   ├── 06.data
│   ├── 07.code
│   └── 07.data
├── Expression.class
├── Expression.java
├── Factor.class
├── Factor.java
├── Function.class
├── Function.java
├── IfKeyword.class
├── IfKeyword.java
├── InKeyword.class
├── InKeyword.java
├── Loop.class
├── Loop.java
├── Main.class
├── Main.java
├── OutKeyword.class
├── OutKeyword.java
├── Parameters.class
├── Parameters.java
├── ParserUtils.class
├── ParserUtils.java
├── Procedure.class
├── Procedure.java
├── Project3.iml
├── README.txt
├── Scanner.class
├── Scanner.java
├── Stmt.class
├── Stmt.java
├── StmtSeq.class
├── StmtSeq.java
├── Term.class
├── Term.java
├── VariableTable$Value.class
├── VariableTable.class
├── VariableTable.java
└── tester.sh

Function.java is the most important file in this project. It contains the implementation of the function calls.
Call.java handles the function calls.
Parameters.java stores the parameters of a function call.

Features:
1. I like the setParameter() function in the Function class. It is a very elegant way to pass the parameters.

2. Unified Function Calls: In the Variable Table, I've overridden the store() and getIntValue() functions.
This means that regardless of the type of value you wish to store or retrieve, the same function name can be used.
The purpose of this is to simplify the interface, making function calls more intuitive, and reducing errors from forgetting function names.

Interpreter Design:
The call stack is implemented using a stack of stacks of maps. The bottom stack is the global scope. The top stack is the local scope.
Every time a function is called, a new map is added to the top of the stack. When the function returns, the top map is removed from the stack.

Testing&bugs:
Bugs: First and foremost, there are no known bugs in the interpreter.

Testing Approach: Because I do the semantic checking during parsing in my previous project, I met some problem when I tried to parse the function calls.
So I decided to do the semantic at last.
I first modified the parser to parse the function calls, and test it using the printer.
After making sure the parser works, I started to implement the execution part.
It took me some time to figure out how to pass the values to the formal parameters.
And I figured it out by considering the nature of passing by sharing in Java.
When I implemented the store() function to assign an array with the value of another array, I found that the array is not copied, but shared.
So I could simply use store() again to assign the value of the formal parameter to the actual parameter.
I used the same test cases as the previous project, and I also added some new test cases to test the function calls.