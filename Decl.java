
import java.io.IOException;

class Decl {
    private Core declType;
    private String id;

    private VariableTable vTable;

    public Decl(VariableTable vTable) {
        this.vTable = vTable;
    }
    void parse(Scanner s) throws IOException {
        declType = s.currentToken();
        switch (declType) {
            case INTEGER:
                parseIntegerDecl(s);
                break;
            case ARRAY:
                parseArrayDecl(s);
                break;
            default:
                System.out.println("ERROR: expected an array or an integer token");
                System.exit(0);
        }
    }

    private void parseIntegerDecl(Scanner s) throws IOException {
        s.nextToken();
        if (s.currentToken() != Core.ID) {
            System.out.println("ERROR: invalid id for decl");
            System.exit(0);
        }
        id = s.getId();

        s.nextToken();
        if (s.currentToken() != Core.SEMICOLON) {
            System.out.println("ERROR: Expect ';' here.");
            System.exit(1);
        }
        s.nextToken();
    }

    private void parseArrayDecl(Scanner s) throws IOException {
        s.nextToken();
        if (s.currentToken() != Core.ID) {
            System.out.println("ERROR: invalid id for decl, found "+s.currentToken());
            System.exit(1);
        }
        id = s.getId();

        s.nextToken();
        if (s.currentToken() != Core.SEMICOLON) {
            System.out.println("ERROR: Expect ';' here, found "+s.currentToken());
            System.exit(1);
        }

        s.nextToken();
    }

    public void print() {
        if (declType == Core.INTEGER) {
            System.out.println("integer " + id + ";");
        } else if (declType == Core.ARRAY) {
            System.out.println("array " + id + ";");
        }
    }

    public String getVariableName() {
        return id;
    }

    public Core getVariableType() {
        return declType;
    }

    void execute() {
        if (declType == Core.INTEGER) {
            vTable.addGlobalVariable(id, declType);
        } else if (declType == Core.ARRAY) {
            vTable.addGlobalVariable(id, declType);
        }
    }




}
