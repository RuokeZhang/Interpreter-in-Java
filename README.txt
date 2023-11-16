Author: Ruoke Zhang

Files:
.
├── 3341 Project 5.pdf
├── ArrAndRc.class
├── ArrAndRc.java
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
│   ├── 0.code
│   ├── 0.data
│   ├── 0.expected
│   ├── 0.student
│   ├── 1.code
│   ├── 1.data
│   ├── 1.expected
│   ├── 1.student
│   ├── 2.code
│   ├── 2.data
│   ├── 2.expected
│   ├── 2.student
│   ├── 3.code
│   ├── 3.data
│   ├── 3.expected
│   ├── 3.student
│   ├── 4.code
│   ├── 4.data
│   ├── 4.expected
│   ├── 4.student
│   ├── 5.code
│   ├── 5.data
│   ├── 5.expected
│   ├── 5.student
│   ├── 6.code
│   ├── 6.data
│   ├── 6.expected
│   ├── 6.student
│   ├── 7.code
│   ├── 7.data
│   ├── 7.expected
│   ├── 7.student
│   ├── 8.code
│   ├── 8.data
│   ├── 8.expected
│   ├── 8.student
│   ├── 9.code
│   ├── 9.data
│   ├── 9.expected
│   └── 9.student
├── Decl$1.class
├── Decl.class
├── Decl.java
├── DeclSeq.class
├── DeclSeq.java
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
├── Value.class
├── Value.java
├── VariableTable$Value.class
├── VariableTable.class
├── VariableTable.java
└── tester.sh

Function.java is the most important file in this project. It contains the implementation of the function calls.
Call.java handles the function calls.
Parameters.java stores the parameters of a function call.

Features:
1. Every Variable has a rc. If two arrays are aliased, they share the same rc.

Testing&bugs:
Bugs: First and foremost, there are no known bugs in the interpreter.

Testing Approach:
At first, I didn't like two aliased variables share the rc, this will cause some problems.
Even though I increment/decrement both of the variable when doing array assignment, it still can't solve the problem.
So I decided to change the implementation of the rc. I moved the Value class out of the VariableTable class, and when doing assignment, I assign one variable's value to another's. This will solve the problem.
