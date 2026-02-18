/**
 * Token Class
 * Represents a single token recognized by the lexical analyzer
 * Compatible with both Manual Scanner and JFlex implementation
 */
public class Token {
    private TokenType type;
    private String lexeme;
    private int line;
    private int column;
    
    /**
     * Constructor
     * @param type The token type
     * @param lexeme The actual text of the token
     * @param line Line number where token appears
     * @param column Column number where token starts
     */
    public Token(TokenType type, String lexeme, int line, int column) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }
    
    // Getters
    public TokenType getType() {
        return type;
    }
    
    public String getLexeme() {
        return lexeme;
    }
    
    public int getLine() {
        return line;
    }
    
    public int getColumn() {
        return column;
    }
    
    /**
     * Returns string representation in required format:
     * <TokenType, "lexeme", Line: X, Col: Y>
     */
    @Override
    public String toString() {
        return String.format("<%s, \"%s\", Line: %d, Col: %d>", 
                           type, lexeme, line, column);
    }
}
