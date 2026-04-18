import java.io.*;

/**
 * JFlex Test Driver
 * Reads a file, runs the generated JFlex Scanner, and prints results
 */
public class JFlexTest {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java JFlexTest <input_file>");
            System.exit(1);
        }

        String inputFile = args[0];
        try {
            System.out.println("Scanning file (JFlex): " + inputFile);
            System.out.println("----------------------------------------");

            Reader reader = new FileReader(inputFile);
            Yylex scanner = new Yylex(reader);

            Token token;
            while ((token = scanner.yylex()) != null) {
                System.out.println(token);
            }

            // Stats
            System.out.println("\n=== SCANNER STATISTICS ===");
            System.out.println("Total Tokens: " + scanner.getTotalTokens());

            // Symbol Table
            scanner.getSymbolTable().display();

            // Errors
            scanner.getErrorHandler().displayErrors();

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error during scanning: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
