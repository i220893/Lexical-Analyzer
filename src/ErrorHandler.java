import java.util.*;

/**
 * ErrorHandler Class
 * Detects, reports, and recovers from lexical errors
 */
public class ErrorHandler {
    
    /**
     * Inner class to represent a lexical error
     */
    private class LexicalError {
        String errorType;
        int line;
        int column;
        String lexeme;
        String reason;
        
        LexicalError(String errorType, int line, int column, String lexeme, String reason) {
            this.errorType = errorType;
            this.line = line;
            this.column = column;
            this.lexeme = lexeme;
            this.reason = reason;
        }
        
        @Override
        public String toString() {
            return String.format("[%s] Line: %d, Col: %d, Lexeme: \"%s\" - %s", 
                               errorType, line, column, lexeme, reason);
        }
    }
    
    private List<LexicalError> errors;
    
    /**
     * Constructor
     */
    public ErrorHandler() {
        this.errors = new ArrayList<>();
    }
    
    /**
     * Report an error
     * @param errorType Type of error
     * @param line Line number
     * @param column Column number
     * @param lexeme The problematic lexeme
     * @param reason Explanation of the error
     */
    public void reportError(String errorType, int line, int column, String lexeme, String reason) {
        errors.add(new LexicalError(errorType, line, column, lexeme, reason));
    }
    
    /**
     * Check if any errors were encountered
     * @return true if errors exist, false otherwise
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
    
    /**
     * Get total error count
     */
    public int getErrorCount() {
        return errors.size();
    }
    
    /**
     * Display all errors
     */
    public void displayErrors() {
        if (errors.isEmpty()) {
            System.out.println("\n=== NO LEXICAL ERRORS ===");
            return;
        }
        
        System.out.println("\n=== LEXICAL ERRORS ===");
        System.out.println("Total Errors: " + errors.size());
        System.out.println();
        
        for (LexicalError error : errors) {
            System.out.println(error);
        }
    }
    
    /**
     * Clear all errors
     */
    public void clearErrors() {
        errors.clear();
    }
}
