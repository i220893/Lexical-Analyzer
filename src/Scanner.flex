/* ----------------------------------------------------------------------------
   Scanner.flex - JFlex Specification for CustomLang
   ---------------------------------------------------------------------------- */

import java.util.*;

%%

%class Yylex
%public
%unicode
%line
%column
%type Token

%{
    /* Helper variables */
    private SymbolTable symbolTable = new SymbolTable();
    private ErrorHandler errorHandler = new ErrorHandler();
    private int totalTokens = 0;
    
    /* Helper method to create a Token and update stats */
    private Token symbol(TokenType type) {
        return symbol(type, yytext());
    }
    
    private Token symbol(TokenType type, String text) {
        totalTokens++;
        return new Token(type, text, yyline + 1, yycolumn + 1);
    }
    
    /* Error reporting helper */
    private void error(String type, String message) {
        errorHandler.reportError(type, yyline + 1, yycolumn + 1, yytext(), message);
    }
    
    /* Data accessors */
    public SymbolTable getSymbolTable() { return symbolTable; }
    public ErrorHandler getErrorHandler() { return errorHandler; }
    public int getTotalTokens() { return totalTokens; }
%}

/* ----------------------------------------------------------------------------
   Macro Definitions
   ---------------------------------------------------------------------------- */

/* Basic Character Classes */
DIGIT           = [0-9]
LETTER_UPPER    = [A-Z]
LETTER_LOWER    = [a-z]
UNDERSCORE      = [_]
WHITESPACE      = [ \t\f\r\n]+

/* Integer Literal: [+-]?[0-9]+ */
INTEGER         = [+-]?{DIGIT}+

/* Floating Point Literal */
/* Valid: 3.14, +2.5, -0.123456, 1.5e10, 2.0E-3 */
/* Pattern: Sign? Digits . Digits(1-6) Exponent? */
FLOAT           = [+-]?{DIGIT}+\.{DIGIT}{1,6}([eE][+-]?{DIGIT}+)?

/* Invalid Float Patterns (for Error Handling) */
FLOAT_NO_DEC    = [+-]?{DIGIT}+\.
FLOAT_NO_INT    = \.{DIGIT}+
FLOAT_TOO_LONG  = [+-]?{DIGIT}+\.{DIGIT}{7}{DIGIT}*([eE][+-]?{DIGIT}+)?

/* Identifier: [A-Z][a-z0-9_]{0,30} */
IDENTIFIER      = {LETTER_UPPER}({LETTER_LOWER}|{DIGIT}|{UNDERSCORE}){0,30}
IDENTIFIER_LONG = {LETTER_UPPER}({LETTER_LOWER}|{DIGIT}|{UNDERSCORE}){30}({LETTER_LOWER}|{DIGIT}|{UNDERSCORE})+
IDENTIFIER_BAD  = ({LETTER_LOWER}|{DIGIT}|{UNDERSCORE})({LETTER_UPPER}|{LETTER_LOWER}|{DIGIT}|{UNDERSCORE})*

/* Comment: ## followed by anything until newline */
COMMENT         = "##"[^\r\n]*

%%

/* ----------------------------------------------------------------------------
   Lexical Rules (Order matters!)
   ---------------------------------------------------------------------------- */

/* 1. Comments (Ignored) */
{COMMENT} { /* Ignore comments implementation */ }

/* 2. Whitespace (Ignored) */
{WHITESPACE} { /* Ignore whitespace */ }

/* 3. Boolean Literals */
"true"  { return symbol(TokenType.BOOLEAN_LITERAL); }
"false" { return symbol(TokenType.BOOLEAN_LITERAL); }

/* 4. Punctuators & Operators */
"(" { return symbol(TokenType.LPAREN); }
")" { return symbol(TokenType.RPAREN); }
"{" { return symbol(TokenType.LBRACE); }
"}" { return symbol(TokenType.RBRACE); }
"[" { return symbol(TokenType.LBRACKET); }
"]" { return symbol(TokenType.RBRACKET); }
"," { return symbol(TokenType.COMMA); }
";" { return symbol(TokenType.SEMICOLON); }
":" { return symbol(TokenType.COLON); }

/* 5. Floating Point Literals (Must come before Integer to catch decimals) */
{FLOAT} { return symbol(TokenType.FLOAT_LITERAL); }

/* Floating Point Errors */
{FLOAT_NO_DEC}   { error("Malformed Literal", "Missing digits after decimal point"); }
{FLOAT_NO_INT}   { error("Malformed Literal", "Missing integer part before decimal point"); }
{FLOAT_TOO_LONG} { error("Malformed Literal", "Too many decimal places (max 6)"); }

/* 6. Integer Literals */
{INTEGER} { return symbol(TokenType.INTEGER_LITERAL); }

/* 7. Identifiers */
{IDENTIFIER} { 
    symbolTable.addSymbol(yytext(), "Identifier", yyline + 1, yycolumn + 1);
    return symbol(TokenType.IDENTIFIER); 
}

/* Identifier Errors */
{IDENTIFIER_LONG} { error("Invalid Identifier", "Identifier exceeds maximum length of 31 characters"); }
{IDENTIFIER_BAD}  { 
    /* Only flag as bad identifier if it's NOT a number (numbers handled above) */
    /* Checks if it starts with lower case or has invalid structure not caught by other rules */
    error("Invalid Identifier", "Must start with uppercase letter"); 
}

/* 8. Fallback / Error Catch-all */
. { error("Invalid Character", "Character not recognized"); }
