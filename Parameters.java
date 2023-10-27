import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.io.IOException;

public class Parameters {
    private List<String> parameters = new ArrayList<>();

    private VariableTable vTable;

    public Parameters(VariableTable vTable){
        this.vTable = vTable;
    }

    void parse(Scanner scanner) throws IOException{

        // Validating the parameter name
        if (scanner.currentToken() != Core.ID) {
            System.out.println("ERROR: Expected an ID token for the parameter name");
            System.exit(0);
        }
        parameters.add(scanner.getId());
        scanner.nextToken();

        if(scanner.currentToken()==Core.COMMA){
            scanner.nextToken();
            parse(scanner);
        }
    }

    void print() {
        for (String parameter : parameters) {
            System.out.print(parameter);
            System.out.println(",");
        }
    }

    List<String> getParameters(){
        return parameters;
    }
}
