grammar Calculations;

options { output=AST; }

// Single-character tokens
tokens
{
	FuncEval;
	OpenParen = '(';
	CloseParen = ')';
	Add = '+';
	Subtract= '-';
	Multiply = '*';
	Divide = '/';
	Comma = ',';
	Dot = '.';
	Exp = '^';
	OpenBrace = '[';
	CloseBrace = ']';
}

@header { package com.easyinsight.calculations.generated; }
@lexer::header { package com.easyinsight.calculations.generated; }

startExpr
	:	expr EOF!;

expr	:	term ((Add^ | Subtract^) term)*;

term	:	unaryOperator ((Multiply^ | Divide^) unaryOperator)*;

unaryOperator
	:	(Add^ | Subtract^)? exponent;
exponent:	factor (Exp^ unaryOperator)?;

factor	:	symbol | parenExpr;
// Rule to remove parenthesis out of the AST
parenExpr
	:	OpenParen expr CloseParen -> expr;
symbol	:	Decimal | Variable | function;
function:	Variable OpenParen (expr (Comma expr)*)? CloseParen -> ^(FuncEval Variable (expr (expr)*)?);

/* LEXR */

Decimal	:	UInteger (Dot UInteger)? ('E' Integer)?;

Variable:	BracketedVariable | NoBracketsVariable;

// Last rule to make sure whitespace incorporated in earlier rules is counted.

HideWhiteSpace
	:	Whitespace+ {  $channel = HIDDEN; };

fragment Integer :	(Add | Subtract)? UInteger;
	
fragment Digit
	:	'0'..'9';
fragment UInteger
	:	Digit+;

fragment VariableWhitespace
	: ( '\t' | ' ');
	
fragment Whitespace
	: (VariableWhitespace | '\r' | '\n'| '\u000C' );
		
fragment LowerCase
	:	'a'..'z';
fragment UpperCase
	:	'A'..'Z';
	
fragment Character
	:	LowerCase | UpperCase;
	
fragment BracketedVariable
	:	OpenBrace (Character | Digit) (Character | Digit | SpecialChars | VariableWhitespace)* CloseBrace;

fragment NoBracketsVariable
	:	(Character | Digit) (Character | Digit | VariableWhitespace)*;
	
// List any special characters that should be part of variable names here.
fragment NoBracketSpecialChars
	:	'_';
	
fragment SpecialChars
	:	NoBracketSpecialChars | ':' | '<' | '>' | ',' | '.' | ';' | '/' | '?' | '"' | '\'' | '{' | '}' | '-' | '=' | '+' | '(' | ')' | '!' | '@' | '#' | '$' | '%' | '^' | '&' | '*' | '~' | '`' | '|' | '\\';
	
