
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class DeclSeq {
    private List<Decl> declSeq = new ArrayList<>();
    private List<Function> functionSeq = new ArrayList<>();
    private VariableTable vTable;

    public DeclSeq(VariableTable vTable) {
        this.vTable = vTable;
    }

    /**
     * Parses the declaration sequence from the scanner input.
     *
     * @param s The scanner object.
     * @throws IOException If there's an issue reading the input.
     */
    final void parse(Scanner s) throws IOException {

        while (ParserUtils.isStartOfDecl(s.currentToken()) || ParserUtils.isStartOfFunction(s.currentToken())) {
            if (ParserUtils.isStartOfDecl(s.currentToken())) {
                Decl decl = new Decl(vTable); // Construct Decl with VariableTable
                decl.parse(s);
                String variableName = decl.getVariableName();
                declSeq.add(decl);
            } else if (ParserUtils.isStartOfFunction(s.currentToken())) {
                Function function = new Function(vTable, s);
                function.parse(s);
                String functionName = function.getFunctionName();
                functionSeq.add(function);
            }
        }
    }

    public void print() {
        for (Decl decl : declSeq) {
            decl.print();
        }
        for (Function function : functionSeq) {
            function.print();
        }
    }

    void execute() {
        for (Decl decl : declSeq) {
            decl.execute();
        }
        for (Function function : functionSeq) {
            vTable.addFunction(function);
        }
    }

}
