import java.util.*;

/**
 * ManualScanner - DFA-based Lexical Analyzer
 * Implements token recognition using manually coded DFA logic
 */
public class ManualScanner {
    private String input;
    private int length;
    private int position;
    private int line;
    private int column;
    private int startLine;
    private int startColumn;
    
    private List<Token> tokens;
    private SymbolTable symbolTable;
    private ErrorHandler errorHandler;
    
    // Statistics
    private int totalTokens;
    private Map<TokenType, Integer> tokenCounts;
    private int linesProcessed;
    private int commentsRemoved;
    
    /**
     * Constructor
     * @param input Source code to scan
     */
    public ManualScanner(String input) {
        this.input = input;
        this.length = input.length();
        this.position = 0;
        this.line = 1;
        this.column = 1;
        this.tokens = new ArrayList<>();
        this.symbolTable = new SymbolTable();
        this.errorHandler = new ErrorHandler();
        this.tokenCounts = new HashMap<>();
        this.totalTokens = 0;
        this.commentsRemoved = 0;
        
        // Initialize counts
        for (TokenType type : TokenType.values()) {
            tokenCounts.put(type, 0);
        }
    }
    
    /**
     * Main scanning method
     * @return List of tokens
     */
    public List<Token> scan() {
        while (position < length) {
            char current = peek();
            
            // 1. Skip Whitespace
            if (Character.isWhitespace(current)) {
                // Track newlines for line counting
                if (current == '\n') {
                    line++;
                    linesProcessed++; // Count every line encountered
                    column = 1;
                } else {
                    column++;
                }
                advance();
                continue;
            }
            
            // Store start position of the token
            startLine = line;
            startColumn = column;
            
            // 2. Comments (Priority 1)
            // Pattern: ##[^\n]*
            if (current == '#' && peekNext() == '#') {
                scanComment();
                continue;
            }
            
            // 3. Boolean Literals (Priority 2) & Identifiers (Priority 3)
            // Identifiers must start with Upper Case. Booleans starts with lower case.
            // Invalid Identifiers might start with lower case (error case).
            if (Character.isLetter(current)) {
                if (Character.isUpperCase(current)) {
                    scanIdentifier();
                } else {
                    // Lowercase start - could be 'true', 'false', or invalid identifier
                    scanBooleanOrInvalidIdentifier();
                }
                continue;
            }
            
            // 4. Numbers (Integer & Float) - Check for Digit or Sign followed by Digit
            if (Character.isDigit(current) || 
               ((current == '+' || current == '-') && Character.isDigit(peekNext()))) {
                scanNumber();
                continue;
            }
            
            // 5. Punctuators & Operators
            if (isPunctuator(current)) {
                scanPunctuator();
                continue;
            }
            
            // 6. Unknown/Invalid Character
            handleInvalidCharacter();
        }
        
        // Add final stats
        if (column > 1) linesProcessed = line; // Ensure last line is counted
        
        return tokens;
    }
    
    // --- Scanning Logic Methods ---
    
    private void scanComment() {
        // Consume both #
        advance(); 
        advance();
        
        StringBuffer commentContent = new StringBuffer();
        while (position < length && peek() != '\n') {
            commentContent.append(advance());
        }
        
        // Note: We do not add COMMENT tokens to the list as per "Removes unnecessary whitespace/comments"
        // But we increment the count
        commentsRemoved++;
        
        // Don't consume the newline here, let the whitespace handler deal with it
    }
    
    private void scanIdentifier() {
        // Pattern: [A-Z][a-z0-9_]{0,30}
        StringBuilder sb = new StringBuilder();
        sb.append(advance()); // Consume uppercase start
        
        while (position < length) {
            char c = peek();
            if ((c >= 'a' && c <= 'z') || Character.isDigit(c) || c == '_') {
                sb.append(advance());
            } else {
                break;
            }
        }
        
        String lexeme = sb.toString();
        
        // Check length constraint
        if (lexeme.length() > 31) {
            errorHandler.reportError("Invalid Identifier", startLine, startColumn, lexeme, 
                                   "Identifier exceeds maximum length of 31 characters");
            return; // Skip adding token
        }
        
        // Check for invalid characters inside? (Already handled by loop condition)
        
        addToken(TokenType.IDENTIFIER, lexeme);
        symbolTable.addSymbol(lexeme, "Identifier", startLine, startColumn);
    }
    
    private void scanBooleanOrInvalidIdentifier() {
        // Starts with lowercase. Check if it matches "true" or "false"
        StringBuilder sb = new StringBuilder();
        while (position < length && Character.isLetter(peek())) {
            sb.append(advance());
        }
        
        String lexeme = sb.toString();
        
        if (lexeme.equals("true") || lexeme.equals("false")) {
            addToken(TokenType.BOOLEAN_LITERAL, lexeme);
        } else {
            // It's a string of letters starting with lowercase, but not boolean -> Invalid Identifier
            errorHandler.reportError("Invalid Identifier", startLine, startColumn, lexeme, 
                                   "Identifiers must start with an uppercase letter");
        }
    }
    
