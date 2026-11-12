/* enum: a fixed list of named constants */
public enum TokenType {
    // Reserved Keywords:
    OUTPUT(1),
    ON (1),
    CREATE(1),
    CONSTANT(1),
    ELSE_IF(1),
    ME(1),
    UNTIL(1),
    PUBLIC(1),
    PRIVATE(1),
    ALERT(1),
    DETECT(1),
    ALWAYS(1),
    CHECK(1),
    PARENT(1),
    BLUEPRINT(1),
    NATIVE(1),
    INHERITS(1),
    CAST(1),
    INPUT(1),
    SAY(1),
    NOW(1),
    WHILE(1),
    PACKAGE_NAME(1),
    TIMES(1),
    REPEAT(1),
    ELSE(1),
    RETURNS(1),
    RETURN(1),
    AND(1),
    OR(1),
    NULL(1),
    STATIC(1),
    ACTION(1),
    INTEGER_KEYWORD(1),
    NUMBER_KEYWORD(1),
    TEXT(1),
    BOOLEAN_KEYWORD(1),
    USE(1),
    NOT(1),
    IF(1),
    END(1),
    CLASS(1),
    BOOLEAN(1),

    // IDs & Literals:
    INT(2),
    DECIMAL(3),
    ID(4),
    STRING(5),

    // Punctuation:
    COLON(6),
    PERIOD(6),
    COMMA(6),

    // Operators:
    EQUALITY(7),
    GREATER(7),
    GREATER_EQUAL(7),
    LESS(7),
    LESS_EQUAL(7),
    PLUS(7),
    MINUS(7),
    MULTIPLY(7),
    DIVIDE(7),
    MODULO(7),
    NOTEQUALS(7),

    // Grouping:
    LEFT_SQR_BRACE(8),
    RIGHT_SQR_BRACE(8),
    LEFT_PAREN(8),
    RIGHT_PAREN(8),

    /*
    // Other:
    WS
    DOUBLE_QUOTE
    */

    // What I added:
    COMMENT(9),
    ERROR(-1);

    // Token Category Number
    private final int category;
    // Constructor: Assign category number to object
    TokenType(int category) {
        this.category = category;
    }
    // Getter
    public int getCategory() {
        return category;
    }
    // Associate category number with appropriate string:
    public String getCategoryName(){
       return switch(category) {
            case 1 -> "keyword";
            case 2 -> "integer literal";
            case 3 -> "decimal literal";
            case 4 -> "identifier";
            case 5 -> "string literal";
            case 6 -> "punctuation";
            case 7 -> "operator";
            case 8 ->  "grouping";
            case 9 ->  "comment";
            case -1 -> "error";
            default -> "unknown";
        };
    }

}
