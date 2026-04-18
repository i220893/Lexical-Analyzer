/**
 * TokenTest Driver
 * A simple utility to verify Token output format
 */
public class TokenTest {
    public static void main(String[] args) {
        System.out.println("Running Token Infrastructure Tests...");
        
        // Test Case 1: Identifier
        Token t1 = new Token(TokenType.IDENTIFIER, "Count", 1, 1);
        System.out.println("Expected: <IDENTIFIER, \"Count\", Line: 1, Col: 1>");
        System.out.println("Actual:   " + t1.toString());
        verify(t1.toString(), "<IDENTIFIER, \"Count\", Line: 1, Col: 1>");
        
        // Test Case 2: Integer
        Token t2 = new Token(TokenType.INTEGER_LITERAL, "42", 1, 9);
        System.out.println("Expected: <INTEGER_LITERAL, \"42\", Line: 1, Col: 9>");
        System.out.println("Actual:   " + t2.toString());
        verify(t2.toString(), "<INTEGER_LITERAL, \"42\", Line: 1, Col: 9>");
        
        // Test Case 3: Semicolon
        Token t3 = new Token(TokenType.SEMICOLON, ";", 2, 15);
        System.out.println("Expected: <SEMICOLON, \";\", Line: 2, Col: 15>");
        System.out.println("Actual:   " + t3.toString());
        verify(t3.toString(), "<SEMICOLON, \";\", Line: 2, Col: 15>");
        
        System.out.println("\nAll Token tests passed!");
    }
    
    private static void verify(String actual, String expected) {
        if (!actual.equals(expected)) {
            System.err.println("TEST FAILED!");
            System.err.println("Expected: " + expected);
            System.err.println("Got:      " + actual);
            System.exit(1);
        }
    }
}