    private void scanNumber() {
        StringBuilder sb = new StringBuilder();
        
        // 1. Optional Sign
        if (peek() == '+' || peek() == '-') {
            sb.append(advance());
        }
        
        // 2. Integer Part
        while (position < length && Character.isDigit(peek())) {
            sb.append(advance());
        }
        
        boolean isFloat = false;
        boolean hasError = false;
        String errorMessage = "";
        
        // 3. Optional Decimal Part
        if (position < length && peek() == '.') {
            isFloat = true;
            sb.append(advance());
            
            // Check for digits after dot
            int decimalDigits = 0;
            while (position < length && Character.isDigit(peek())) {
                sb.append(advance());
                decimalDigits++;
            }
            
            if (decimalDigits == 0) {
                hasError = true;
                errorMessage = "Floating point literal must have digits after decimal point";
            } else if (decimalDigits > 6) {
                hasError = true;
                errorMessage = "Floating point literal exceeds maximum of 6 decimal places";
            }
        }
        
        // 4. Optional Exponent Part
        if (position < length && (peek() == 'e' || peek() == 'E')) {
            isFloat = true; // Scientific notation implies float
            sb.append(advance());
            
            if (position < length && (peek() == '+' || peek() == '-')) {
                sb.append(advance());
            }
            
            int expDigits = 0;
            while (position < length && Character.isDigit(peek())) {
                sb.append(advance());
                expDigits++;
            }
            
            if (expDigits == 0) {
                hasError = true;
                if(errorMessage.isEmpty()) errorMessage = "Exponent must be followed by digits";
            }
        }
        
        // 5. Lookahead check for errors (e.g. 1.2.3)
        if (position < length && peek() == '.') {
             sb.append(advance());
             // Consume rest to form the invalid lexeme
             while (position < length && Character.isDigit(peek())) sb.append(advance());
             hasError = true;
             errorMessage = "Malformed literal: Multiple decimal points";
        }
        
        String lexeme = sb.toString();
        
        if (hasError) {
            errorHandler.reportError("Malformed Literal", startLine, startColumn, lexeme, errorMessage);
            return;
        }
        
        if (isFloat) {
            addToken(TokenType.FLOAT_LITERAL, lexeme);
        } else {
            addToken(TokenType.INTEGER_LITERAL, lexeme);
        }
    }
    
    private void scanPunctuator() {
        char c = advance();
        String lexeme = String.valueOf(c);
        TokenType type = null;
        
        switch (c) {
            case '(': type = TokenType.LPAREN; break;
            case ')': type = TokenType.RPAREN; break;
            case '{': type = TokenType.LBRACE; break;
            case '}': type = TokenType.RBRACE; break;
            case '[': type = TokenType.LBRACKET; break;
            case ']': type = TokenType.RBRACKET; break;
            case ',': type = TokenType.COMMA; break;
            case ';': type = TokenType.SEMICOLON; break;
            case ':': type = TokenType.COLON; break;
            default: break;
        }
        
        if (type != null) {
            addToken(type, lexeme);
        }
    }
    
    private void handleInvalidCharacter() {
        char c = advance();
        errorHandler.reportError("Invalid Character", startLine, startColumn, String.valueOf(c), 
                               "Character not supported by language");
    }
    
    // --- Helper Methods ---
    
    private char peek() {
        if (position >= length) return '\0';
        return input.charAt(position);
    }
    
    private char peekNext() {
        if (position + 1 >= length) return '\0';
        return input.charAt(position + 1);
    }
    
    private char advance() {
        if (position >= length) return '\0';
        char c = input.charAt(position);
        position++;
        column++;
        return c;
    }
    
    private boolean isPunctuator(char c) {
        return "(){}[],;:".indexOf(c) != -1;
    }
    
    private void addToken(TokenType type, String lexeme) {
        // Skip adding ERROR tokens, we just log them
        if (type == TokenType.ERROR) return;
        
        Token token = new Token(type, lexeme, startLine, startColumn);
        tokens.add(token);
        totalTokens++;
        
        // Update stats
        tokenCounts.put(type, tokenCounts.getOrDefault(type, 0) + 1);
    }
    
    /**
     * Display statistics
     */
    public void displayStatistics() {
        System.out.println("\n=== SCANNER STATISTICS ===");
        System.out.println("Total Tokens:     " + totalTokens);
        System.out.println("Lines Processed:  " + linesProcessed);
        System.out.println("Comments Removed: " + commentsRemoved);
        System.out.println("\nToken Counts by Type:");
        
        // Print in a standard order or sorted
        for (TokenType type : TokenType.values()) {
            int count = tokenCounts.getOrDefault(type, 0);
            if (count > 0 || isEssentialType(type)) { // Show essential types even if 0
                System.out.println(String.format("  %-15s : %d", type, count));
            }
        }
    }
    
    private boolean isEssentialType(TokenType type) {
        // Types to always show in stats
        return type == TokenType.IDENTIFIER || 
               type == TokenType.INTEGER_LITERAL || 
               type == TokenType.FLOAT_LITERAL;
    }
    
    public SymbolTable getSymbolTable() { return symbolTable; }
    public ErrorHandler getErrorHandler() { return errorHandler; }
}
