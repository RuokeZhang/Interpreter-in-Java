import java.io.IOException;

public class Stmt {

    // Member variables to represent the different types of statements
    private Core stmtType;
    private IfKeyword ifKeyword;
    private Loop loop;
    private OutKeyword outKeyword;
    private InKeyword inKeyword;
    private Decl decl;
    private Assign assign;

    // Variable table to manage the variable scope and declaration
    private VariableTable vTable;

    /**
     * Constructor initializes the Stmt with a given variable table.
     *
     * @param vTable The variable table.
     */
    public Stmt(VariableTable vTable) {
        this.vTable = vTable;
    }

    /**
     * Parses the statement based on the current token from the scanner.
     *
     * @param s The scanner instance to read tokens.
     */
    void parse(Scanner s, VariableTable vTable) throws IOException {
        stmtType = s.currentToken();
        // Parsing if-statement
        if (stmtType == Core.IF) {
            ifKeyword = new IfKeyword(vTable);
            ifKeyword.parse(s);

            // Parsing while-loop
        } else if (stmtType == Core.WHILE) {
            loop = new Loop(vTable);
            loop.parse(s);

            // Parsing output statement
        } else if (stmtType == Core.OUT) {
            outKeyword = new OutKeyword(vTable);
            outKeyword.parse(s);

            // Parsing input statement
        } else if (stmtType == Core.IN) {
            inKeyword = new InKeyword(vTable);
            inKeyword.parse(s);

            // Parsing variable declarations (either array or integer type)
        } else if (stmtType == Core.ARRAY || stmtType == Core.INTEGER) {
            decl = new Decl();
            decl.parse(s);

            // Check for duplicate variable declaration
            String varName = decl.getVariableName();
            if (vTable.variableExists(varName)) {
                System.out.println("ERROR: Variable " + varName + " is already declared in this scope!");
                System.exit(1);
            } else {
                vTable.addLocalVariable(varName, decl.getVariableType());
            }

            // Parsing assignment statements
        } else if (stmtType == Core.ID) {
            assign = new Assign(vTable); // Construct Assign with VariableTable
            assign.parse(s);


            // Error handling for unexpected statement types
        } else {
            System.out.println("ERROR: Statement Type Expected, but found " + stmtType);
            System.exit(0);
        }
    }

    /**
     * Prints the parsed statement based on its type with proper indentation.
     *
     *
     */
    void print() {
        if (ifKeyword != null) {
            ifKeyword.print();
        } else if (loop != null) {
            loop.print();
        } else if (outKeyword != null) {
            outKeyword.print();
        } else if (inKeyword != null) {
            inKeyword.print();
        } else if (decl != null) {
            decl.print();
        } else if (assign != null) {
            assign.print();
        }
    }

    void execute(Scanner dataScanner) {
        if (ifKeyword != null) {
            ifKeyword.execute(dataScanner);
        } else if (loop != null) {
            loop.execute(dataScanner);
        } else if (outKeyword != null) {
            outKeyword.execute();
        } else if (inKeyword != null) {
            inKeyword.execute(dataScanner);
        }  else if (assign != null) {
            assign.execute();
        }
    }
}
