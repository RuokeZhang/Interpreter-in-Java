
import java.io.IOException;

public class Factor {
    private String id;
    private int num;
    private Expression expr;
    private boolean hasNum = false;
    private VariableTable vTable;

    public Factor(VariableTable vTable) {
        this.vTable = vTable;
    }

    void parse(Scanner s) throws IOException {
        Core token = s.currentToken();
        if (token == Core.ID) {
            id = s.getId();
            s.nextToken();
            // Check for '[' followed by an expression and then ']'
            if (s.currentToken() == Core.LBRACE) {
                s.nextToken();
                expr = new Expression(vTable);
                expr.parse(s);
                ParserUtils.handleExpectedToken(s, Core.RBRACE);
            } else {

                ParserUtils.lookAheadToken = s.currentToken();
                s.nextToken();
            }
        } else if (token == Core.CONST) {
            num = s.getConst();
            hasNum = true;
            s.nextToken();
        } else if (token == Core.LPAREN) {
            s.nextToken();
            expr = new Expression(vTable);
            expr.parse(s);
            ParserUtils.handleExpectedToken(s, Core.RPAREN);
        } else {
            System.out.println("ERROR: Invalid token encountered for Factor parsing!" + token);
            System.exit(0);
        }
    }

    public int execute() {
        int value;
        if (id != null) {
            vTable.checkVariableDeclared(id);
            value = vTable.getIntValue(id);
            if (expr != null) {
                vTable.checkVariableType(id, Core.ARRAY);
                value = vTable.getIntValue(id, expr.execute());
            }else{
                if(vTable.getVariableType(id)==Core.ARRAY){
                    value=vTable.getIntValue(id,0);
                }
            }
        } else if (hasNum) {
            value=num;
        } else {
            value=expr.execute();
        }
        return value;
    }

    public void print() {
        if (id != null) {
            System.out.print(id);
            // Check if there's an associated expression, meaning the id was followed by
            // square brackets.
            if (expr != null) {
                System.out.print("[");
                expr.print();
                System.out.print("]");
            }
        } else if (hasNum) { // Assuming default initialization to 0, adjust if necessary.
            System.out.print(num);
        } else {
            System.out.print("(");
            expr.print();
            System.out.print(")");
        }
    }
    public boolean isIdentifier(){
        return id!=null&&expr==null;
    }
    public String getName(){
        return id;
    }

}
