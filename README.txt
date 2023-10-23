Author: Ruoke Zhang

Files:
.
├── Assign.class
├── Assign.java
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
│   ├── 01.code
│   ├── 01.data
│   ├── 02.code
│   ├── 02.data
│   ├── 03.code
│   ├── 03.data
│   ├── 04.code
│   └── 04.data
├── Expression.class
├── Expression.java
├── Factor.class
├── Factor.java
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
├── ParserUtils.class
├── ParserUtils.java # This file contains utility methods that the Parser utilizes. These methods assist in parsing and interpreting the code, making the parsing process more organized and modular.
├── Procedure.class
├── Procedure.java
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
├── VariableTable.java #This serves as the memory class for the interpreter. It handles semantic checks and manages the values of variables. The VariableTable ensures that the variables are accessed and modified correctly and provides mechanisms for error checks related to variable operations.
├── docs
│   └── 3341 Project 3.pdf
└── scripts
    └── tester.sh

5 directories, 175 files



Features:
1. Early Variable Storage: During the parsing phase, upon encountering a variable declaration, I immediately store it in the Variable Table.
I adopted this approach primarily because, in the language, variables have default values.
Thus, even before an assignment statement, the variable won't be undefined, ensuring the program's robustness.

2. Unified Function Calls: In the Variable Table, I've overridden the store() and getIntValue() functions.
This means that regardless of the type of value you wish to store or retrieve, the same function name can be used.
The purpose of this is to simplify the interface, making function calls more intuitive, and reducing errors from forgetting function names.


Interpreter Design:
Variable Management with Value Tracking:
The interpreter uses a structure called VariableTable to manage variables.
Within this table, each variable is associated with a Value object.
This Value object is a wrapper class that contains the variable's value and type.

Scope Management and Variable Hiding:
Variables are categorized into global and local.
For global variables, they are stored in a map. For local variables, they are placed in the topmost map of a stack.
Every time a function is called, the enterScope method is used, which adds a new map to the top of the local stack.


Testing&bugs:
Bugs: First and foremost, there are no known bugs in the interpreter.

Testing Approach: After completing all the execute methods, I ran the correct test files.
I discovered several errors. To pinpoint the sources of these errors, I set up numerous outputs within both the parse and execute methods.
