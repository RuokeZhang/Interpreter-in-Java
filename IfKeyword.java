
import java.io.IOException;

public class IfKeyword {
    private Condition condition;
    private StmtSeq thenStmtSeq;
    private StmtSeq elseStmtSeq = null;
    private VariableTable vTable;

    public IfKeyword(VariableTable vTable) {
        this.vTable = vTable;
        this.thenStmtSeq = new StmtSeq(vTable);
    }

    void parse(Scanner s) throws IOException {
        if (s.currentToken() != Core.IF) {
            System.out.println("ERROR: IF keyword expected, found: " + s.currentToken());
            System.exit(0);
        }

        s.nextToken();
        condition = new Condition(vTable);
        condition.parse(s);

        ParserUtils.handleExpectedToken(s, Core.THEN);

        if (!ParserUtils.isStartOfStmt(s.currentToken())) {
            System.out.println("ERROR: expected a statement, but found " + s.currentToken());
            System.exit(0);
        }
        thenStmtSeq.parse(s);

        if (s.currentToken() == Core.ELSE) {
            // Consume ELSE
            s.nextToken();
            elseStmtSeq = new StmtSeq(vTable); // Note: We need to pass the vTable to StmtSeq

            if (!ParserUtils.isStartOfStmt(s.currentToken())) {
                System.out.println("ERROR: expected a statement, but found " + s.currentToken());
                System.exit(0);
            }
            elseStmtSeq.parse(s);
        } else if (s.currentToken() != Core.END) {
            System.out.println("ERROR: Incorrect If structure! Expected ELSE or END, found: " + s.currentToken());
            System.exit(0);
        }
        // consume END token
        s.nextToken();
    }

    void print() {
        System.out.print("if ");
        condition.print();
        System.out.println(" then");

        thenStmtSeq.print();

        if (elseStmtSeq != null) {
            System.out.println( "else");
            elseStmtSeq.print();
        }

        System.out.println( "end");
    }

    void execute(Scanner dataScanner){
        if(condition.execute()){
            vTable.enterLocalScope();
            thenStmtSeq.execute(dataScanner);
            //TODO: before leaving the scope, we need to set the ref count of all variables to 0
            vTable.decrementBlockVariable();
            vTable.leaveLocalScope();
        }else if(elseStmtSeq != null){
            vTable.enterLocalScope();
            elseStmtSeq.execute(dataScanner);
            vTable.decrementBlockVariable();
            vTable.leaveLocalScope();
        }
    }


}
