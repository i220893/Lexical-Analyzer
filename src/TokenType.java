/**
 * TokenType Enum
 * Defines all token types recognized by the lexical analyzer
 */
public enum TokenType {
    // Literals
    IDENTIFIER,
    INTEGER_LITERAL,
    FLOAT_LITERAL,
    BOOLEAN_LITERAL,
    
    // Punctuators
    LPAREN,          // (
    RPAREN,          // )
    LBRACE,          // {
    RBRACE,          // }
    LBRACKET,        // [
    RBRACKET,        // ]
    COMMA,           // ,
    SEMICOLON,       // ;
    COLON,           // :
    
    /* Operators removed as per user request */
    
    // Comments
    COMMENT,
    
    // Special
    WHITESPACE,
    EOF,
    ERROR
}
