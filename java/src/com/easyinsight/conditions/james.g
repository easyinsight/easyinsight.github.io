grammar james;

options { output=ast; }

expr	:	term ((Add | Subtract) term)*;
term	:	factor ((Multiply | Divide) factor)*;
factor	:	symbol | OpenParen expr CloseParen;
symbol	:	Integer | Variable | function;
function:	Variable OpenParen (expr (Comma expr)*)? CloseParen;

/* LEXR */

fragment Digit
	:	'0'..'9';

fragment UInteger
	:	Digit+;
Integer :	(Add | Subtract)? UInteger;

OpenParen
	:	'(';
CloseParen
	:	')';

Add	:	'+';
Subtract:	'-';
Multiply:	'*';
Divide	:	'/';
Comma	:	',';

fragment Whitespace
	: ( '\t' | ' ' | '\r' | '\n'| '\u000C' );
		
fragment LowerCase
	:	'a'..'z';

fragment UpperCase
	:	'A'..'Z';
	
fragment Character
	:	LowerCase | UpperCase;
	
fragment SpecialChar
	:	'_';

Variable:	SpacedVariableName;

fragment SpacedVariableName
	:	Character (Character | Digit | SpecialChar | Whitespace)*;

HideWhiteSpace
	:	Whitespace* {  $channel = HIDDEN; };
