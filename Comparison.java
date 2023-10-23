import java.io.IOException;

public class Comparison {
    private Expression leftExpr;
    private Expression rightExpr;
    private VariableTable vTable;
    private Core operator;

    public Comparison(VariableTable vTable) {
        this.vTable = vTable;
    }

    void parse(Scanner s) throws IOException {
        leftExpr = new Expression(vTable);
        leftExpr.parse(s);

        // Determine the operator either from the currentToken or lookAheadToken
        if (ParserUtils.lookAheadToken == null) {
            if (s.currentToken() == Core.EQUAL || s.currentToken() == Core.LESS) {
                operator = s.currentToken();
                s.nextToken(); // Consume the operator token

                // Parse the right expression only if an operator was identified
                rightExpr = new Expression(vTable);
                rightExpr.parse(s);
            } else {
                System.out.println("ERROR: expect < or = here, but found " + s.currentToken());
                System.exit(0);
            }
        } else if (ParserUtils.lookAheadToken == Core.EQUAL || ParserUtils.lookAheadToken == Core.LESS) {
            operator = ParserUtils.lookAheadToken;
            ParserUtils.lookAheadToken = null;

            // Parse the right expression
            rightExpr = new Expression(vTable);
            rightExpr.parse(s);
        } else {
            System.out.println("ERROR: expect < or = here, but found " + ParserUtils.lookAheadToken);
            System.exit(0);
        }
    }

    void print() {
        leftExpr.print();
        if (operator == Core.EQUAL) {
            System.out.print("=");
        } else if (operator == Core.LESS) {
            System.out.print("<");
        }
        rightExpr.print();
    }

    boolean execute() {
        int leftValue = leftExpr.execute();
        int rightValue = rightExpr.execute();
        if (operator == Core.EQUAL) {
            return leftValue == rightValue;
        } else if (operator == Core.LESS) {
            return leftValue < rightValue;
        }
        return false;
    }
}
