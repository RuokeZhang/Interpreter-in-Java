import java.io.IOException;
import Parser.Procedure;
import Parser.VariableTable;
import Parser.Scanner;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Main <input_file> <data_file>");
            System.exit(1);
        }

        Scanner scanner = new Scanner(args[0]);
        Scanner dataScanner = new Scanner(args[1]);

        VariableTable vTable = new VariableTable();
        Procedure procedure = new Procedure(vTable, dataScanner);

        try {
            procedure.parse(scanner);
            procedure.execute();
        } catch (IOException e) {
            System.out.println("IO error while reading " + args[0]);
        }
    }
}
