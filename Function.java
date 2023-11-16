import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Function {
    private String functionName;
    private StmtSeq stmtSeq;
    private VariableTable vTable;
    private Parameters formalParameters;
    private Scanner dataScanner;

    public Function(VariableTable vTable, Scanner dataScanner) {
        this.vTable = vTable;
        this.dataScanner = dataScanner;  // Initialize dataScanner
    }

    /**
     * Parses the function from the scanner input.
     *
     * @param scanner The scanner object.
     * @throws IOException If there's an issue reading the input.
     */
    public void parse(Scanner scanner) throws IOException {
        // Expecting a PROCEDURE token
        ParserUtils.handleExpectedToken(scanner, Core.PROCEDURE);

        // Validating the procedure name
        if (scanner.currentToken() != Core.ID) {
            System.out.println("ERROR: Expected an ID token for the procedure name");
            System.exit(0);
        }
        functionName = scanner.getId();
        scanner.nextToken();

        // Expecting a LPAREN token
        ParserUtils.handleExpectedToken(scanner, Core.LPAREN);

        // Parsing parameters
        formalParameters = new Parameters(vTable);
        formalParameters.parse(scanner);

        // Expecting a RPAREN token
        ParserUtils.handleExpectedToken(scanner, Core.RPAREN);

        //Expecting a IS token
        ParserUtils.handleExpectedToken(scanner, Core.IS);

        //Parsing statements sequence
        stmtSeq = new StmtSeq(vTable);
        stmtSeq.parse(scanner);

        //Expecting an END token
        ParserUtils.handleExpectedToken(scanner, Core.END);
    }

    final String getFunctionName() {
        return functionName;
    }

    void print() {
        System.out.print("procedure " + functionName + "(");
        formalParameters.print();
        System.out.println(")");
        System.out.println("is");
        stmtSeq.print();
        System.out.println("end;");
    }

    void execute(Scanner dataScanner, List<Value> actualParameters) {
        checkDuplicateFormalParameters();
        vTable.enterLocalScope();
        setParameters(actualParameters);
        stmtSeq.execute(dataScanner);
        vTable.decrementBlockVariable();
        vTable.leaveLocalScope();
    }

    void checkDuplicateFormalParameters() {
        HashMap<String, Integer> map = new HashMap<>();
        for (String parameter : formalParameters.getParameters()) {
            if (map.containsKey(parameter)) {
                System.out.println("ERROR: Duplicate formal parameter " + parameter);
                System.exit(1);
            }
            map.put(parameter, 1);
        }
    }

    void setParameters(List<Value> actualParameters) {
        List<String> formalParamNames = formalParameters.getParameters();
        if (formalParamNames.size() != actualParameters.size()) {
            System.out.println("ERROR: Number of formal parameters and actual parameters do not match");
            System.exit(1);
        }
        for (int i = 0; i < formalParamNames.size(); i++) {
            String formalParamName = formalParamNames.get(i);
            vTable.addVariable(formalParamName, Core.ARRAY);
            vTable.store(formalParamName, actualParameters.get(i));
            vTable.incrementRefCount(formalParamName);
        }
    }

    void clearFormalParameters() {
        List<String> formalParamNames = formalParameters.getParameters();
        for (String formalParamName : formalParamNames) {
            vTable.decrementRefCount(formalParamName);
        }
    }


}
