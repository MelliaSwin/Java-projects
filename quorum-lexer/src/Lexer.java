import java.util.ArrayList;
import java.util.List;

public class Lexer {
    // Defined variables
    private final String input;
    private int index;

    // Constructor: that takes in an input string and places in object
    public Lexer(String input) {
        this.input = input;
        this.index = 0; // Position starts at 0
    }

    /*
    My Lexer:
    1. Skip WS/Comments
    2. Assess (Based on first character in token)
    3. Assemble (Assembles appended characters into string, num, op)
    4. Categorize (Places the assembled word into the correct Token)
    */

    /* Tokenize Function */
    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();         // List holds all of our tokens
        while (index < input.length()) {               // Loop through each character in str
            char c = input.charAt(index);             // char c holds character at index (pos)

            // 1. Skip Whitespace:
            if(Character.isWhitespace(c)) {
                index++;
                continue;
            }
            // Skip Comments:
                                                             // If current char is '/', this MAY be com/com block
            if(c == '/' && index + 1 < input.length()) {    // && if there is a next character:
                char next = input.charAt(index + 1);       // char next is the character NEXT TO our current char
                if(next == '/' || next == '*') {          // If next is the second '/' or '*'
                    Token comment = readComment();       // Extract comment
                    if(comment != null) {               // Add to list if not null
                        tokens.add(comment);
                    }
                    continue;                         // Back up to while loop if this was a comment
                }
            }                                        // Else, not a comment, continue to assess character

            Token token = assess();                 // Call assess() to assess the next token starting at this char
            if(token != null) {
                tokens.add(token);                 // Add token to list
            }
        }
        return tokens;
    }

    /* Comment function: Extract the text */
    private Token readComment() {
        char next = input.charAt(index + 1);
        // If next character is '/', read until the end of line:
        if(next == '/'){
            index += 2; // Update index to skip '//'
            StringBuilder comment = new StringBuilder();
            while(index < input.length() && input.charAt(index) != '\n') {
                comment.append(input.charAt(index));
                index++; // Update index until we reach input.length()
            }
            return new Token(TokenType.COMMENT, comment.toString().trim());
        }

        // If next character is '*', read until we hit * and / ' /*...*/ '
        if(next == '*'){
            index += 2; // Update index to skip '/*'
            StringBuilder comment = new StringBuilder();
            while(index + 1 < input.length()) {
                // Checker: Once we reach the end of block comment, return
                if(input.charAt(index) == '*' && input.charAt(index + 1) == '/') {
                    index += 2; // Update index to skip '*/'
                    return new Token(TokenType.COMMENT, comment.toString().trim());
                } // Else, keep appending
                comment.append(input.charAt(index));
                index++; // Update index until we reach input.length()
            }
            // If the following case: (no end */) above aren't met, then there is an issue with block comment
            return new Token(TokenType.ERROR, "Error with block comment");
        }
        // Else, if none of the above is true, then this is NOT a comment/block comment
        return null;
    }

    /* Assess Function */
    // Assess the first character in token, then assemble, and categorize a token
    private Token assess() {
        if(index >= input.length()){
            return null;                             // If there's no more characters, return null
        }
        char c = input.charAt(index);               // char c holds character at index (pos)

        if(Character.isLetter(c) || c == '_'){    // Assess if this is a letter or underscore (Identifier/Keyword, etc)
            String word = assembleWord();        // Call assembleWord()
            return categorizeWord(word);        // Once our word is assembled, categorize it
        }


        if(Character.isDigit(c)){                  // Assess if this is a digit (Number)
            String number = assembleNumber();     // Call assembleNumber()
            return categorizeNumber(number);     // Once our number is assembled, categorize it
        }

        if(c == '"'){                                    // Assess if this is a string (character has first quote:")
            return assembleString();                    // Call assembleString(), Return the appended string
        }

        return categorizeSymbols();                  // Else: If none of the above, then it's a symbol
    }

    /* Assemble Functions */
    private String assembleWord(){
        StringBuilder sb = new StringBuilder();     // Create StringBuilder object to append characters
        while(index < input.length()){             // Loop through the string
            char c = input.charAt(index);         // char c holds character at index (pos)
            // Check if character is part of a word (e.g,: var1, _helloWorld)
            if(Character.isLetterOrDigit(c) || c == '_'){
                sb.append(c);                   // Append to object sb
                index++;                       // Next character
            }else{
                break;                        // If character is not a letter or '_', break and return the full string
            }
        }
        return sb.toString();               // Return the appended string
    }

    private String assembleNumber(){
        StringBuilder sb = new StringBuilder();      // Create StringBuilder object to append characters
        boolean oneDot = false;                     // This ensures that there isn't more than one '.' after a number

        while(index < input.length()){            // Loop through the string
            char c = input.charAt(index);        // char c holds character at index (pos)
            // Check if character is a number
            if(Character.isDigit(c)){
                sb.append(c);                  // Append to object sb
                index++;                      // Next character
                // Edge Case: Make sure '.' is allowed once
            } else if(c == '.' && !oneDot){
                oneDot = true;              // If we hit '.', switch oneDot to true
                sb.append(c);              // then append to sb
                index++;                  // Next character
                // Edge Case: Make sure the char AFTER '.' is a digit
                if(index < input.length() && !Character.isDigit(input.charAt(index))){
                    break;
                }
            } else {
                break;                  // If character is not number, break and return the full string
            }
        }
        return sb.toString();         // Return the appended string
    }

    private Token assembleString(){
        StringBuilder sb = new StringBuilder();     // Create StringBuilder object to append characters
        index++;                                   // Move to next character after ' " '
        while(index < input.length()){            // Loop through the string
            char c = input.charAt(index);
            // Check if we reached end of string
            if(c == '"'){
                index++;                       // Skip the last ' " '
                return new Token(TokenType.STRING, sb.toString());
            }
            // Else, keep appending characters
            sb.append(c);                   // Append to object sb
            index++;                       // Next character
        }
        // If no closing quote, output ERROR
        return new Token(TokenType.ERROR, "unterminated string");
        // Note about this, I made it so any errors after this error, will simply not output
    }

    /* Categorize Functions */
    private Token categorizeWord(String word){
        // Bring the words to lower case for easy comparison
        String lower= word.toLowerCase();
        // This will be case by case:
        switch(lower){
            // Reserved Keywords:
            case "output":   return new Token(TokenType.OUTPUT, word);
            case "on":       return new Token(TokenType.ON, word);
            case "create":   return new Token(TokenType.CREATE, word);
            case "constant": return new Token(TokenType.CONSTANT, word);
            case "elseif":   return new Token(TokenType.ELSE_IF, word);
            case "me":       return new Token(TokenType.ME, word);
            case "until":    return new Token(TokenType.UNTIL, word);
            case "public":   return new Token(TokenType.PUBLIC, word);
            case "private":  return new Token(TokenType.PRIVATE, word);
            case "alert":    return new Token(TokenType.ALERT, word);
            case "detect":   return new Token(TokenType.DETECT, word);
            case "always":   return new Token(TokenType.ALWAYS, word);
            case "check":    return new Token(TokenType.CHECK, word);
            case "parent":   return new Token(TokenType.PARENT, word);
            case "blueprint":return new Token(TokenType.BLUEPRINT, word);
            case "native":   return new Token(TokenType.NATIVE, word);
            case "is":       return new Token(TokenType.INHERITS, word);
            case "cast":     return new Token(TokenType.CAST, word);
            case "input":    return new Token(TokenType.INPUT, word);
            case "say":      return new Token(TokenType.SAY, word);
            case "now":      return new Token(TokenType.NOW, word);
            case "while":    return new Token(TokenType.WHILE, word);
            case "package":  return new Token(TokenType.PACKAGE_NAME, word);
            case "times":    return new Token(TokenType.TIMES, word);
            case "repeat":   return new Token(TokenType.REPEAT, word);
            case "else":     return new Token(TokenType.ELSE, word);
            case "returns":  return new Token(TokenType.RETURNS, word);
            case "return":   return new Token(TokenType.RETURN, word);
            case "shared":   return new Token(TokenType.STATIC, word);
            case "action":   return new Token(TokenType.ACTION, word);
            case "use":      return new Token(TokenType.USE, word);
            case "if":       return new Token(TokenType.IF, word);
            case "end":      return new Token(TokenType.END, word);
            case "class":    return new Token(TokenType.CLASS, word);

            // Word operators & Other:
            case "and":           return new Token(TokenType.AND, word);
            case "or":            return new Token(TokenType.OR, word);
            case "undefined":     return new Token(TokenType.NULL, word);
            case "mod":           return new Token(TokenType.MODULO, word);
            case "true":          return new Token(TokenType.BOOLEAN, word);
            case "false":         return new Token(TokenType.BOOLEAN, word);
            case "not":           // Handles special case of not and notequals
                if(index < input.length() && input.charAt(index) == '=') {
                    index++; return new Token(TokenType.NOTEQUALS, "not=");
                } else {
                    return new Token(TokenType.NOT, word);
                }

            // DataTypes:
            case "integer":       return new Token(TokenType.INTEGER_KEYWORD, word);
            case "number":        return new Token(TokenType.NUMBER_KEYWORD, word);
            case "text":          return new Token(TokenType.TEXT, word);
            case "boolean":       return new Token(TokenType.BOOLEAN_KEYWORD, word);

            // Else, this is an identifier and NOT a keyword:
            default:
                return new Token(TokenType.ID, word);
        }
    }

    private Token categorizeNumber(String number){
        if(number.contains(".")){                           // Handle decimal case
            return new Token(TokenType.DECIMAL, number);
        } else {                                          // Else, this is just an integer
            return new Token(TokenType.INT, number);
        }
    }

    private Token categorizeSymbols(){
        char c = input.charAt(index); // char c holds character at index (pos)
        // Handles case of two operators combined: >= and <=
        // Here we 'lookahead' at the next index, if there is a '=', we assign it to appropriate token
        if(c == '>' && index + 1 < input.length()
                && input.charAt(index+1) == '='){
            // +=2 to skip over '>='
            index += 2;
            return new Token(TokenType.GREATER_EQUAL, ">=");
        }
        if(c == '<' && index + 1 < input.length()
                && input.charAt(index+1) == '='){
            // +=2 to skip '<='
            index += 2;
            return new Token(TokenType.LESS_EQUAL, "<=");
        }

        // This will be case by case (more enhanced switch case:)
        return switch (c) {
            case '+' -> {
                index++;
                yield new Token(TokenType.PLUS, "+");
            }
            case '-' -> {
                index++;
                yield new Token(TokenType.MINUS, "-");
            }
            case '*' -> {
                index++;
                yield new Token(TokenType.MULTIPLY, "*");
            }
            case '/' -> {
                index++;
                yield new Token(TokenType.DIVIDE, "/");
            }
            case '%' -> {
                index++;
                yield new Token(TokenType.MODULO, "%");
            }
            case '<' -> {
                index++;
                yield new Token(TokenType.LESS, "<");
            }
            case '>' -> {
                index++;
                yield new Token(TokenType.GREATER, ">");
            }
            case '=' -> {
                index++;
                yield new Token(TokenType.EQUALITY, "=");
            }
            case '(' -> {
                index++;
                yield new Token(TokenType.LEFT_PAREN, "(");
            }
            case ')' -> {
                index++;
                yield new Token(TokenType.RIGHT_PAREN, ")");
            }
            case '[' -> {
                index++;
                yield new Token(TokenType.LEFT_SQR_BRACE, "[");
            }
            case ']' -> {
                index++;
                yield new Token(TokenType.RIGHT_SQR_BRACE, "]");
            }
            case ':' -> {
                index++;
                yield new Token(TokenType.COLON, ":");
            }
            case ',' -> {
                index++;
                yield new Token(TokenType.COMMA, ",");
            }
            case '.' -> {
                index++;
                yield new Token(TokenType.PERIOD, ".");
            }

            // If none of the following tokens, then we have an unknown symbol
            default -> {
                String unknownSymbol = Character.toString(c);
                index++;
                yield new Token(TokenType.ERROR, unknownSymbol);
            }
        };
    }
}
