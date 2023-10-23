
import java.io.IOException;

/**
 * Utility class to handle token-related operations for parsing the language.
 * It also provides helper methods to determine the start of specific
 * structures.
 */
public class ParserUtils {

    // The lookahead token to assist in parsing.
    public static Core lookAheadToken = null;

    /**
     * Handle an expected token during parsing.
     *
     * @param s        The scanner instance to read tokens.
     * @param expected The expected token.
     * @throws IOException If there's an issue reading the input.
     */
    public static void handleExpectedToken(Scanner s, Core expected) throws IOException {
        Core foundToken;
        if (lookAheadToken == null) {
            foundToken = s.currentToken();
            if (foundToken == expected) {
                s.nextToken();
            } else {
                displayErrorMessage(expected, foundToken, s);
                System.exit(0);
            }
        } else {
            foundToken = lookAheadToken;
            if (lookAheadToken == expected) {
                lookAheadToken = null;
            } else {
                displayErrorMessage(expected, foundToken, s);
                System.exit(0);
            }
        }
    }

    /**
     * Display an error message when there's a token mismatch.
     *
     * @param expected The expected token.
     * @param found    The token that was found instead.
     * @param s        The scanner instance.
     */
    private static void displayErrorMessage(Core expected, Core found, Scanner s) {
        String errorMessage = "ERROR: Expected " + expected + ", found: ";
        if (found == Core.ID) {
            errorMessage += s.getId();
        } else {
            errorMessage += found;
        }
        System.out.println(errorMessage);
    }

    /**
     * Checks if the provided token is the start of a factor.
     *
     * @param token The token to check.
     * @return True if it starts with a factor, otherwise false.
     */
    public static boolean startsWithFactor(Core token) {
        return token == Core.ID || token == Core.CONST || token == Core.LPAREN;
    }

    /**
     * Checks if the provided token is the start of a statement.
     *
     * @param token The token to check.
     * @return True if it's the start of a statement, otherwise false.
     */
    public static boolean isStartOfStmt(Core token) {
        return token == Core.ID || // Assuming assign starts with an identifier
                token == Core.IF ||
                token == Core.WHILE ||
                token == Core.OUT ||
                token == Core.IN ||
                token == Core.INTEGER || // Assuming INT is a starting token for decl
                token == Core.ARRAY; // Assuming BOOL is another starting token for decl
    }
}
