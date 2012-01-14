// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g 2012-01-13 21:50:10
 package com.easyinsight.calculations.generated; 

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class CalculationsLexer extends Lexer {
    public static final int OpenBrace=14;
    public static final int CloseBrace=15;
    public static final int CloseParen=6;
    public static final int HideWhiteSpace=29;
    public static final int FuncEval=4;
    public static final int Subtract=8;
    public static final int Multiply=9;
    public static final int Exp=13;
    public static final int Decimal=18;
    public static final int Quote=16;
    public static final int BracketedVariable=22;
    public static final int Digit=25;
    public static final int EOF=-1;
    public static final int Add=7;
    public static final int Divide=10;
    public static final int VariableWhitespace=26;
    public static final int Variable=17;
    public static final int OpenParen=5;
    public static final int UpperCase=31;
    public static final int Character=24;
    public static final int Dot=12;
    public static final int LowerCase=30;
    public static final int String=19;
    public static final int NoBracketsVariable=23;
    public static final int UInteger=20;
    public static final int Whitespace=28;
    public static final int Comma=11;
    public static final int VariableSpecialChars=33;
    public static final int SpecialChars=27;
    public static final int Integer=21;
    public static final int InternationalCharacters=32;
    public static final int NoBracketSpecialChars=34;

    // delegates
    // delegators

    public CalculationsLexer() {;} 
    public CalculationsLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public CalculationsLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g"; }

    // $ANTLR start "OpenParen"
    public final void mOpenParen() throws RecognitionException {
        try {
            int _type = OpenParen;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:5:11: ( '(' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:5:13: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OpenParen"

    // $ANTLR start "CloseParen"
    public final void mCloseParen() throws RecognitionException {
        try {
            int _type = CloseParen;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:6:12: ( ')' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:6:14: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CloseParen"

    // $ANTLR start "Add"
    public final void mAdd() throws RecognitionException {
        try {
            int _type = Add;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:7:5: ( '+' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:7:7: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Add"

    // $ANTLR start "Subtract"
    public final void mSubtract() throws RecognitionException {
        try {
            int _type = Subtract;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:8:10: ( '-' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:8:12: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Subtract"

    // $ANTLR start "Multiply"
    public final void mMultiply() throws RecognitionException {
        try {
            int _type = Multiply;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:9:10: ( '*' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:9:12: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Multiply"

    // $ANTLR start "Divide"
    public final void mDivide() throws RecognitionException {
        try {
            int _type = Divide;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:10:8: ( '/' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:10:10: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Divide"

    // $ANTLR start "Comma"
    public final void mComma() throws RecognitionException {
        try {
            int _type = Comma;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:11:7: ( ',' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:11:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Comma"

    // $ANTLR start "Dot"
    public final void mDot() throws RecognitionException {
        try {
            int _type = Dot;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:12:5: ( '.' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:12:7: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Dot"

    // $ANTLR start "Exp"
    public final void mExp() throws RecognitionException {
        try {
            int _type = Exp;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:13:5: ( '^' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:13:7: '^'
            {
            match('^'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Exp"

    // $ANTLR start "OpenBrace"
    public final void mOpenBrace() throws RecognitionException {
        try {
            int _type = OpenBrace;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:14:11: ( '[' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:14:13: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OpenBrace"

    // $ANTLR start "CloseBrace"
    public final void mCloseBrace() throws RecognitionException {
        try {
            int _type = CloseBrace;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:15:12: ( ']' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:15:14: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CloseBrace"

    // $ANTLR start "Quote"
    public final void mQuote() throws RecognitionException {
        try {
            int _type = Quote;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:16:7: ( '\"' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:16:9: '\"'
            {
            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Quote"

    // $ANTLR start "Decimal"
    public final void mDecimal() throws RecognitionException {
        try {
            int _type = Decimal;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:49:9: ( ( ( UInteger ( Dot UInteger )? ) | ( Dot UInteger ) ) ( 'E' Integer )? )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:49:11: ( ( UInteger ( Dot UInteger )? ) | ( Dot UInteger ) ) ( 'E' Integer )?
            {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:49:11: ( ( UInteger ( Dot UInteger )? ) | ( Dot UInteger ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( ((LA2_0>='0' && LA2_0<='9')) ) {
                alt2=1;
            }
            else if ( (LA2_0=='.') ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:49:12: ( UInteger ( Dot UInteger )? )
                    {
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:49:12: ( UInteger ( Dot UInteger )? )
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:49:13: UInteger ( Dot UInteger )?
                    {
                    mUInteger(); 
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:49:22: ( Dot UInteger )?
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0=='.') ) {
                        alt1=1;
                    }
                    switch (alt1) {
                        case 1 :
                            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:49:23: Dot UInteger
                            {
                            mDot(); 
                            mUInteger(); 

                            }
                            break;

                    }


                    }


                    }
                    break;
                case 2 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:49:41: ( Dot UInteger )
                    {
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:49:41: ( Dot UInteger )
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:49:42: Dot UInteger
                    {
                    mDot(); 
                    mUInteger(); 

                    }


                    }
                    break;

            }

            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:49:57: ( 'E' Integer )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='E') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:49:58: 'E' Integer
                    {
                    match('E'); 
                    mInteger(); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Decimal"

    // $ANTLR start "Variable"
    public final void mVariable() throws RecognitionException {
        try {
            int _type = Variable;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:52:9: ( BracketedVariable | NoBracketsVariable )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='[') ) {
                alt4=1;
            }
            else if ( ((LA4_0>='0' && LA4_0<='9')||(LA4_0>='A' && LA4_0<='Z')||(LA4_0>='a' && LA4_0<='z')||(LA4_0>='\u0100' && LA4_0<='\uFFFE')) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:52:11: BracketedVariable
                    {
                    mBracketedVariable(); 

                    }
                    break;
                case 2 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:52:31: NoBracketsVariable
                    {
                    mNoBracketsVariable(); 

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Variable"

    // $ANTLR start "String"
    public final void mString() throws RecognitionException {
        try {
            int _type = String;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:54:8: ( '\\\"' ( Character | Digit | VariableWhitespace | SpecialChars )* '\\\"' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:54:10: '\\\"' ( Character | Digit | VariableWhitespace | SpecialChars )* '\\\"'
            {
            match('\"'); 
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:54:15: ( Character | Digit | VariableWhitespace | SpecialChars )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0=='\t'||(LA5_0>=' ' && LA5_0<='!')||(LA5_0>='#' && LA5_0<='Z')||LA5_0=='\\'||(LA5_0>='^' && LA5_0<='~')||(LA5_0>='\u0100' && LA5_0<='\uFFFE')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            	    {
            	    if ( input.LA(1)=='\t'||(input.LA(1)>=' ' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='Z')||input.LA(1)=='\\'||(input.LA(1)>='^' && input.LA(1)<='~')||(input.LA(1)>='\u0100' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "String"

    // $ANTLR start "HideWhiteSpace"
    public final void mHideWhiteSpace() throws RecognitionException {
        try {
            int _type = HideWhiteSpace;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:60:2: ( ( Whitespace )+ )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:60:4: ( Whitespace )+
            {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:60:4: ( Whitespace )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='\t' && LA6_0<='\n')||(LA6_0>='\f' && LA6_0<='\r')||LA6_0==' ') ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:60:4: Whitespace
            	    {
            	    mWhitespace(); 

            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);

              _channel = HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HideWhiteSpace"

    // $ANTLR start "Integer"
    public final void mInteger() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:62:18: ( ( Add | Subtract )? UInteger )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:62:20: ( Add | Subtract )? UInteger
            {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:62:20: ( Add | Subtract )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='+'||LA7_0=='-') ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


                    }
                    break;

            }

            mUInteger(); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "Integer"

    // $ANTLR start "Digit"
    public final void mDigit() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:65:2: ( '0' .. '9' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:65:4: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "Digit"

    // $ANTLR start "UInteger"
    public final void mUInteger() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:67:2: ( ( Digit )+ )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:67:4: ( Digit )+
            {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:67:4: ( Digit )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>='0' && LA8_0<='9')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:67:4: Digit
            	    {
            	    mDigit(); 

            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end "UInteger"

    // $ANTLR start "VariableWhitespace"
    public final void mVariableWhitespace() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:70:2: ( ( '\\t' | ' ' ) )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:70:4: ( '\\t' | ' ' )
            {
            if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "VariableWhitespace"

    // $ANTLR start "Whitespace"
    public final void mWhitespace() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:73:2: ( ( VariableWhitespace | '\\r' | '\\n' | '\\u000C' ) )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:73:4: ( VariableWhitespace | '\\r' | '\\n' | '\\u000C' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "Whitespace"

    // $ANTLR start "LowerCase"
    public final void mLowerCase() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:76:2: ( 'a' .. 'z' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:76:4: 'a' .. 'z'
            {
            matchRange('a','z'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "LowerCase"

    // $ANTLR start "UpperCase"
    public final void mUpperCase() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:78:2: ( 'A' .. 'Z' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:78:4: 'A' .. 'Z'
            {
            matchRange('A','Z'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "UpperCase"

    // $ANTLR start "InternationalCharacters"
    public final void mInternationalCharacters() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:81:2: ( '\\u0100' .. '\\uFFFE' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:81:4: '\\u0100' .. '\\uFFFE'
            {
            matchRange('\u0100','\uFFFE'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "InternationalCharacters"

    // $ANTLR start "Character"
    public final void mCharacter() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:84:2: ( LowerCase | UpperCase | InternationalCharacters )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u0100' && input.LA(1)<='\uFFFE') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "Character"

    // $ANTLR start "BracketedVariable"
    public final void mBracketedVariable() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:87:2: ( OpenBrace ( Character | Digit | VariableSpecialChars ) ( Character | Digit | VariableSpecialChars | VariableWhitespace )* CloseBrace )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:87:4: OpenBrace ( Character | Digit | VariableSpecialChars ) ( Character | Digit | VariableSpecialChars | VariableWhitespace )* CloseBrace
            {
            mOpenBrace(); 
            if ( (input.LA(1)>='!' && input.LA(1)<='Z')||input.LA(1)=='\\'||(input.LA(1)>='^' && input.LA(1)<='~')||(input.LA(1)>='\u0100' && input.LA(1)<='\uFFFE') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:87:57: ( Character | Digit | VariableSpecialChars | VariableWhitespace )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0=='\t'||(LA9_0>=' ' && LA9_0<='Z')||LA9_0=='\\'||(LA9_0>='^' && LA9_0<='~')||(LA9_0>='\u0100' && LA9_0<='\uFFFE')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            	    {
            	    if ( input.LA(1)=='\t'||(input.LA(1)>=' ' && input.LA(1)<='Z')||input.LA(1)=='\\'||(input.LA(1)>='^' && input.LA(1)<='~')||(input.LA(1)>='\u0100' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            mCloseBrace(); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "BracketedVariable"

    // $ANTLR start "NoBracketsVariable"
    public final void mNoBracketsVariable() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:90:2: ( ( Character | Digit ) ( Character | Digit | VariableWhitespace )* )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:90:4: ( Character | Digit ) ( Character | Digit | VariableWhitespace )*
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u0100' && input.LA(1)<='\uFFFE') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:90:24: ( Character | Digit | VariableWhitespace )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0=='\t'||LA10_0==' '||(LA10_0>='0' && LA10_0<='9')||(LA10_0>='A' && LA10_0<='Z')||(LA10_0>='a' && LA10_0<='z')||(LA10_0>='\u0100' && LA10_0<='\uFFFE')) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' '||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u0100' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end "NoBracketsVariable"

    // $ANTLR start "NoBracketSpecialChars"
    public final void mNoBracketSpecialChars() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:94:2: ( '_' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:94:4: '_'
            {
            match('_'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "NoBracketSpecialChars"

    // $ANTLR start "VariableSpecialChars"
    public final void mVariableSpecialChars() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:97:2: ( '\\\"' | SpecialChars )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            {
            if ( (input.LA(1)>='!' && input.LA(1)<='/')||(input.LA(1)>=':' && input.LA(1)<='@')||input.LA(1)=='\\'||(input.LA(1)>='^' && input.LA(1)<='`')||(input.LA(1)>='{' && input.LA(1)<='~') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "VariableSpecialChars"

    // $ANTLR start "SpecialChars"
    public final void mSpecialChars() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:100:2: ( NoBracketSpecialChars | ':' | '<' | '>' | ',' | '.' | ';' | '/' | '?' | '\\'' | '-' | '=' | '+' | '(' | ')' | '!' | '@' | '#' | '$' | '%' | '^' | '&' | '*' | '~' | '`' | '|' | '\\\\' | '{' | '}' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            {
            if ( input.LA(1)=='!'||(input.LA(1)>='#' && input.LA(1)<='/')||(input.LA(1)>=':' && input.LA(1)<='@')||input.LA(1)=='\\'||(input.LA(1)>='^' && input.LA(1)<='`')||(input.LA(1)>='{' && input.LA(1)<='~') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "SpecialChars"

    public void mTokens() throws RecognitionException {
        // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:8: ( OpenParen | CloseParen | Add | Subtract | Multiply | Divide | Comma | Dot | Exp | OpenBrace | CloseBrace | Quote | Decimal | Variable | String | HideWhiteSpace )
        int alt11=16;
        alt11 = dfa11.predict(input);
        switch (alt11) {
            case 1 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:10: OpenParen
                {
                mOpenParen(); 

                }
                break;
            case 2 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:20: CloseParen
                {
                mCloseParen(); 

                }
                break;
            case 3 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:31: Add
                {
                mAdd(); 

                }
                break;
            case 4 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:35: Subtract
                {
                mSubtract(); 

                }
                break;
            case 5 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:44: Multiply
                {
                mMultiply(); 

                }
                break;
            case 6 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:53: Divide
                {
                mDivide(); 

                }
                break;
            case 7 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:60: Comma
                {
                mComma(); 

                }
                break;
            case 8 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:66: Dot
                {
                mDot(); 

                }
                break;
            case 9 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:70: Exp
                {
                mExp(); 

                }
                break;
            case 10 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:74: OpenBrace
                {
                mOpenBrace(); 

                }
                break;
            case 11 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:84: CloseBrace
                {
                mCloseBrace(); 

                }
                break;
            case 12 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:95: Quote
                {
                mQuote(); 

                }
                break;
            case 13 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:101: Decimal
                {
                mDecimal(); 

                }
                break;
            case 14 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:109: Variable
                {
                mVariable(); 

                }
                break;
            case 15 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:118: String
                {
                mString(); 

                }
                break;
            case 16 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:125: HideWhiteSpace
                {
                mHideWhiteSpace(); 

                }
                break;

        }

    }


    protected DFA11 dfa11 = new DFA11(this);
    static final String DFA11_eotS =
        "\10\uffff\1\20\1\uffff\1\22\1\uffff\1\24\1\21\7\uffff\1\16\2\21";
    static final String DFA11_eofS =
        "\30\uffff";
    static final String DFA11_minS =
        "\1\11\7\uffff\1\60\1\uffff\1\41\1\uffff\2\11\7\uffff\1\53\2\11";
    static final String DFA11_maxS =
        "\1\ufffe\7\uffff\1\71\1\uffff\1\ufffe\1\uffff\2\ufffe\7\uffff\1"+
        "\71\2\ufffe";
    static final String DFA11_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\uffff\1\11\1\uffff\1\13\2"+
        "\uffff\1\16\1\20\1\10\1\15\1\12\1\17\1\14\3\uffff";
    static final String DFA11_specialS =
        "\30\uffff}>";
    static final String[] DFA11_transitionS = {
            "\2\17\1\uffff\2\17\22\uffff\1\17\1\uffff\1\14\5\uffff\1\1\1"+
            "\2\1\5\1\3\1\7\1\4\1\10\1\6\12\15\7\uffff\32\16\1\12\1\uffff"+
            "\1\13\1\11\2\uffff\32\16\u0085\uffff\ufeff\16",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\12\21",
            "",
            "\72\16\1\uffff\1\16\1\uffff\41\16\u0081\uffff\ufeff\16",
            "",
            "\1\23\26\uffff\73\23\1\uffff\1\23\1\uffff\41\23\u0081\uffff"+
            "\ufeff\23",
            "\1\16\26\uffff\1\16\17\uffff\12\26\7\uffff\4\16\1\25\25\16"+
            "\6\uffff\32\16\u0085\uffff\ufeff\16",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\21\1\uffff\1\21\2\uffff\12\27",
            "\1\16\26\uffff\1\16\17\uffff\12\26\7\uffff\4\16\1\25\25\16"+
            "\6\uffff\32\16\u0085\uffff\ufeff\16",
            "\1\16\26\uffff\1\16\17\uffff\12\27\7\uffff\32\16\6\uffff\32"+
            "\16\u0085\uffff\ufeff\16"
    };

    static final short[] DFA11_eot = DFA.unpackEncodedString(DFA11_eotS);
    static final short[] DFA11_eof = DFA.unpackEncodedString(DFA11_eofS);
    static final char[] DFA11_min = DFA.unpackEncodedStringToUnsignedChars(DFA11_minS);
    static final char[] DFA11_max = DFA.unpackEncodedStringToUnsignedChars(DFA11_maxS);
    static final short[] DFA11_accept = DFA.unpackEncodedString(DFA11_acceptS);
    static final short[] DFA11_special = DFA.unpackEncodedString(DFA11_specialS);
    static final short[][] DFA11_transition;

    static {
        int numStates = DFA11_transitionS.length;
        DFA11_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA11_transition[i] = DFA.unpackEncodedString(DFA11_transitionS[i]);
        }
    }

    class DFA11 extends DFA {

        public DFA11(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 11;
            this.eot = DFA11_eot;
            this.eof = DFA11_eof;
            this.min = DFA11_min;
            this.max = DFA11_max;
            this.accept = DFA11_accept;
            this.special = DFA11_special;
            this.transition = DFA11_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( OpenParen | CloseParen | Add | Subtract | Multiply | Divide | Comma | Dot | Exp | OpenBrace | CloseBrace | Quote | Decimal | Variable | String | HideWhiteSpace );";
        }
    }
 

}