# Lexical Analyzer for Custom Programming Language

## Project Overview
This project implements a Lexical Analyzer (Scanner) for a custom programming language as part of a Compiler Design assignment. The scanner is implemented in two ways:
1. **Manual Implementation**: Using Regular Expressions, NFA, and DFA
2. **JFlex Implementation**: Using JFlex tool for validation

## Team Members
- Hussain Waseem Syed (22I-0893)
- Mohammad Abbas (22I-1409)

## Language Specification

### Language Name
**CustomLang** (File extension: `.lang`)

### Keywords
The language currently defines the following reserved words (Boolean literals):
- `true`
- `false`

### Identifiers
**Rules:**
- Must start with an uppercase letter (A-Z)
- Followed by lowercase letters (a-z), digits (0-9), or underscores (_)
- Maximum length: 31 characters

**Valid Examples:**
- `Count`
- `Variable_name`
- `X`
- `Total_sum_2024`

**Invalid Examples:**
- `count` (starts with lowercase)
- `_Variable` (starts with underscore)
- `2Count` (starts with digit)
- `myVariable` (starts with lowercase)
- `Identifier_too_long_exceeding_limits` (length > 31)

### Literals

#### Integer Literals
**Format:** `[+-]?[0-9]+`

**Examples:**
- `42`
- `+100`
- `-567`
- `0`

#### Floating-Point Literals
**Format:** `[+-]?[0-9]+\.[0-9]{1,6}([eE][+-]?[0-9]+)?`

**Examples:**
- `3.14`
- `+2.5`
- `-0.123456`
- `1.5e10`
- `2.0E-3`

**Invalid:**
- `3.` (no decimal digits)
- `.14` (no integer part)
- `1.2345678` (more than 6 decimal places)
- `1.2.3` (multiple decimal points)

#### Boolean Literals
**Values:** `true`, `false` (case-sensitive)

### Punctuators
- `(` - Left Parenthesis
- `)` - Right Parenthesis
- `{` - Left Brace
- `}` - Right Brace
- `[` - Left Bracket
- `]` - Right Bracket
- `,` - Comma
- `;` - Semicolon
- `:` - Colon

*Note: Mathematical operators are not part of the required token set.*

### Comments
**Single-line:** `##` followed by any characters until end of line

**Example:**
```
## This is a comment
Count = 42;  ## Inline comment
```

## Compilation and Execution

### Prerequisites
- Java Development Kit (JDK) installed
- `javac` and `java` commands in PATH

### Quick Start (Windows)
Use the provided batch script to compile and run all tests:
```cmd
run_tests.bat
```

### Manual Compilation
To compile and run manually:

**1. Manual Scanner:**
```bash
# Compile
javac -d bin src/*.java

# Run Test Driver
java -cp bin ManualTest tests/test1.lang
```

**2. JFlex Scanner:**
```bash
# Generate scanner (requires JFlex)
jflex src/Scanner.flex

# Compile
javac -d bin src/*.java

# Run generated scanner
java -cp bin Yylex tests/test1.lang
```

## Sample Programs

### Sample 1: Variable Declarations
```
## Variable declarations
Count = 42;
Total = 100;
Average = 3.14;
Is_valid = true;
```

### Sample 2: Complex Expressions
```
## Mathematical expressions
Result = +123;
Pi = 3.141592;
Scientific = 1.5e10;
Negative = -42;
```

### Sample 3: All Token Types
```
## Demonstrating all token types
Counter = 0;
Max_value = 100;
Rate = 2.5;
Active = true;
Data = [1, 2, 3];
```

## Project Structure
```
CompilerAssignment/
├── src/
│   ├── ManualScanner.java   # Core DFA implementation
│   ├── Token.java           # Token class definition
│   ├── TokenType.java       # Token enum types
│   ├── SymbolTable.java     # Symbol table management
│   ├── ErrorHandler.java    # Error reporting system
│   ├── ManualTest.java      # Test driver
│   ├── TokenTest.java       # Unit test for Token class
│   └── Scanner.flex         # JFlex specification
├── docs/
│   ├── Automata_Design.pdf  # (Deliverable 1.1)
│   ├── LanguageGrammar.txt  # Formal specifications
│   ├── Comparison.pdf       # Analysis of scanner differences
│   └── README.md            # This file
├── tests/
│   ├── test1.lang           # Valid tokens
│   ├── test2.lang           # Complex expressions
│   ├── test3.lang           # Edge cases
│   ├── test4.lang           # Lexical errors
│   ├── test5.lang           # Comment testing
│   └── TestResults.txt      # Test execution outcomes
└── Task.txt                 # Assignment instructions
```

## Features
- ✅ **DFA-based token recognition**: Manually implemented state machine
- ✅ **Longest match principle**: Correctly handles ambiguous prefixes
- ✅ **Symbol table management**: Tracks identifier occurrence and frequency
- ✅ **Comprehensive error handling**: Detects valid chars, malformed literals, and invalid identifiers
- ✅ **Statistics reporting**: Counts total tokens, types, lines, and stripped comments
- ✅ **Line and column tracking**: Precise location info for tokens and errors

## Notes
- The scanner implements proper pattern matching priority: Comments > Booleans > Identifiers > Numbers
- The Comparison document (`docs/Comparison.pdf`) details minor differences in error reporting strategies between the Manual and JFlex implementations (e.g., handling of `2Count`).
