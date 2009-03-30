// $ANTLR 3.1.2 C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g 2009-03-30 17:46:54
 package com.easyinsight.calculations.generated; 

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class CalculationsLexer extends Lexer {
    public static final int CloseParen=6;
    public static final int FuncEval=4;
    public static final int HideWhiteSpace=23;
    public static final int Subtract=8;
    public static final int Multiply=9;
    public static final int Exp=13;
    public static final int Decimal=14;
    public static final int Digit=19;
    public static final int EOF=-1;
    public static final int Add=7;
    public static final int Divide=10;
    public static final int VariableWhitespace=21;
    public static final int Variable=15;
    public static final int OpenParen=5;
    public static final int UpperCase=25;
    public static final int Character=18;
    public static final int Dot=12;
    public static final int LowerCase=24;
    public static final int UInteger=16;
    public static final int Whitespace=22;
    public static final int Comma=11;
    public static final int SpecialChars=20;
    public static final int Integer=17;

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

    // $ANTLR start "Decimal"
    public final void mDecimal() throws RecognitionException {
        try {
            int _type = Decimal;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:40:9: ( UInteger ( Dot UInteger )? ( 'E' Integer )? )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:40:11: UInteger ( Dot UInteger )? ( 'E' Integer )?
            {
            mUInteger(); 
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:40:20: ( Dot UInteger )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='.') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:40:21: Dot UInteger
                    {
                    mDot(); 
                    mUInteger(); 

                    }
                    break;

            }

            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:40:36: ( 'E' Integer )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='E') ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:40:37: 'E' Integer
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:42:9: ( Character ( Character | Digit | SpecialChars | VariableWhitespace )* )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:42:11: Character ( Character | Digit | SpecialChars | VariableWhitespace )*
            {
            mCharacter(); 
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:42:21: ( Character | Digit | SpecialChars | VariableWhitespace )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='\t'||LA3_0==' '||(LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' '||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:47:2: ( ( Whitespace )* )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:47:4: ( Whitespace )*
            {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:47:4: ( Whitespace )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='\t' && LA4_0<='\n')||(LA4_0>='\f' && LA4_0<='\r')||LA4_0==' ') ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:47:4: Whitespace
            	    {
            	    mWhitespace(); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:49:18: ( ( Add | Subtract )? UInteger )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:49:20: ( Add | Subtract )? UInteger
            {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:49:20: ( Add | Subtract )?
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:52:2: ( '0' .. '9' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:52:4: '0' .. '9'
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:54:2: ( ( Digit )+ )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:54:4: ( Digit )+
            {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:54:4: ( Digit )+
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
            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:54:4: Digit
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:57:2: ( ( '\\t' | ' ' ) )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:57:4: ( '\\t' | ' ' )
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:60:2: ( ( VariableWhitespace | '\\r' | '\\n' | '\\u000C' ) )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:60:4: ( VariableWhitespace | '\\r' | '\\n' | '\\u000C' )
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:63:2: ( 'a' .. 'z' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:63:4: 'a' .. 'z'
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:66:2: ( 'A' .. 'Z' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:66:4: 'A' .. 'Z'
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
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:69:2: ( LowerCase | UpperCase )
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

    // $ANTLR start "SpecialChars"
    public final void mSpecialChars() throws RecognitionException {
        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:73:2: ( '_' )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:73:4: '_'
            {
            match('_'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "SpecialChars"

    public void mTokens() throws RecognitionException {
        // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:8: ( OpenParen | CloseParen | Add | Subtract | Multiply | Divide | Comma | Dot | Exp | Decimal | Variable | HideWhiteSpace )
        int alt7=12;
        alt7 = dfa7.predict(input);
        switch (alt7) {
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
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:74: Decimal
                {
                mDecimal(); 

                }
                break;
            case 11 :
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:82: Variable
                {
                mVariable(); 

                }
                break;
            case 12 :
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:91: HideWhiteSpace
                {
                mHideWhiteSpace(); 

                }
                break;

        }

    }


    protected DFA7 dfa7 = new DFA7(this);
    static final String DFA7_eotS =
        "\1\14\14\uffff";
    static final String DFA7_eofS =
        "\15\uffff";
    static final String DFA7_minS =
        "\1\50\14\uffff";
    static final String DFA7_maxS =
        "\1\172\14\uffff";
    static final String DFA7_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14";
    static final String DFA7_specialS =
        "\15\uffff}>";
    static final String[] DFA7_transitionS = {
            "\1\1\1\2\1\5\1\3\1\7\1\4\1\10\1\6\12\12\7\uffff\32\13\3\uffff"+
            "\1\11\2\uffff\32\13",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( OpenParen | CloseParen | Add | Subtract | Multiply | Divide | Comma | Dot | Exp | Decimal | Variable | HideWhiteSpace );";
        }
    }
 

}