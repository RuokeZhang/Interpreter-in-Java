
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class StmtSeq {
    private List<Stmt> stmtSeq = new ArrayList<>();
    private VariableTable vTable;

    public StmtSeq(VariableTable vTable) {
        this.vTable = vTable;
    }

    void parse(Scanner s) throws IOException {
        // If the next token is still the start of another statement, we parse it
        // recursively.
        while (ParserUtils.isStartOfStmt(s.currentToken())) {
            Stmt stmt = new Stmt(vTable); // Assuming Stmt constructor takes a VariableTable
            stmt.parse(s, vTable);
            stmtSeq.add(stmt);
        }
        if (stmtSeq.isEmpty()) {
            System.out.println("ERROR: No statements found!");
            System.exit(1);
        }
    }

    public void print() {
        for (Stmt stmt : stmtSeq) {
            stmt.print();
        }
    }

    void execute(Scanner dataScanner) {
        for (Stmt stmt : stmtSeq) {
            stmt.execute(dataScanner);
        }
    }
}
