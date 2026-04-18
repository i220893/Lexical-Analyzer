import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Manual Test Driver
 * Reads a file, runs the Manual Scanner, and prints results
 */
public class ManualTest {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java ManualTest <input_file>");
            System.exit(1);
        }
        
        String inputFile = args[0];
        try {
            String content = new String(Files.readAllBytes(Paths.get(inputFile)));
            System.out.println("Scanning file: " + inputFile);
            System.out.println("----------------------------------------");
            
            ManualScanner scanner = new ManualScanner(content);
            List<Token> tokens = scanner.scan();
            
            // Print Tokens
            System.out.println("TOKENS:");
            for (Token token : tokens) {
                System.out.println(token);
            }
            
            // Print Statistics
            scanner.displayStatistics();
            
            // Print Symbol Table
            scanner.getSymbolTable().display();
            
            // Print Errors
            scanner.getErrorHandler().displayErrors();
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error during scanning: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
