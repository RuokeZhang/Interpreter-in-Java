import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Call {
    private Parameters parameters;
    private String functionName;

    private VariableTable vTable;

    public Call(VariableTable vTable) {
        this.vTable = vTable;
    }

    final void parse(Scanner scanner) throws IOException {
        // Expecting a BEGIN token
        ParserUtils.handleExpectedToken(scanner, Core.BEGIN);

        // Validating the parameter name
        if (scanner.currentToken() != Core.ID) {
            System.out.println("ERROR: Expected an ID token for the parameter name");
            System.exit(0);
        }
        functionName = scanner.getId();
        scanner.nextToken();

        // Expecting a LPAREN token
        ParserUtils.handleExpectedToken(scanner, Core.LPAREN);

        // Parse parameters
        parameters = new Parameters(vTable);
        parameters.parse(scanner);

        // Expecting a RPAREN token
        ParserUtils.handleExpectedToken(scanner, Core.RPAREN);

        // Expecting a SEMICOLON token
        ParserUtils.handleExpectedToken(scanner, Core.SEMICOLON);
    }

    void print() {
        System.out.print("begin " + functionName + "(");
        parameters.print();
        System.out.println(");");
    }

    void execute(Scanner dataScanner) {
        Function function = vTable.getFunctionByName(functionName);
        if (!vTable.functionExists(functionName)) {
            System.out.println("ERROR: Function " + functionName + " not declared in this scope");
            System.exit(1);
        }
        checkActualParametersDeclared();
        checkActualParametersInitialized();
        List<Value> actualParameters = getParametersValues();
        vTable.enterScope();
        function.execute(dataScanner, actualParameters);
        vTable.leaveScope();
    }

    void checkActualParametersInitialized() {
        List<String> actualParameters = parameters.getParameters();
        for (String actualParameter : actualParameters) {
            if (!vTable.variableIsInitialized(actualParameter)) {
                System.out.println("ERROR: Variable " + actualParameter + " not initialized in this scope");
                System.exit(1);
            }
        }
    }

    void checkActualParametersDeclared() {
        List<String> actualParameters = parameters.getParameters();
        for (String actualParameter : actualParameters) {
            if (!vTable.variableExists(actualParameter)) {
                System.out.println("ERROR: Variable " + actualParameter + " not declared in this scope");
                System.exit(1);
            }
        }
    }

    /**
     * Returns the values of the parameters of the function call
     *
     * @return List of int[] containing the values of the parameters
     */
    List<Value> getParametersValues() {
        List<Value> tempValues = new ArrayList<>();
        for (String actualParamName : parameters.getParameters()) {
            Value value = vTable.getValue(actualParamName);
            tempValues.add(value);
        }
        return tempValues;
    }

    List<Integer> getParametersRc() {
        List<Integer> tempRc = new ArrayList<>();
        for (String actualParamName : parameters.getParameters()) {
            int rc = vTable.getValue(actualParamName).rc;
            tempRc.add(rc);
        }
        return tempRc;
    }
}
