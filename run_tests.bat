@echo off
if not exist bin mkdir bin

echo Compiling Java files...
javac -d bin src/*.java
if %errorlevel% neq 0 (
    echo Compilation Failed!
    pause
    exit /b 1
)

echo.
echo Running Token Test...
java -cp bin TokenTest
if %errorlevel% neq 0 (
    echo TokenTest Failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Running Manual Scanner on Test Files
echo ========================================

echo.
echo --- Test 1: Valid Tokens ---
java -cp bin ManualTest tests/test1.lang

echo.
echo --- Test 2: Complex Expressions ---
java -cp bin ManualTest tests/test2.lang

echo.
echo --- Test 3: Edge Cases ---
java -cp bin ManualTest tests/test3.lang

echo.
echo --- Test 4: Lexical Errors ---
java -cp bin ManualTest tests/test4.lang

echo.
echo --- Test 5: Comments ---
java -cp bin ManualTest tests/test5.lang

echo.
echo ========================================
echo Running JFlex Scanner Tests
echo ========================================

call jflex src/Scanner.flex
if %errorlevel% neq 0 (
    echo JFlex Generation Failed!
    pause
    exit /b 1
)

javac -d bin src/*.java
if %errorlevel% neq 0 (
    echo JFlex Compilation Failed!
    pause
    exit /b 1
)

echo.
echo --- Test 1: Valid Tokens (JFlex) ---
java -cp bin JFlexTest tests/test1.lang

echo.
echo --- Test 2: Complex Expressions (JFlex) ---
java -cp bin JFlexTest tests/test2.lang

echo.
echo --- Test 3: Edge Cases (JFlex) ---
java -cp bin JFlexTest tests/test3.lang

echo.
echo --- Test 4: Lexical Errors (JFlex) ---
java -cp bin JFlexTest tests/test4.lang

echo.
echo --- Test 5: Comments (JFlex) ---
java -cp bin JFlexTest tests/test5.lang

echo.
echo Done.
pause
