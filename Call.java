import java.io.IOException;

public class Call {
    private Parameters parameters;
    private String functionName;

    private VariableTable vTable;

    public Call(VariableTable vTable){
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
        functionName=scanner.getId();
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
        Function function=vTable.getFunctionByName(functionName);
        vTable.enterScope();
        function.execute(dataScanner, parameters);
        vTable.leaveScope();
    }



}
