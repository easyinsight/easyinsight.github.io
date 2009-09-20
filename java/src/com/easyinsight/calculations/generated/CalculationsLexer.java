// $ANTLR 3.1.2 C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g 2009-09-20 11:53:40
 package com.easyinsight.calculations.generated; 

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class CalculationsLexer extends Lexer {
    public static final int OpenBrace=14;
    public static final int CloseBrace=15;
    public static final int CloseParen=6;
    public static final int HideWhiteSpace=23;
    public static final int FuncEval=4;
    public static final int Subtract=8;
    public static final int Multiply=9;
    public static final int Exp=13;
    public static final int Decimal=16;
    public static final int BracketedVariable=20;
    public static final int Digit=24;
    public static final int EOF=-1;
    public static final int Add=7;
    public static final int Divide=10;
    public static final int VariableWhitespace=25;
    public static final int Variable=17;
    public static final int OpenParen=5;
    public static final int UpperCase=27;
    public static final int Character=28;
    public static final int Dot=12;
    public static final int LowerCase=26;
    public static final int NoBracketsVariable=21;
    public static final int UInteger=18;
    public static final int Whitespace=22;
    public static final int Comma=11;
    public static final int SpecialChars=29;
    public static final int Integer=19;
    public static final int NoBracketSpecialChars=30;

    // delegates
    // delegators

    public CalculationsLexer() {;} 
    public CalculationsLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public CalculationsLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g"; }

    // $ANTLR start "OpenParen"
    public final void mOpenParen() throws RecognitionException {
        try {
            int _type = OpenParen;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:5:11: ( '(' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:5:13: '('
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:6:12: ( ')' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:6:14: ')'
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:7:5: ( '+' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:7:7: '+'
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:8:10: ( '-' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:8:12: '-'
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:9:10: ( '*' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:9:12: '*'
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:10:8: ( '/' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:10:10: '/'
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:11:7: ( ',' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:11:9: ','
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:12:5: ( '.' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:12:7: '.'
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:13:5: ( '^' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:13:7: '^'
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:14:11: ( '[' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:14:13: '['
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:15:12: ( ']' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:15:14: ']'
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

    // $ANTLR start "Decimal"
    public final void mDecimal() throws RecognitionException {
        try {
            int _type = Decimal;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:45:9: ( UInteger ( Dot UInteger )? ( 'E' Integer )? )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:45:11: UInteger ( Dot UInteger )? ( 'E' Integer )?
            {
            mUInteger(); 
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:45:20: ( Dot UInteger )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='.') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:45:21: Dot UInteger
                    {
                    mDot(); 
                    mUInteger(); 

                    }
                    break;

            }

            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:45:36: ( 'E' Integer )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='E') ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:45:37: 'E' Integer
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:47:9: ( BracketedVariable | NoBracketsVariable )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='[') ) {
                alt3=1;
            }
            else if ( ((LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||(LA3_0>='a' && LA3_0<='z')) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:47:11: BracketedVariable
                    {
                    mBracketedVariable(); 

                    }
                    break;
                case 2 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:47:31: NoBracketsVariable
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

    // $ANTLR start "HideWhiteSpace"
    public final void mHideWhiteSpace() throws RecognitionException {
        try {
            int _type = HideWhiteSpace;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:52:2: ( ( Whitespace )+ )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:52:4: ( Whitespace )+
            {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:52:4: ( Whitespace )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='\t' && LA4_0<='\n')||(LA4_0>='\f' && LA4_0<='\r')||LA4_0==' ') ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:52:4: Whitespace
            	    {
            	    mWhitespace(); 

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:54:18: ( ( Add | Subtract )? UInteger )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:54:20: ( Add | Subtract )? UInteger
            {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:54:20: ( Add | Subtract )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='+'||LA5_0=='-') ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:57:2: ( '0' .. '9' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:57:4: '0' .. '9'
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:59:2: ( ( Digit )+ )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:59:4: ( Digit )+
            {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:59:4: ( Digit )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='0' && LA6_0<='9')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:59:4: Digit
            	    {
            	    mDigit(); 

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


            }

        }
        finally {
        }
    }
    // $ANTLR end "UInteger"

    // $ANTLR start "VariableWhitespace"
    public final void mVariableWhitespace() throws RecognitionException {
        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:62:2: ( ( '\\t' | ' ' ) )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:62:4: ( '\\t' | ' ' )
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:65:2: ( ( VariableWhitespace | '\\r' | '\\n' | '\\u000C' ) )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:65:4: ( VariableWhitespace | '\\r' | '\\n' | '\\u000C' )
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:68:2: ( 'a' .. 'z' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:68:4: 'a' .. 'z'
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:70:2: ( 'A' .. 'Z' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:70:4: 'A' .. 'Z'
            {
            matchRange('A','Z'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "UpperCase"

    // $ANTLR start "Character"
    public final void mCharacter() throws RecognitionException {
        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:73:2: ( LowerCase | UpperCase )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:76:2: ( OpenBrace ( Character | Digit ) ( Character | Digit | SpecialChars | VariableWhitespace )* CloseBrace )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:76:4: OpenBrace ( Character | Digit ) ( Character | Digit | SpecialChars | VariableWhitespace )* CloseBrace
            {
            mOpenBrace(); 
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:76:34: ( Character | Digit | SpecialChars | VariableWhitespace )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='\t'||(LA7_0>=' ' && LA7_0<='Z')||(LA7_0>='^' && LA7_0<='_')||(LA7_0>='a' && LA7_0<='{')||LA7_0=='}') ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:
            	    {
            	    if ( input.LA(1)=='\t'||(input.LA(1)>=' ' && input.LA(1)<='Z')||(input.LA(1)>='^' && input.LA(1)<='_')||(input.LA(1)>='a' && input.LA(1)<='{')||input.LA(1)=='}' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop7;
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:79:2: ( ( Character | Digit ) ( Character | Digit | VariableWhitespace )* )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:79:4: ( Character | Digit ) ( Character | Digit | VariableWhitespace )*
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:79:24: ( Character | Digit | VariableWhitespace )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0=='\t'||LA8_0==' '||(LA8_0>='0' && LA8_0<='9')||(LA8_0>='A' && LA8_0<='Z')||(LA8_0>='a' && LA8_0<='z')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' '||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop8;
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:83:2: ( '_' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:83:4: '_'
            {
            match('_'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "NoBracketSpecialChars"

    // $ANTLR start "SpecialChars"
    public final void mSpecialChars() throws RecognitionException {
        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:86:2: ( NoBracketSpecialChars | ':' | '<' | '>' | ',' | '.' | ';' | '/' | '?' | '\"' | '\\'' | '{' | '}' | '-' | '=' | '+' | '(' | ')' | '!' | '@' | '#' | '$' | '%' | '^' | '&' | '*' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:
            {
            if ( (input.LA(1)>='!' && input.LA(1)<='/')||(input.LA(1)>=':' && input.LA(1)<='@')||(input.LA(1)>='^' && input.LA(1)<='_')||input.LA(1)=='{'||input.LA(1)=='}' ) {
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
        // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:8: ( OpenParen | CloseParen | Add | Subtract | Multiply | Divide | Comma | Dot | Exp | OpenBrace | CloseBrace | Decimal | Variable | HideWhiteSpace )
        int alt9=14;
        alt9 = dfa9.predict(input);
        switch (alt9) {
            case 1 :
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:10: OpenParen
                {
                mOpenParen(); 

                }
                break;
            case 2 :
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:20: CloseParen
                {
                mCloseParen(); 

                }
                break;
            case 3 :
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:31: Add
                {
                mAdd(); 

                }
                break;
            case 4 :
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:35: Subtract
                {
                mSubtract(); 

                }
                break;
            case 5 :
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:44: Multiply
                {
                mMultiply(); 

                }
                break;
            case 6 :
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:53: Divide
                {
                mDivide(); 

                }
                break;
            case 7 :
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:60: Comma
                {
                mComma(); 

                }
                break;
            case 8 :
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:66: Dot
                {
                mDot(); 

                }
                break;
            case 9 :
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:70: Exp
                {
                mExp(); 

                }
                break;
            case 10 :
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:74: OpenBrace
                {
                mOpenBrace(); 

                }
                break;
            case 11 :
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:84: CloseBrace
                {
                mCloseBrace(); 

                }
                break;
            case 12 :
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:95: Decimal
                {
                mDecimal(); 

                }
                break;
            case 13 :
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:103: Variable
                {
                mVariable(); 

                }
                break;
            case 14 :
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:112: HideWhiteSpace
                {
                mHideWhiteSpace(); 

                }
                break;

        }

    }


    protected DFA9 dfa9 = new DFA9(this);
    static final String DFA9_eotS =
        "\12\uffff\1\17\1\uffff\1\20\4\uffff\1\15\2\20";
    static final String DFA9_eofS =
        "\24\uffff";
    static final String DFA9_minS =
        "\1\11\11\uffff\1\60\1\uffff\1\11\4\uffff\1\53\2\11";
    static final String DFA9_maxS =
        "\1\172\11\uffff\1\172\1\uffff\1\172\4\uffff\1\71\2\172";
    static final String DFA9_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\uffff\1\13\1\uffff"+
        "\1\15\1\16\1\12\1\14\3\uffff";
    static final String DFA9_specialS =
        "\24\uffff}>";
    static final String[] DFA9_transitionS = {
            "\2\16\1\uffff\2\16\22\uffff\1\16\7\uffff\1\1\1\2\1\5\1\3\1"+
            "\7\1\4\1\10\1\6\12\14\7\uffff\32\15\1\12\1\uffff\1\13\1\11\2"+
            "\uffff\32\15",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\12\15\7\uffff\32\15\6\uffff\32\15",
            "",
            "\1\15\26\uffff\1\15\17\uffff\12\22\7\uffff\4\15\1\21\25\15"+
            "\6\uffff\32\15",
            "",
            "",
            "",
            "",
            "\1\20\1\uffff\1\20\2\uffff\12\23",
            "\1\15\26\uffff\1\15\17\uffff\12\22\7\uffff\4\15\1\21\25\15"+
            "\6\uffff\32\15",
            "\1\15\26\uffff\1\15\17\uffff\12\23\7\uffff\32\15\6\uffff\32"+
            "\15"
    };

    static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
    static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
    static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
    static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
    static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
    static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
    static final short[][] DFA9_transition;

    static {
        int numStates = DFA9_transitionS.length;
        DFA9_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
        }
    }

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = DFA9_eot;
            this.eof = DFA9_eof;
            this.min = DFA9_min;
            this.max = DFA9_max;
            this.accept = DFA9_accept;
            this.special = DFA9_special;
            this.transition = DFA9_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( OpenParen | CloseParen | Add | Subtract | Multiply | Divide | Comma | Dot | Exp | OpenBrace | CloseBrace | Decimal | Variable | HideWhiteSpace );";
        }
    }
 

}