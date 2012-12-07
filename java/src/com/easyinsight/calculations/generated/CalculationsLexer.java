// $ANTLR 3.4 /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g 2012-12-07 14:22:43
 package com.easyinsight.calculations.generated; 

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class CalculationsLexer extends Lexer {
    public static final int EOF=-1;
    public static final int Add=4;
    public static final int And=5;
    public static final int BracketedVariable=6;
    public static final int Character=7;
    public static final int CloseBrace=8;
    public static final int CloseParen=9;
    public static final int Comma=10;
    public static final int Decimal=11;
    public static final int Digit=12;
    public static final int Divide=13;
    public static final int Dot=14;
    public static final int Equals=15;
    public static final int Exp=16;
    public static final int FuncEval=17;
    public static final int GreaterThan=18;
    public static final int GreaterThanEqualTo=19;
    public static final int HideWhiteSpace=20;
    public static final int Integer=21;
    public static final int InternationalCharacters=22;
    public static final int LessThan=23;
    public static final int LessThanEqualTo=24;
    public static final int LowerCase=25;
    public static final int Multiply=26;
    public static final int NoBracketSpecialChars=27;
    public static final int NoBracketsVariable=28;
    public static final int Not=29;
    public static final int NotEquals=30;
    public static final int OpenBrace=31;
    public static final int OpenParen=32;
    public static final int Or=33;
    public static final int Quote=34;
    public static final int SpecialChars=35;
    public static final int String=36;
    public static final int Subtract=37;
    public static final int UInteger=38;
    public static final int UpperCase=39;
    public static final int Variable=40;
    public static final int VariableSpecialChars=41;
    public static final int VariableWhitespace=42;
    public static final int Whitespace=43;

    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public CalculationsLexer() {} 
    public CalculationsLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public CalculationsLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "/Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g"; }

    // $ANTLR start "Add"
    public final void mAdd() throws RecognitionException {
        try {
            int _type = Add;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:4:5: ( '+' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:4:7: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Add"

    // $ANTLR start "And"
    public final void mAnd() throws RecognitionException {
        try {
            int _type = And;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:5:5: ( '&&' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:5:7: '&&'
            {
            match("&&"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "And"

    // $ANTLR start "CloseBrace"
    public final void mCloseBrace() throws RecognitionException {
        try {
            int _type = CloseBrace;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:6:12: ( ']' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:6:14: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CloseBrace"

    // $ANTLR start "CloseParen"
    public final void mCloseParen() throws RecognitionException {
        try {
            int _type = CloseParen;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:7:12: ( ')' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:7:14: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CloseParen"

    // $ANTLR start "Comma"
    public final void mComma() throws RecognitionException {
        try {
            int _type = Comma;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:8:7: ( ',' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:8:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Comma"

    // $ANTLR start "Divide"
    public final void mDivide() throws RecognitionException {
        try {
            int _type = Divide;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:9:8: ( '/' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:9:10: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Divide"

    // $ANTLR start "Dot"
    public final void mDot() throws RecognitionException {
        try {
            int _type = Dot;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:10:5: ( '.' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:10:7: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Dot"

    // $ANTLR start "Equals"
    public final void mEquals() throws RecognitionException {
        try {
            int _type = Equals;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:11:8: ( '==' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:11:10: '=='
            {
            match("=="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Equals"

    // $ANTLR start "Exp"
    public final void mExp() throws RecognitionException {
        try {
            int _type = Exp;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:12:5: ( '^' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:12:7: '^'
            {
            match('^'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Exp"

    // $ANTLR start "GreaterThan"
    public final void mGreaterThan() throws RecognitionException {
        try {
            int _type = GreaterThan;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:13:13: ( '>' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:13:15: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "GreaterThan"

    // $ANTLR start "GreaterThanEqualTo"
    public final void mGreaterThanEqualTo() throws RecognitionException {
        try {
            int _type = GreaterThanEqualTo;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:14:20: ( '>=' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:14:22: '>='
            {
            match(">="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "GreaterThanEqualTo"

    // $ANTLR start "LessThan"
    public final void mLessThan() throws RecognitionException {
        try {
            int _type = LessThan;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:15:10: ( '<' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:15:12: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LessThan"

    // $ANTLR start "LessThanEqualTo"
    public final void mLessThanEqualTo() throws RecognitionException {
        try {
            int _type = LessThanEqualTo;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:16:17: ( '<=' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:16:19: '<='
            {
            match("<="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LessThanEqualTo"

    // $ANTLR start "Multiply"
    public final void mMultiply() throws RecognitionException {
        try {
            int _type = Multiply;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:17:10: ( '*' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:17:12: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Multiply"

    // $ANTLR start "Not"
    public final void mNot() throws RecognitionException {
        try {
            int _type = Not;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:18:5: ( '!' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:18:7: '!'
            {
            match('!'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Not"

    // $ANTLR start "NotEquals"
    public final void mNotEquals() throws RecognitionException {
        try {
            int _type = NotEquals;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:19:11: ( '!=' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:19:13: '!='
            {
            match("!="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NotEquals"

    // $ANTLR start "OpenBrace"
    public final void mOpenBrace() throws RecognitionException {
        try {
            int _type = OpenBrace;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:20:11: ( '[' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:20:13: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "OpenBrace"

    // $ANTLR start "OpenParen"
    public final void mOpenParen() throws RecognitionException {
        try {
            int _type = OpenParen;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:21:11: ( '(' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:21:13: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "OpenParen"

    // $ANTLR start "Or"
    public final void mOr() throws RecognitionException {
        try {
            int _type = Or;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:22:4: ( '||' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:22:6: '||'
            {
            match("||"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Or"

    // $ANTLR start "Quote"
    public final void mQuote() throws RecognitionException {
        try {
            int _type = Quote;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:23:7: ( '\"' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:23:9: '\"'
            {
            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Quote"

    // $ANTLR start "Subtract"
    public final void mSubtract() throws RecognitionException {
        try {
            int _type = Subtract;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:24:10: ( '-' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:24:12: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Subtract"

    // $ANTLR start "Decimal"
    public final void mDecimal() throws RecognitionException {
        try {
            int _type = Decimal;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:64:9: ( ( ( UInteger ( Dot UInteger )? ) | ( Dot UInteger ) ) ( 'E' Integer )? )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:64:11: ( ( UInteger ( Dot UInteger )? ) | ( Dot UInteger ) ) ( 'E' Integer )?
            {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:64:11: ( ( UInteger ( Dot UInteger )? ) | ( Dot UInteger ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
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
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:64:12: ( UInteger ( Dot UInteger )? )
                    {
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:64:12: ( UInteger ( Dot UInteger )? )
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:64:13: UInteger ( Dot UInteger )?
                    {
                    mUInteger(); 


                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:64:22: ( Dot UInteger )?
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0=='.') ) {
                        alt1=1;
                    }
                    switch (alt1) {
                        case 1 :
                            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:64:23: Dot UInteger
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
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:64:41: ( Dot UInteger )
                    {
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:64:41: ( Dot UInteger )
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:64:42: Dot UInteger
                    {
                    mDot(); 


                    mUInteger(); 


                    }


                    }
                    break;

            }


            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:64:57: ( 'E' Integer )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='E') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:64:58: 'E' Integer
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
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Decimal"

    // $ANTLR start "Variable"
    public final void mVariable() throws RecognitionException {
        try {
            int _type = Variable;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:66:9: ( BracketedVariable | NoBracketsVariable )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='[') ) {
                alt4=1;
            }
            else if ( ((LA4_0 >= 'A' && LA4_0 <= 'Z')||(LA4_0 >= 'a' && LA4_0 <= 'z')||(LA4_0 >= '\u0100' && LA4_0 <= '\uFFFE')) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;

            }
            switch (alt4) {
                case 1 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:66:11: BracketedVariable
                    {
                    mBracketedVariable(); 


                    }
                    break;
                case 2 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:66:31: NoBracketsVariable
                    {
                    mNoBracketsVariable(); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Variable"

    // $ANTLR start "String"
    public final void mString() throws RecognitionException {
        try {
            int _type = String;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:68:8: ( '\\\"' ( Character | Digit | VariableWhitespace | SpecialChars | ']' | '[' )* '\\\"' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:68:10: '\\\"' ( Character | Digit | VariableWhitespace | SpecialChars | ']' | '[' )* '\\\"'
            {
            match('\"'); 

            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:68:15: ( Character | Digit | VariableWhitespace | SpecialChars | ']' | '[' )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0=='\t'||(LA5_0 >= ' ' && LA5_0 <= '!')||(LA5_0 >= '#' && LA5_0 <= '~')||(LA5_0 >= '\u0100' && LA5_0 <= '\uFFFE')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            	    {
            	    if ( input.LA(1)=='\t'||(input.LA(1) >= ' ' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '~')||(input.LA(1) >= '\u0100' && input.LA(1) <= '\uFFFE') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


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
        	// do for sure before leaving
        }
    }
    // $ANTLR end "String"

    // $ANTLR start "HideWhiteSpace"
    public final void mHideWhiteSpace() throws RecognitionException {
        try {
            int _type = HideWhiteSpace;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:73:2: ( ( Whitespace )+ )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:73:4: ( Whitespace )+
            {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:73:4: ( Whitespace )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0 >= '\t' && LA6_0 <= '\n')||(LA6_0 >= '\f' && LA6_0 <= '\r')||LA6_0==' ') ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            	    {
            	    if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


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
        	// do for sure before leaving
        }
    }
    // $ANTLR end "HideWhiteSpace"

    // $ANTLR start "Integer"
    public final void mInteger() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:75:18: ( ( Add | Subtract )? UInteger )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:75:20: ( Add | Subtract )? UInteger
            {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:75:20: ( Add | Subtract )?
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
                        throw mse;
                    }


                    }
                    break;

            }


            mUInteger(); 


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Integer"

    // $ANTLR start "Digit"
    public final void mDigit() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:78:2: ( '0' .. '9' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Digit"

    // $ANTLR start "UInteger"
    public final void mUInteger() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:80:2: ( ( Digit )+ )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:80:4: ( Digit )+
            {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:80:4: ( Digit )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0 >= '0' && LA8_0 <= '9')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


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
        	// do for sure before leaving
        }
    }
    // $ANTLR end "UInteger"

    // $ANTLR start "VariableWhitespace"
    public final void mVariableWhitespace() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:83:2: ( ( '\\t' | ' ' ) )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            {
            if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "VariableWhitespace"

    // $ANTLR start "Whitespace"
    public final void mWhitespace() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:86:2: ( ( VariableWhitespace | '\\r' | '\\n' | '\\u000C' ) )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            {
            if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Whitespace"

    // $ANTLR start "LowerCase"
    public final void mLowerCase() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:89:2: ( 'a' .. 'z' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            {
            if ( (input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LowerCase"

    // $ANTLR start "UpperCase"
    public final void mUpperCase() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:91:2: ( 'A' .. 'Z' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "UpperCase"

    // $ANTLR start "InternationalCharacters"
    public final void mInternationalCharacters() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:94:2: ( '\\u0100' .. '\\uFFFE' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            {
            if ( (input.LA(1) >= '\u0100' && input.LA(1) <= '\uFFFE') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "InternationalCharacters"

    // $ANTLR start "Character"
    public final void mCharacter() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:97:2: ( LowerCase | UpperCase | InternationalCharacters )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z')||(input.LA(1) >= '\u0100' && input.LA(1) <= '\uFFFE') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Character"

    // $ANTLR start "BracketedVariable"
    public final void mBracketedVariable() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:100:2: ( OpenBrace ( Character | Digit | VariableSpecialChars ) ( Character | Digit | VariableSpecialChars | VariableWhitespace )* CloseBrace )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:100:4: OpenBrace ( Character | Digit | VariableSpecialChars ) ( Character | Digit | VariableSpecialChars | VariableWhitespace )* CloseBrace
            {
            mOpenBrace(); 


            if ( (input.LA(1) >= '!' && input.LA(1) <= 'Z')||input.LA(1)=='\\'||(input.LA(1) >= '^' && input.LA(1) <= '~')||(input.LA(1) >= '\u0100' && input.LA(1) <= '\uFFFE') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:100:57: ( Character | Digit | VariableSpecialChars | VariableWhitespace )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0=='\t'||(LA9_0 >= ' ' && LA9_0 <= 'Z')||LA9_0=='\\'||(LA9_0 >= '^' && LA9_0 <= '~')||(LA9_0 >= '\u0100' && LA9_0 <= '\uFFFE')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            	    {
            	    if ( input.LA(1)=='\t'||(input.LA(1) >= ' ' && input.LA(1) <= 'Z')||input.LA(1)=='\\'||(input.LA(1) >= '^' && input.LA(1) <= '~')||(input.LA(1) >= '\u0100' && input.LA(1) <= '\uFFFE') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


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
        	// do for sure before leaving
        }
    }
    // $ANTLR end "BracketedVariable"

    // $ANTLR start "NoBracketsVariable"
    public final void mNoBracketsVariable() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:103:2: ( Character ( Character | Digit | VariableWhitespace )* )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:103:4: Character ( Character | Digit | VariableWhitespace )*
            {
            mCharacter(); 


            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:103:14: ( Character | Digit | VariableWhitespace )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0=='\t'||LA10_0==' '||(LA10_0 >= '0' && LA10_0 <= '9')||(LA10_0 >= 'A' && LA10_0 <= 'Z')||(LA10_0 >= 'a' && LA10_0 <= 'z')||(LA10_0 >= '\u0100' && LA10_0 <= '\uFFFE')) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' '||(input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z')||(input.LA(1) >= '\u0100' && input.LA(1) <= '\uFFFE') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NoBracketsVariable"

    // $ANTLR start "NoBracketSpecialChars"
    public final void mNoBracketSpecialChars() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:107:2: ( '_' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:107:4: '_'
            {
            match('_'); 

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NoBracketSpecialChars"

    // $ANTLR start "VariableSpecialChars"
    public final void mVariableSpecialChars() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:110:2: ( '\\\"' | SpecialChars )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            {
            if ( (input.LA(1) >= '!' && input.LA(1) <= '/')||(input.LA(1) >= ':' && input.LA(1) <= '@')||input.LA(1)=='\\'||(input.LA(1) >= '^' && input.LA(1) <= '`')||(input.LA(1) >= '{' && input.LA(1) <= '~') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "VariableSpecialChars"

    // $ANTLR start "SpecialChars"
    public final void mSpecialChars() throws RecognitionException {
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:113:2: ( NoBracketSpecialChars | ':' | '<' | '>' | ',' | '.' | ';' | '/' | '?' | '\\'' | '-' | '=' | '+' | '(' | ')' | '!' | '@' | '#' | '$' | '%' | '^' | '&' | '*' | '~' | '`' | '|' | '\\\\' | '{' | '}' )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            {
            if ( input.LA(1)=='!'||(input.LA(1) >= '#' && input.LA(1) <= '/')||(input.LA(1) >= ':' && input.LA(1) <= '@')||input.LA(1)=='\\'||(input.LA(1) >= '^' && input.LA(1) <= '`')||(input.LA(1) >= '{' && input.LA(1) <= '~') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SpecialChars"

    public void mTokens() throws RecognitionException {
        // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:8: ( Add | And | CloseBrace | CloseParen | Comma | Divide | Dot | Equals | Exp | GreaterThan | GreaterThanEqualTo | LessThan | LessThanEqualTo | Multiply | Not | NotEquals | OpenBrace | OpenParen | Or | Quote | Subtract | Decimal | Variable | String | HideWhiteSpace )
        int alt11=25;
        int LA11_0 = input.LA(1);

        if ( (LA11_0=='+') ) {
            alt11=1;
        }
        else if ( (LA11_0=='&') ) {
            alt11=2;
        }
        else if ( (LA11_0==']') ) {
            alt11=3;
        }
        else if ( (LA11_0==')') ) {
            alt11=4;
        }
        else if ( (LA11_0==',') ) {
            alt11=5;
        }
        else if ( (LA11_0=='/') ) {
            alt11=6;
        }
        else if ( (LA11_0=='.') ) {
            int LA11_7 = input.LA(2);

            if ( ((LA11_7 >= '0' && LA11_7 <= '9')) ) {
                alt11=22;
            }
            else {
                alt11=7;
            }
        }
        else if ( (LA11_0=='=') ) {
            alt11=8;
        }
        else if ( (LA11_0=='^') ) {
            alt11=9;
        }
        else if ( (LA11_0=='>') ) {
            int LA11_10 = input.LA(2);

            if ( (LA11_10=='=') ) {
                alt11=11;
            }
            else {
                alt11=10;
            }
        }
        else if ( (LA11_0=='<') ) {
            int LA11_11 = input.LA(2);

            if ( (LA11_11=='=') ) {
                alt11=13;
            }
            else {
                alt11=12;
            }
        }
        else if ( (LA11_0=='*') ) {
            alt11=14;
        }
        else if ( (LA11_0=='!') ) {
            int LA11_13 = input.LA(2);

            if ( (LA11_13=='=') ) {
                alt11=16;
            }
            else {
                alt11=15;
            }
        }
        else if ( (LA11_0=='[') ) {
            int LA11_14 = input.LA(2);

            if ( ((LA11_14 >= '!' && LA11_14 <= 'Z')||LA11_14=='\\'||(LA11_14 >= '^' && LA11_14 <= '~')||(LA11_14 >= '\u0100' && LA11_14 <= '\uFFFE')) ) {
                alt11=23;
            }
            else {
                alt11=17;
            }
        }
        else if ( (LA11_0=='(') ) {
            alt11=18;
        }
        else if ( (LA11_0=='|') ) {
            alt11=19;
        }
        else if ( (LA11_0=='\"') ) {
            int LA11_17 = input.LA(2);

            if ( (LA11_17=='\t'||(LA11_17 >= ' ' && LA11_17 <= '~')||(LA11_17 >= '\u0100' && LA11_17 <= '\uFFFE')) ) {
                alt11=24;
            }
            else {
                alt11=20;
            }
        }
        else if ( (LA11_0=='-') ) {
            alt11=21;
        }
        else if ( ((LA11_0 >= '0' && LA11_0 <= '9')) ) {
            alt11=22;
        }
        else if ( ((LA11_0 >= 'A' && LA11_0 <= 'Z')||(LA11_0 >= 'a' && LA11_0 <= 'z')||(LA11_0 >= '\u0100' && LA11_0 <= '\uFFFE')) ) {
            alt11=23;
        }
        else if ( ((LA11_0 >= '\t' && LA11_0 <= '\n')||(LA11_0 >= '\f' && LA11_0 <= '\r')||LA11_0==' ') ) {
            alt11=25;
        }
        else {
            NoViableAltException nvae =
                new NoViableAltException("", 11, 0, input);

            throw nvae;

        }
        switch (alt11) {
            case 1 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:10: Add
                {
                mAdd(); 


                }
                break;
            case 2 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:14: And
                {
                mAnd(); 


                }
                break;
            case 3 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:18: CloseBrace
                {
                mCloseBrace(); 


                }
                break;
            case 4 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:29: CloseParen
                {
                mCloseParen(); 


                }
                break;
            case 5 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:40: Comma
                {
                mComma(); 


                }
                break;
            case 6 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:46: Divide
                {
                mDivide(); 


                }
                break;
            case 7 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:53: Dot
                {
                mDot(); 


                }
                break;
            case 8 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:57: Equals
                {
                mEquals(); 


                }
                break;
            case 9 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:64: Exp
                {
                mExp(); 


                }
                break;
            case 10 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:68: GreaterThan
                {
                mGreaterThan(); 


                }
                break;
            case 11 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:80: GreaterThanEqualTo
                {
                mGreaterThanEqualTo(); 


                }
                break;
            case 12 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:99: LessThan
                {
                mLessThan(); 


                }
                break;
            case 13 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:108: LessThanEqualTo
                {
                mLessThanEqualTo(); 


                }
                break;
            case 14 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:124: Multiply
                {
                mMultiply(); 


                }
                break;
            case 15 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:133: Not
                {
                mNot(); 


                }
                break;
            case 16 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:137: NotEquals
                {
                mNotEquals(); 


                }
                break;
            case 17 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:147: OpenBrace
                {
                mOpenBrace(); 


                }
                break;
            case 18 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:157: OpenParen
                {
                mOpenParen(); 


                }
                break;
            case 19 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:167: Or
                {
                mOr(); 


                }
                break;
            case 20 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:170: Quote
                {
                mQuote(); 


                }
                break;
            case 21 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:176: Subtract
                {
                mSubtract(); 


                }
                break;
            case 22 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:185: Decimal
                {
                mDecimal(); 


                }
                break;
            case 23 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:193: Variable
                {
                mVariable(); 


                }
                break;
            case 24 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:202: String
                {
                mString(); 


                }
                break;
            case 25 :
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:1:209: HideWhiteSpace
                {
                mHideWhiteSpace(); 


                }
                break;

        }

    }


 

}