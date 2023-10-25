import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

        //Expecting a LPAREN token
        ParserUtils.handleExpectedToken(scanner, Core.LPAREN);

        //Parse parameters
        parameters = new Parameters(vTable);
        parameters.parse(scanner);

        //Expecting a RPAREN token
        ParserUtils.handleExpectedToken(scanner, Core.RPAREN);

        //Expecting a SEMICOLON token
        ParserUtils.handleExpectedToken(scanner, Core.SEMICOLON);
    }

    void print() {
        System.out.print("begin " + functionName + "(");
        parameters.print();
        System.out.println(");");
    }

    void execute(Scanner dataScanner) {
        Function function = vTable.getFunctionByName(functionName);
        if(!vTable.functionExists(functionName)){
            System.out.println("ERROR: Function "+functionName+" not declared in this scope");
            System.exit(1);
        }
        List<int[]> actualParameters = getParametersValues();
        vTable.enterScope();
        function.execute(dataScanner, actualParameters);
        vTable.leaveScope();
    }

    List<int[]> getParametersValues() {
        List<int[]> tempValues = new ArrayList<>();
        //System.out.println("parameters.getParameters(): " + parameters.getParameters());
        for (String actualParamName : parameters.getParameters()) {
            int[] value = vTable.getArrValue(actualParamName);
            tempValues.add(value);
        }
        return tempValues;
    }


}
