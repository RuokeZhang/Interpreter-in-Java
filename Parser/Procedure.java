package Parser;
import java.io.IOException;



/**
 * Represents a procedure in the language. A procedure contains declarations and statements.
 */
public class Procedure {
    private String procedureName;       // Name of the procedure
    private DeclSeq declSeq = null;     // Sequence of declarations within the procedure
    private StmtSeq stmtSeq;            // Sequence of statements within the procedure
    private VariableTable vTable;       // Table to keep track of variables within the procedure

    /**
     * Constructor initializes a Procedure with a VariableTable to manage variables in its scope.
     *
     * @param vTable The variable table.
     */
    public Procedure(VariableTable vTable) {
        this.vTable = vTable;
    }

    /**
     * Parses the procedure from the scanner input.
     *
     * @param scanner The scanner object.
     * @throws IOException If there's an issue reading the input.
     */
    public void parse(Scanner scanner) throws IOException {
        vTable.initialization();
        vTable.enterScope(); // Entering a new scope for this procedure

        // Expecting a PROCEDURE token
        ParserUtils.handleExpectedToken(scanner, Core.PROCEDURE);

        // Validating the procedure name
        if (scanner.currentToken() != Core.ID) {
            System.out.println("ERROR: There should be no token after the 'end' token");
            System.exit(0);
        }
        procedureName = scanner.getId();
        scanner.nextToken();

        ParserUtils.handleExpectedToken(scanner, Core.IS);

        // Parsing declarations if present
        if (scanner.currentToken() == Core.ARRAY || scanner.currentToken() == Core.INTEGER) {
            declSeq = new DeclSeq(vTable); // Pass the variable table to DeclSeq
            declSeq.parse(scanner);
        }

        // Expecting the BEGIN token to start parsing statements
        ParserUtils.handleExpectedToken(scanner, Core.BEGIN);

        stmtSeq = new StmtSeq(vTable); // Pass the variable table to StmtSeq
        System.out.println("start parsing stmtSeq  ");
        stmtSeq.parse(scanner);

        // Finish up by expecting the END token for the procedure
        ParserUtils.handleExpectedToken(scanner, Core.END);

        // No tokens should follow after the 'end' token
        if (scanner.currentToken() != Core.EOS) {
            System.out.println("ERROR: There should be no token after the 'end' token");
            System.exit(0);
        }

        vTable.leaveScope(); // Exiting the scope for this procedure
    }

    /**
     * Prints out the procedure, its declarations, and its statements.
     */
    public void print() {
        System.out.println("procedure " + procedureName + " is");

        // Print the declarations if present
        if (declSeq != null) {
            declSeq.print(); // Assuming DeclSeq has a print method
        }
        System.out.println("begin");
        // Print the statements within the procedure
        stmtSeq.print(); // Assuming StmtSeq has a print method

        System.out.println("end");
    }

    public void execute(String data){
        stmtSeq.execute(data);
    }
}
