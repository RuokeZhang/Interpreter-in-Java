import java.io.IOException;
import Parser.Procedure;
import Parser.VariableTable;
import Parser.Scanner;




public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Main <input_file>");
            System.exit(1);
        }

        Scanner scanner;
        scanner = new Scanner(args[0]);
        VariableTable vTable = new VariableTable();
        Procedure procedure = new Procedure(vTable);

        try {
            procedure.parse(scanner);
            procedure.execute(args[1]);
        } catch (IOException e) {
            System.out.println("IO error while reading " + args[0]);
        }

    }
}
