grammar Condition;
options { output=AST; }
tokens {
	PLUS 	= '+' ;
	MINUS	= '-' ;
	MULT	= '*' ;
	DIV	= '/' ;
}

/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

functioncall	:	(SYMBOL OPENCALL (parameter)* CLOSECALL);

parameters 	:	'(' ;

expr	: term ( ( PLUS | MINUS )  term )* ;

term	: factor ( ( MULT | DIV ) factor )* ;

factor	: (NUMBER | SYMBOL) ;

parameter	:	(expr | SYMBOL);

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

NUMBER	: (DIGIT)+ ;

WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ 	{ $channel = HIDDEN; } ;

fragment DIGIT	: '0'..'9' ;

SYMBOL  :   ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9')+ ;

OPENCALL	:	('(');

CLOSECALL	:	(')');

PARAMDELIM	:	(',');
