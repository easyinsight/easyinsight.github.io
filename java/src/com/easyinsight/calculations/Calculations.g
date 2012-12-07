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
	Quote = '"';
	LessThanEqualTo = '<=';
	GreaterThanEqualTo = '>=';
	Equals = '==';
	NotEquals = '!=';
	GreaterThan = '>';
	LessThan = '<';
	And = '&&';
	Or = '||';
	Not = '!';
}

@header { package com.easyinsight.calculations.generated; }
@lexer::header { package com.easyinsight.calculations.generated; }


expr	:	evaluation ((And^ | Or^) evaluation)*;


startExpr
	:	expr EOF!;

evaluation
	:	sum ((LessThanEqualTo^ | GreaterThanEqualTo^ | Equals^ | NotEquals^ | GreaterThan^ | LessThan^) sum)*;

sum	:	term ((Add^ | Subtract^) term)*;

term	:	unaryOperator ((Multiply^ | Divide^) unaryOperator)*;

unaryOperator
	:	(Add^ | Subtract^ | Not^)? exponent;
exponent:	factor (Exp^ unaryOperator)?;

factor	:	symbol | parenExpr;
// Rule to remove parenthesis out of the AST
parenExpr
	:	OpenParen expr CloseParen -> expr;
symbol	:	literal | Variable | function;
function:	Variable OpenParen (expr (Comma expr)*)? CloseParen -> ^(FuncEval Variable (expr (expr)*)?);

/* LEXR */

literal	:	Decimal | String;

Decimal	:	((UInteger (Dot UInteger)?) | (Dot UInteger)) ('E' Integer)?;

Variable:	BracketedVariable | NoBracketsVariable;

String	:	'\"' (Character | Digit | VariableWhitespace | SpecialChars | ']' | '[')* '\"';

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
	
fragment InternationalCharacters
	:	'\u0100'..'\uFFFE';
	
fragment Character
	:	LowerCase | UpperCase | InternationalCharacters;
	
fragment BracketedVariable
	:	OpenBrace (Character | Digit | VariableSpecialChars) (Character | Digit | VariableSpecialChars | VariableWhitespace)* CloseBrace;

fragment NoBracketsVariable
	:	Character (Character | Digit | VariableWhitespace)*;
	
// List any special characters that should be part of variable names here.
fragment NoBracketSpecialChars
	:	'_';

fragment VariableSpecialChars
	:	'\"' | SpecialChars;	
	
fragment SpecialChars
	:	NoBracketSpecialChars | ':' | '<' | '>' | ',' | '.' | ';' | '/' | '?' | '\'' | '-' | '=' | '+' | '(' | ')' | '!' | '@' | '#' | '$' | '%' | '^' | '&' | '*' | '~' | '`' | '|' | '\\' | '{' | '}';
	
