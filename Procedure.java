
import java.io.IOException;

public class Procedure {
    private String procedureName;
    private DeclSeq declSeq = null;
    private StmtSeq stmtSeq;
    private VariableTable vTable;
    private Scanner dataScanner;  // Add dataScanner member

    public Procedure(VariableTable vTable, Scanner dataScanner) {
        this.vTable = vTable;
        this.dataScanner = dataScanner;  // Initialize dataScanner
    }

    /**
     * Parses the procedure from the scanner input.
     *
     * @param scanner The scanner object.
     * @throws IOException If there's an issue reading the input.
     */
    public void parse(Scanner scanner) throws IOException {


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

        vTable.enterScope(); // Entering a new scope for this procedure

        stmtSeq = new StmtSeq(vTable); // Pass the variable table to StmtSeq
        stmtSeq.parse(scanner);

        // Finish up by expecting the END token for the procedure
        ParserUtils.handleExpectedToken(scanner, Core.END);

        // No tokens should follow after the 'end' token
        if (scanner.currentToken() != Core.EOS) {
            System.out.println("ERROR: There should be no token after the 'end' token");
            System.exit(0);
        }

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

    public void execute() {
        stmtSeq.execute(dataScanner);
    }
}
