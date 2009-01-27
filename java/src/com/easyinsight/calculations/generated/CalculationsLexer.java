// $ANTLR 3.0.1 C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g 2008-12-20 15:01:29
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
    public static final int Tokens=26;
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
    public CalculationsLexer() {;} 
    public CalculationsLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g"; }

    // $ANTLR start OpenParen
    public final void mOpenParen() throws RecognitionException {
        try {
            int _type = OpenParen;
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:4:11: ( '(' )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:4:13: '('
            {
            match('('); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end OpenParen

    // $ANTLR start CloseParen
    public final void mCloseParen() throws RecognitionException {
        try {
            int _type = CloseParen;
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:5:12: ( ')' )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:5:14: ')'
            {
            match(')'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end CloseParen

    // $ANTLR start Add
    public final void mAdd() throws RecognitionException {
        try {
            int _type = Add;
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:6:5: ( '+' )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:6:7: '+'
            {
            match('+'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end Add

    // $ANTLR start Subtract
    public final void mSubtract() throws RecognitionException {
        try {
            int _type = Subtract;
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:7:10: ( '-' )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:7:12: '-'
            {
            match('-'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end Subtract

    // $ANTLR start Multiply
    public final void mMultiply() throws RecognitionException {
        try {
            int _type = Multiply;
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:8:10: ( '*' )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:8:12: '*'
            {
            match('*'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end Multiply

    // $ANTLR start Divide
    public final void mDivide() throws RecognitionException {
        try {
            int _type = Divide;
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:9:8: ( '/' )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:9:10: '/'
            {
            match('/'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end Divide

    // $ANTLR start Comma
    public final void mComma() throws RecognitionException {
        try {
            int _type = Comma;
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:10:7: ( ',' )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:10:9: ','
            {
            match(','); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end Comma

    // $ANTLR start Dot
    public final void mDot() throws RecognitionException {
        try {
            int _type = Dot;
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:11:5: ( '.' )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:11:7: '.'
            {
            match('.'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end Dot

    // $ANTLR start Exp
    public final void mExp() throws RecognitionException {
        try {
            int _type = Exp;
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:12:5: ( '^' )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:12:7: '^'
            {
            match('^'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end Exp

    // $ANTLR start Decimal
    public final void mDecimal() throws RecognitionException {
        try {
            int _type = Decimal;
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:40:9: ( UInteger ( Dot UInteger )? ( 'E' Integer )? )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:40:11: UInteger ( Dot UInteger )? ( 'E' Integer )?
            {
            mUInteger(); 
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:40:20: ( Dot UInteger )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='.') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:40:21: Dot UInteger
                    {
                    mDot(); 
                    mUInteger(); 

                    }
                    break;

            }

            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:40:36: ( 'E' Integer )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='E') ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:40:37: 'E' Integer
                    {
                    match('E'); 
                    mInteger(); 

                    }
                    break;

            }


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end Decimal

    // $ANTLR start Variable
    public final void mVariable() throws RecognitionException {
        try {
            int _type = Variable;
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:42:9: ( Character ( Character | Digit | SpecialChars | VariableWhitespace )* )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:42:11: Character ( Character | Digit | SpecialChars | VariableWhitespace )*
            {
            mCharacter(); 
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:42:21: ( Character | Digit | SpecialChars | VariableWhitespace )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='\t'||LA3_0==' '||(LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' '||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end Variable

    // $ANTLR start HideWhiteSpace
    public final void mHideWhiteSpace() throws RecognitionException {
        try {
            int _type = HideWhiteSpace;
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:47:2: ( ( Whitespace )* )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:47:4: ( Whitespace )*
            {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:47:4: ( Whitespace )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='\t' && LA4_0<='\n')||(LA4_0>='\f' && LA4_0<='\r')||LA4_0==' ') ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:47:4: Whitespace
            	    {
            	    mWhitespace(); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

              channel = HIDDEN; 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end HideWhiteSpace

    // $ANTLR start Integer
    public final void mInteger() throws RecognitionException {
        try {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:49:18: ( ( Add | Subtract )? UInteger )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:49:20: ( Add | Subtract )? UInteger
            {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:49:20: ( Add | Subtract )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='+'||LA5_0=='-') ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse =
                            new MismatchedSetException(null,input);
                        recover(mse);    throw mse;
                    }


                    }
                    break;

            }

            mUInteger(); 

            }

        }
        finally {
        }
    }
    // $ANTLR end Integer

    // $ANTLR start Digit
    public final void mDigit() throws RecognitionException {
        try {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:52:2: ( '0' .. '9' )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:52:4: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end Digit

    // $ANTLR start UInteger
    public final void mUInteger() throws RecognitionException {
        try {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:54:2: ( ( Digit )+ )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:54:4: ( Digit )+
            {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:54:4: ( Digit )+
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
            	    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:54:4: Digit
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
    // $ANTLR end UInteger

    // $ANTLR start VariableWhitespace
    public final void mVariableWhitespace() throws RecognitionException {
        try {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:57:2: ( ( '\\t' | ' ' ) )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:57:4: ( '\\t' | ' ' )
            {
            if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }


            }

        }
        finally {
        }
    }
    // $ANTLR end VariableWhitespace

    // $ANTLR start Whitespace
    public final void mWhitespace() throws RecognitionException {
        try {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:60:2: ( ( VariableWhitespace | '\\r' | '\\n' | '\\u000C' ) )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:60:4: ( VariableWhitespace | '\\r' | '\\n' | '\\u000C' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }


            }

        }
        finally {
        }
    }
    // $ANTLR end Whitespace

    // $ANTLR start LowerCase
    public final void mLowerCase() throws RecognitionException {
        try {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:63:2: ( 'a' .. 'z' )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:63:4: 'a' .. 'z'
            {
            matchRange('a','z'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end LowerCase

    // $ANTLR start UpperCase
    public final void mUpperCase() throws RecognitionException {
        try {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:66:2: ( 'A' .. 'Z' )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:66:4: 'A' .. 'Z'
            {
            matchRange('A','Z'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end UpperCase

    // $ANTLR start Character
    public final void mCharacter() throws RecognitionException {
        try {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:69:2: ( LowerCase | UpperCase )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }


            }

        }
        finally {
        }
    }
    // $ANTLR end Character

    // $ANTLR start SpecialChars
    public final void mSpecialChars() throws RecognitionException {
        try {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:73:2: ( '_' )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:73:4: '_'
            {
            match('_'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end SpecialChars

    public void mTokens() throws RecognitionException {
        // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:8: ( OpenParen | CloseParen | Add | Subtract | Multiply | Divide | Comma | Dot | Exp | Decimal | Variable | HideWhiteSpace )
        int alt7=12;
        switch ( input.LA(1) ) {
        case '(':
            {
            alt7=1;
            }
            break;
        case ')':
            {
            alt7=2;
            }
            break;
        case '+':
            {
            alt7=3;
            }
            break;
        case '-':
            {
            alt7=4;
            }
            break;
        case '*':
            {
            alt7=5;
            }
            break;
        case '/':
            {
            alt7=6;
            }
            break;
        case ',':
            {
            alt7=7;
            }
            break;
        case '.':
            {
            alt7=8;
            }
            break;
        case '^':
            {
            alt7=9;
            }
            break;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            {
            alt7=10;
            }
            break;
        case 'A':
        case 'B':
        case 'C':
        case 'D':
        case 'E':
        case 'F':
        case 'G':
        case 'H':
        case 'I':
        case 'J':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
        case 'a':
        case 'b':
        case 'c':
        case 'd':
        case 'e':
        case 'f':
        case 'g':
        case 'h':
        case 'i':
        case 'j':
        case 'k':
        case 'l':
        case 'm':
        case 'n':
        case 'o':
        case 'p':
        case 'q':
        case 'r':
        case 's':
        case 't':
        case 'u':
        case 'v':
        case 'w':
        case 'x':
        case 'y':
        case 'z':
            {
            alt7=11;
            }
            break;
        default:
            alt7=12;}

        switch (alt7) {
            case 1 :
                // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:10: OpenParen
                {
                mOpenParen(); 

                }
                break;
            case 2 :
                // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:20: CloseParen
                {
                mCloseParen(); 

                }
                break;
            case 3 :
                // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:31: Add
                {
                mAdd(); 

                }
                break;
            case 4 :
                // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:35: Subtract
                {
                mSubtract(); 

                }
                break;
            case 5 :
                // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:44: Multiply
                {
                mMultiply(); 

                }
                break;
            case 6 :
                // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:53: Divide
                {
                mDivide(); 

                }
                break;
            case 7 :
                // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:60: Comma
                {
                mComma(); 

                }
                break;
            case 8 :
                // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:66: Dot
                {
                mDot(); 

                }
                break;
            case 9 :
                // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:70: Exp
                {
                mExp(); 

                }
                break;
            case 10 :
                // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:74: Decimal
                {
                mDecimal(); 

                }
                break;
            case 11 :
                // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:82: Variable
                {
                mVariable(); 

                }
                break;
            case 12 :
                // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:1:91: HideWhiteSpace
                {
                mHideWhiteSpace(); 

                }
                break;

        }

    }


 

}