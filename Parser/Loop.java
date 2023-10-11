package Parser;
import java.io.IOException;

// This class represents the Loop construct in our grammar.
public class Loop {

    // These are the main components of a loop: the condition to check, and the sequence of statements to execute.
    private Condition condition;
    private StmtSeq stmtSeq;
    private VariableTable vTable;  // Table to store variable information

    // Constructor: Initializes a new loop with a given variable table
    public Loop(VariableTable vTable) {
        this.vTable = vTable;
        this.condition = new Condition(vTable);
        this.stmtSeq = new StmtSeq(vTable);
    }

    // The parse method is used to parse the loop from the input
    void parse(Scanner s) throws IOException {
        ParserUtils.handleExpectedToken(s, Core.WHILE);  // Expecting 'while' keyword at the start of a loop
        condition.parse(s);  // Parse the loop condition

        ParserUtils.handleExpectedToken(s, Core.DO);  // Expecting 'do' keyword after condition
        // Check if the current token marks the start of a statement. If not, raise an error.
        if (!ParserUtils.isStartOfStmt(s.currentToken())) {
            System.out.println("ERROR: expected a statement, but found " + s.currentToken());
            System.exit(0);
        }
        stmtSeq.parse(s);  // Parse the sequence of statements

        ParserUtils.handleExpectedToken(s, Core.END);  // Expecting 'end' keyword at the end of a loop
    }


    void print() {
        System.out.print("    while ");
        condition.print();  // Print the loop condition
        System.out.println(" do");
        stmtSeq.print();
        System.out.println("    end");  // Print the ending keyword for the loop
    }

    void execute(String data){
        while(condition.execute()){
            stmtSeq.execute(data);
        }
    }
}
