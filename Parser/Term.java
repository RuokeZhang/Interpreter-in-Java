package Parser;
import java.io.IOException;

public class Term {
    private Factor factor;
    private Term term;
    private Core operator = null;
    private VariableTable vTable;

    public Term(VariableTable vTable) {
        this.vTable = vTable;
        this.factor = new Factor(vTable); // Assuming Term requires VariableTable for checking undeclared vars
    }

    void parse(Scanner s) throws IOException {
        factor = new Factor(vTable);
        factor.parse(s);
        // Determine the operator either from the currentToken or lookAheadToken
        if (ParserUtils.lookAheadToken == null) {
            if (s.currentToken() == Core.MULTIPLY || s.currentToken() == Core.DIVIDE) {
                operator = s.currentToken();
                s.nextToken();

                // Parse the next term only if an operator was identified
                term = new Term(vTable);
                term.parse(s);
            } else {
                // If it's + or -
                ParserUtils.lookAheadToken = s.currentToken();
                s.nextToken();
            }
        } else if (ParserUtils.lookAheadToken != null
                && (ParserUtils.lookAheadToken == Core.MULTIPLY || ParserUtils.lookAheadToken == Core.DIVIDE)) {
            operator = ParserUtils.lookAheadToken;
            ParserUtils.lookAheadToken = null;

            // Parse the next term only if an operator was identified
            term = new Term(vTable);
            term.parse(s);
        }
        // If ParserUtils.lookAheadToken is + or -, just leave it, wait for the
        // expression parser to handle it
    }

    void print() {
        factor.print();
        if (operator != null) {
            if (operator == Core.MULTIPLY) {
                System.out.print(" * ");
            } else if (operator == Core.DIVIDE) {
                System.out.print(" / ");
            }
            if (term != null) {
                term.print();
            }
        }
    }
    int execute(){
        int value = factor.execute();
        if (operator != null) {
            if (operator == Core.MULTIPLY) {
                value *= term.execute();
            } else if (operator == Core.DIVIDE) {
                value /= term.execute();
            }
        }
        return value;
    }
}

