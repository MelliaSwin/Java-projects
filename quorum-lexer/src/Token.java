/* Token class: Stores the type and value of the token */

// Class Token
public class Token {
    // Immutable objects
    private final TokenType type; // Custom datatype with variable name 'type'
    private final String value;

    // Constructor: Creates a new Token object
    public Token(TokenType type, String value) {
        this.type = type;    // Initializes/Stores type in object
        this.value = value; // Initializes/Stores lexeme in object
    }

    // Getter functions
    public TokenType getType() {
        return type;
    }
    public String getValue() {
        return value;
    }

    // Override default toString() with:
    @Override
    public String toString() {
        if(type == TokenType.ERROR) {
            return "Error: " + value + " not allowed";
        }
        return "Token Category: " +
                type.getCategory() + ", " +
                type + " " + " (" + type.getCategoryName() + ")" + ", " +
               "value " + "\"" + value + "\"";

    }
}
