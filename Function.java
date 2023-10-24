import java.io.IOException;
import java.lang.reflect.Parameter;
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
    final String getFunctionName(){
        return functionName;
    }

    void print(){
        System.out.print("procedure " + functionName + "(");
        formalParameters.print();
        System.out.println(")");
        System.out.println("is");
        stmtSeq.print();
        System.out.println("end;");
    }

    void execute(Scanner dataScanner, Parameters actualParameters){
        vTable.enterScope();
        setParameters(actualParameters);
        stmtSeq.execute(dataScanner);
        vTable.leaveScope();
    }

    void setParameters(Parameters actualParameters) {
        List<String> formalParamNames = formalParameters.getParameters();
        List<String> actualParamNames = actualParameters.getParameters();

        for (int i = 0; i < formalParamNames.size(); i++) {
            String formalParamName = formalParamNames.get(i);
            String actualParamName = actualParamNames.get(i);
            vTable.addLocalVariable(formalParamName, Core.ARRAY);
            vTable.store(formalParamName, vTable.getArrValue((actualParamName)));
        }
    }



}