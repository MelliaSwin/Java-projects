grammar Interpreter;

// Statements
start:
        stmt* EOF;                        // '*' Zero or more statements, parse to end of file
                                         // Added 'stmt' took from prev PL's parser grammar (Python)
stmt : label | cond | pExpr;            // Statement can either be a label | a conditional statement | an expression

cond: LPN cExpr RPN ARROW action;     // Conditional MUST be in '()'; '->' points to what we'll do with it
                                     // I wanted to rid keywords like "IF", "WHILE" (C++) and simplify it

label : LETS COLON;                // Example: 'loopName:', declaration of loop name (inspired by assembly)

cExpr: cmpValue (LT | LTE | GT | GTE | E | NE) cmpValue; // Comparison takes place here

cmpValue: INT | LETS | FLT;           // Are the comparing values and INT, LETS, OR FLT?

action: JP LETS | RP INT blockCode; // This is where programmer decides what we do with conditional JP or RP?
                                   // IF ACTION = RP (Repeat), we specify how many times (INT) repeat for block of code
blockCode: LBR stmt* RBR;         // Block of code confined in {...}


// Expressions
pExpr  :                  // Look Ahead in effect (is it a bool or is it an arithmetic expression?)
       boolExpr | arithExpr
       ;

boolExpr: orExpr               // Start with OR (low precedence, im assuming)
        ;

orExpr  : andExpr (OR andExpr)* // OR at top groups || (technically sep &&'s from ||'s), then rest must be && or an ID
        ;

andExpr : idExpr (AND idExpr)* // Is it an && expression
        ;

idExpr  :  LETS              // Is it letters?
          | BT               // Is it 'true'? (logical value)
          | BF              // Or 'false'?    (logical value)
          | LPN boolExpr RPN // Or maybe it's in parentheses! If so loop back up (recurse): boolExpr
          ;                 // I didn't do INTs here, (1 && 3 || 4: doesn't many any sense -> reject?)
                           // Possible evaluation of > or < in future for INTs)

arithExpr: amExpr                          // Let's start with +- (low precedence)
         ;

amExpr   : mdExpr ((PLUS | MINUS) mdExpr)*  // Is expression + or -?
         ;

mdExpr   : idExp ((MULT | DIV) idExp)*   // Is it * or /?
         ;

idExp    : INT                     // Is it a number?
         | FLT                     // Is it a float/double?
         | LETS                   // Is it letters?
         | LPN arithExpr RPN     // Or is it in parentheses? If so, loop back up to arithExpr
         ;



// Keywords
JP : 'JP';
RP : 'RP';
BT    : ('true'  | 'TRUE');
BF    : ('false' | 'FALSE');

// Operators
LTE: '<=';
GTE: '>=';
LT : '<';
GT : '>';
E : '=';
NE : '!=';
AND    : '&&';
OR     : '||';
PLUS   :  '+';
MINUS  :  '-';
DIV    :  '/';
MULT   :  '*';
ARROW : '->';


// Numbers
FLT   : ('0'..'9')+'.'('0'..'9')+;
INT   :  ('0'..'9')+;
// Identifiers
LETS  :  ('A'..'Z' | 'a'..'z')+;

// Punctuation
LPN :'(';
RPN :')';
COLON : ':';
LBR: '{';
RBR: '}';

// Other
COMMENTS: '//' ->channel(HIDDEN);
WS      : [ \n]+ -> channel(HIDDEN); // Added \n to take into account new line
