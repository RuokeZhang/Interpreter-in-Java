package Parser;
import java.io.IOException;

// This class represents the Assignment construct in the grammar.
public class Assign {

    // Variables for storing parsed data
    private String id;
    private Expression indexExpr;
    private Expression valueExpr;
    private String arrayId;
    private boolean isNewInteger = false;
    private Expression newIntegerExpr;
    private VariableTable vTable;

    // Constructor: Initializes an assignment with the given variable table
    public Assign(VariableTable vTable) {
        this.vTable = vTable;
    }

    void parse(Scanner s) throws IOException {

        // Parse and validate the ID
        ParserUtils.handleExpectedToken(s, Core.ID);
        id = s.getId();
        vTable.checkVariableDeclared(id);

        // Handle array assignments
        if (s.currentToken() == Core.LBRACE) {
            vTable.checkVariableType(id, Core.ARRAY);
            s.nextToken();
            indexExpr = new Expression(vTable);
            indexExpr.parse(s);

            ParserUtils.handleExpectedToken(s, Core.RBRACE);
            ParserUtils.handleExpectedToken(s, Core.ASSIGN);
            valueExpr = new Expression(vTable);
            valueExpr.parse(s);
            ParserUtils.handleExpectedToken(s, Core.SEMICOLON);
        }
        // Handle variable assignments
        else if (s.currentToken() == Core.ASSIGN) {
            s.nextToken();

            // Handle new integer array assignment
            if (s.currentToken() == Core.NEW) {
                vTable.checkVariableType(id, Core.ARRAY);
                s.nextToken();

                ParserUtils.handleExpectedToken(s, Core.INTEGER);
                ParserUtils.handleExpectedToken(s, Core.LBRACE);

                isNewInteger = true;
                newIntegerExpr = new Expression(vTable);
                newIntegerExpr.parse(s);
                ParserUtils.handleExpectedToken(s, Core.RBRACE);
            }
            // Handle array to array assignment
            else if (s.currentToken() == Core.ARRAY) {
                vTable.checkVariableType(id, Core.ARRAY);
                s.nextToken();

                ParserUtils.handleExpectedToken(s, Core.ID);
                arrayId = s.getId();
                vTable.checkVariableType(arrayId, Core.ARRAY);
            }
            // Handle simple variable assignment
            else {
                valueExpr = new Expression(vTable);
                valueExpr.parse(s);
            }

            ParserUtils.handleExpectedToken(s, Core.SEMICOLON);
        } else {
            System.out.println("ERROR: Expecting ':=' token or '[' token, found " + s.currentToken());
            System.exit(0);
        }
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
        else if (arrayId != null) {
            System.out.println(" := array " + arrayId + ";");
        }
        else {
            System.out.print(" := ");
            valueExpr.print();
            System.out.println(";");
        }
    }

    public void execute(){
        if (indexExpr != null) {
            vTable.store(id, indexExpr.execute(), valueExpr.execute());
            //System.out.println("Assigning "+id+"["+indexExpr.execute(vTable)+"] to "+valueExpr.execute());
        }
        // Print new integer array assignment
        else if (isNewInteger) {
            vTable.newArray(id, newIntegerExpr.execute());
        }
        // Print array to array assignment
        else if (arrayId != null) {

            vTable.store(id, vTable.getArrValue(arrayId));

        }
        else {
            if (vTable.getVariableType(id) == Core.ARRAY) {
                vTable.store(id, 0,valueExpr.execute());
            }else{
                vTable.store(id, valueExpr.execute());
            }
        }
    }
}
