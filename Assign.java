
import java.io.IOException;

// This class represents the Assignment construct in the grammar.
public class Assign {
    // type is
    // 0 if id := <expr> assignment
    // 1 if id[<expr>] := <expr> assignment
    // 2 if "new" assignment
    // 3 if "array" assignment

    // Variables for storing parsed data
    private String id;
    private Expression indexExpr;
    private Expression valueExpr;
    private String secId;
    private boolean isNewInteger = false;
    private Expression newIntegerExpr;
    private VariableTable vTable;

    private int type;

    // Constructor: Initializes an assignment with the given variable table
    public Assign(VariableTable vTable) {
        this.vTable = vTable;
    }

    void parse(Scanner s) throws IOException {
        type = 0;
        // Parse and validate the ID
        ParserUtils.handleExpectedToken(s, Core.ID);
        id = s.getId();

        // Handle array assignments
        if (s.currentToken() == Core.LBRACE) {
            type = 1;
            s.nextToken();
            indexExpr = new Expression(vTable);
            indexExpr.parse(s);

            ParserUtils.handleExpectedToken(s, Core.RBRACE);
            ParserUtils.handleExpectedToken(s, Core.ASSIGN);
            valueExpr = new Expression(vTable);
            valueExpr.parse(s);

        } else {
            ParserUtils.handleExpectedToken(s, Core.ASSIGN);
            // Handle new integer array assignment
            if (s.currentToken() == Core.NEW) {
                s.nextToken();
                type = 2;
                ParserUtils.handleExpectedToken(s, Core.INTEGER);
                ParserUtils.handleExpectedToken(s, Core.LBRACE);

                isNewInteger = true;
                newIntegerExpr = new Expression(vTable);
                newIntegerExpr.parse(s);
                ParserUtils.handleExpectedToken(s, Core.RBRACE);
            }
            // Handle array to array assignment
            else if (s.currentToken() == Core.ARRAY) {
                type = 3;
                s.nextToken();
                ParserUtils.handleExpectedToken(s, Core.ID);
                secId = s.getId();
            }
            // Handle simple variable assignment
            else {
                valueExpr = new Expression(vTable);
                valueExpr.parse(s);
            }
        }

        ParserUtils.handleExpectedToken(s, Core.SEMICOLON);
    }

    // Print the parsed assignment in a formatted manner
    public void print() {
        System.out.print(id);

        // Print array assignment
        if (indexExpr != null) {
            System.out.print("[");
            indexExpr.print();
            System.out.print("] := ");
            valueExpr.print();
            System.out.println(";");
        }
        // Print new integer array assignment
        else if (isNewInteger) {
            System.out.print(" := new integer[");
            newIntegerExpr.print();
            System.out.println("];");
        }
        // Print array to array assignment
        else if (secId != null) {
            System.out.println(" := array " + secId + ";");
        } else {
            System.out.print(" := ");
            valueExpr.print();
            System.out.println(";");
        }
    }

    public void execute() {
        vTable.checkVariableDeclared(id);
        if (type == 0) {
            if (vTable.getVariableType(id) == Core.ARRAY) {
                vTable.store(id, 0, valueExpr.execute());
            } else {
                vTable.store(id, valueExpr.execute());
            }
        } else if (type == 1) {
            vTable.checkVariableType(id, Core.ARRAY);
            vTable.store(id, indexExpr.execute(), valueExpr.execute());
        } else if (type == 2) {
            vTable.checkVariableType(id, Core.ARRAY);
            vTable.newArray(id, newIntegerExpr.execute());
            //set reference counting
            vTable.incrementRefCount(id);
            vTable.printMessage();
        } else if (type == 3) {
            vTable.checkVariableType(id, Core.ARRAY);
            vTable.checkVariableType(secId, Core.ARRAY);
            vTable.checkVariableDeclared(secId);
            //before we store the array, we need to decrement the reference count of the array that is being overwritten
            if(vTable.getRefCount(id) > 0){
                vTable.decrementRefCount(id);
            }
            vTable.store(id, vTable.getValue(secId));
            //increment the reference count of the array object
            if(vTable.getRefCount(secId) > 0){
                vTable.incrementRefCount(secId);
            }
        }

    }
}
