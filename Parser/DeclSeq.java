package Parser;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class DeclSeq {
    private List<Decl> declSeq = new ArrayList<>();
    private VariableTable vTable;

    public DeclSeq(VariableTable vTable) {
        this.vTable = vTable;
    }

    void parse(Scanner s) throws IOException {
        // If the next token is still the start of another decl, we parse it
        // recursively.
        while (isStartOfDecl(s.currentToken())) {
            Decl decl = new Decl(vTable); // Construct Decl with VariableTable
            decl.parse(s);

            // Assuming Decl has a method getVariableName() to get the name of the declared
            // variable
            String variableName = decl.getVariableName();
            if (vTable.variableExists(variableName)) {
                System.out.println("ERROR: duplicate decl for variable: " + variableName);
                System.exit(1);
            } else {
                declSeq.add(decl);
                vTable.addGlobalVariable(variableName, decl.getVariableType());
            }
        }
    }

    private boolean isStartOfDecl(Core token) {
        return token == Core.INTEGER || token == Core.ARRAY;
    }

    public void print() {
        for (Decl d : declSeq) {
            d.print();
        }
    }


}
