package Parser;

import java.io.IOException;

public class InKeyword {
    private String id; // Variable to store the identifier
    private VariableTable vTable;


    // The constructor now takes a Scanner for the input data
    public InKeyword(VariableTable vTable) {
        this.vTable = vTable;
    }

    void parse(Scanner s) throws IOException {

        // Expecting the token 'IN'
        if (s.currentToken() != Core.IN) {
            System.out.println("ERROR: IN expected, found " + s.currentToken());
            System.exit(0);
        }
        s.nextToken(); // Consume 'IN' token

        // Expecting left parenthesis '('
        if (s.currentToken() != Core.LPAREN) {
            System.out.println("ERROR: left parenthesis expected after 'in', found " + s.currentToken());
            System.exit(0);
        }
        s.nextToken(); // Consume '(' token

        // Expecting an identifier
        if (s.currentToken() != Core.ID) {
            System.out.println("ERROR: id expected inside parentheses, found " + s.currentToken());
            System.exit(0);
        }
        id = s.getId(); // Store the identifier

        s.nextToken(); // Consume identifier token

        // Expecting right parenthesis ')'
        if (s.currentToken() != Core.RPAREN) {
            System.out.println("ERROR: right parenthesis expected after id, found " + s.currentToken());
            System.exit(0);
        }
        s.nextToken(); // Consume ')' token

        // Expecting a semicolon ';'
        if (s.currentToken() != Core.SEMICOLON) {
            System.out.println("ERROR: semicolon expected after closing parenthesis, found " + s.currentToken());
            System.exit(0);
        }
        s.nextToken(); // Consume ';' token
    }

    void print() {
        System.out.print("    ");
        System.out.println("in (" + id + ") ;"); // Print the 'in' statement with identifier
    }
    void execute(Scanner dataScanner){
        int value = dataScanner.getConst();
        dataScanner.nextToken();
        vTable.store(id, value);
    }
}
