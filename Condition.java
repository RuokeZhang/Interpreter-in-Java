import java.io.IOException;

public class Condition {
    private Comparison cmpr;
    private Condition leftCond;
    private Condition rightCond;
    private Core operator;
    private VariableTable vTable;

    public Condition(VariableTable vTable) {
        this.vTable = vTable;
    }

    void parse(Scanner s) throws IOException {
        if (s.currentToken() == Core.NOT) {
            operator = Core.NOT;
            s.nextToken();
            leftCond = new Condition(vTable);
            leftCond.parse(s);
        } else if (ParserUtils.startsWithFactor(s.currentToken())) {
            cmpr = new Comparison(vTable);
            cmpr.parse(s);

            if (ParserUtils.lookAheadToken == null) {
                if (s.currentToken() == Core.AND || s.currentToken() == Core.OR) {
                    operator = s.currentToken();
                    s.nextToken(); // Consume the AND/OR token

                    rightCond = new Condition(vTable);
                    rightCond.parse(s);
                } else {
                    // For any other token, set it as lookAheadToken
                    ParserUtils.lookAheadToken = s.currentToken();

                    s.nextToken();
                }
            } else if (ParserUtils.lookAheadToken == Core.AND || ParserUtils.lookAheadToken == Core.OR) {
                operator = ParserUtils.lookAheadToken;
                ParserUtils.lookAheadToken = null; // Resetting the lookahead token after using it

                rightCond = new Condition(vTable);
                rightCond.parse(s);
            }
            // If ParserUtils.lookAheadToken is another signal like THEN, just leave it and
            // wait for the respective parser to handle it
        } else {
            System.out.println("ERROR: expected a condition, token found is " + s.currentToken());
            System.exit(0);
        }

    }

    void print() {

        if (operator == Core.NOT) {
            System.out.print("not ");
            leftCond.print();
        } else {
            if (cmpr != null) {
                cmpr.print();

                if (operator != null) {
                    switch (operator) {
                        case AND:
                            System.out.print(" and ");
                            break;
                        case OR:
                            System.out.print(" or ");
                            break;
                        default:
                            break; // We don't expect any other value for operator at this point
                    }

                    if (rightCond != null) {
                        rightCond.print(); // If there's a right side condition, print it
                    }
                }
            }
        }
    }
    boolean execute(){
        if (operator == Core.NOT) {
            return !leftCond.execute();
        } else {
            if (cmpr != null) {
                boolean leftValue = cmpr.execute();

                if (operator != null) {
                    switch (operator) {
                        case AND:
                            return leftValue && rightCond.execute();
                        case OR:
                            return leftValue || rightCond.execute();
                        default:
                            break; // We don't expect any other value for operator at this point
                    }
                }
                return leftValue;
            }
        }
        return false;
    }
}
