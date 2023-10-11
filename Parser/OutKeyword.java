package Parser;

import java.io.IOException;

public class OutKeyword {
    private Expression expr;
    private VariableTable vTable;

    public OutKeyword(VariableTable vTable) {
        this.vTable = vTable;
    }

    void parse(Scanner s) throws IOException {
        ParserUtils.handleExpectedToken(s, Core.OUT);
        ParserUtils.handleExpectedToken(s, Core.LPAREN);
        expr = new Expression(vTable);
        expr.parse(s);
        ParserUtils.handleExpectedToken(s, Core.RPAREN);
        ParserUtils.handleExpectedToken(s, Core.SEMICOLON);
    }

    void print() {
        System.out.print("    out (");
        expr.print(); // Assuming the Expression class has a print method
        System.out.println(") ;");
    }

    void execute() {
        if (expr.isIdentifier()) {
            String name = expr.getName();
            if (vTable.getVariableType(name) == Core.ARRAY) {
                System.out.println(vTable.getIntValue(name, 0));
            } else {
                System.out.println(expr.execute());
            }
        } else {
            System.out.println(expr.execute());
        }
    }


}
