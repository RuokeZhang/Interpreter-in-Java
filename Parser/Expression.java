package Parser;
import java.io.IOException;

public class Expression {
    private Term term;
    private Expression expr;
    private Core operator = null;
    private VariableTable vTable;

    public Expression(VariableTable vTable) {
        this.vTable = vTable;
        this.term = new Term(vTable);  // Assuming Term requires VariableTable for checking undeclared vars
    }


    void parse(Scanner s) throws IOException {
        term.parse(s);
        if (ParserUtils.lookAheadToken == null) {
            if (s.currentToken() == Core.ADD || s.currentToken() == Core.SUBTRACT) {
                operator = s.currentToken();
                s.nextToken(); // Consume the operator token

                // Check if the next token is the start of a factor
                if (s.currentToken() != Core.ID && s.currentToken() != Core.CONST && s.currentToken() != Core.LPAREN) {
                    System.out.println("ERROR: Expected start of a factor after '" + (operator == Core.ADD ? "+" : "-") + "' but found " + s.currentToken());
                    System.exit(0);
                }
                // Parse the next expression only if an operator was identified
                expr = new Expression(vTable);;
                expr.parse(s);
            } else {
                // If it's neither + nor -, then it's a possible lookAhead for another construct
                ParserUtils.lookAheadToken = s.currentToken();
               // System.out.println("When parsing expression, store" + ParserUtils.lookAheadToken);
                s.nextToken();
            }
        } else if (ParserUtils.lookAheadToken != null
                && (ParserUtils.lookAheadToken == Core.ADD || ParserUtils.lookAheadToken == Core.SUBTRACT)) {
            operator = ParserUtils.lookAheadToken;
           // System.out.println("When parsing expression, "+operator+"consumed");
            ParserUtils.lookAheadToken = null; // Resetting the lookahead after using it

            // Check if the next token is the start of a factor
            if (s.currentToken() != Core.ID && s.currentToken() != Core.CONST && s.currentToken() != Core.LPAREN) {
                System.out.println("ERROR: Expected start of a factor after '" + (operator == Core.ADD ? "+" : "-") + "' but found " + s.currentToken());
                System.exit(0);
            }

            // Parse the next expression only if an operator was identified
            expr = new Expression(vTable);
            expr.parse(s);
        }
    }


    void print() {
        term.print();
        if (operator != null) { // Make sure there's an operator to print
            if (operator == Core.ADD) {
                System.out.print(" + ");
            } else if (operator == Core.SUBTRACT) { // Make sure this is the correct enum
                System.out.print(" - ");
            }
            expr.print(); // Ensure that expr is not null
        }
    }
    int execute(){
        int value = term.execute();
        if (operator != null) {
            if (operator == Core.ADD) {
                value += expr.execute();
            } else if (operator == Core.SUBTRACT) {
                value -= expr.execute();
            }
        }
        return value;
    }
    public boolean isIdentifier(){
        return operator==null&&term.isIdentifier();
    }
    public String getName(){
        return term.getName();
    }
}
