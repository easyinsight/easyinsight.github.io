// $ANTLR 3.0.1 C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g 2008-02-12 18:56:29

package test;

import org.antlr.runtime.*;

public class HmmLexer extends Lexer {
    public static final int CloseParen=9;
    public static final int HideWhiteSpace=21;
    public static final int Subtract=5;
    public static final int SpacedVariableName=20;
    public static final int Multiply=6;
    public static final int Digit=13;
    public static final int Tokens=22;
    public static final int EOF=-1;
    public static final int Add=4;
    public static final int Divide=7;
    public static final int Variable=11;
    public static final int OpenParen=8;
    public static final int UpperCase=17;
    public static final int Character=18;
    public static final int SpecialChar=19;
    public static final int LowerCase=16;
    public static final int UInteger=14;
    public static final int Whitespace=15;
    public static final int Comma=12;
    public static final int Integer=10;
    public HmmLexer() {;} 
    public HmmLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g"; }

    // $ANTLR start Digit
    public final void mDigit() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:14:2: ( '0' .. '9' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:14:4: '0' .. '9'
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
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:17:2: ( ( Digit )+ )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:17:4: ( Digit )+
            {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:17:4: ( Digit )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='0' && LA1_0<='9')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:17:4: Digit
            	    {
            	    mDigit(); 

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end UInteger

    // $ANTLR start Integer
    public final void mInteger() throws RecognitionException {
        try {
            int _type = Integer;
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:18:9: ( ( Add | Subtract )? UInteger )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:18:11: ( Add | Subtract )? UInteger
            {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:18:11: ( Add | Subtract )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='+'||LA2_0=='-') ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:
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

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end Integer

    // $ANTLR start OpenParen
    public final void mOpenParen() throws RecognitionException {
        try {
            int _type = OpenParen;
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:21:2: ( '(' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:21:4: '('
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
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:23:2: ( ')' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:23:4: ')'
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
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:25:5: ( '+' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:25:7: '+'
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
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:26:9: ( '-' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:26:11: '-'
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
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:27:9: ( '*' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:27:11: '*'
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
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:28:8: ( '/' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:28:10: '/'
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
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:29:7: ( ',' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:29:9: ','
            {
            match(','); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end Comma

    // $ANTLR start Whitespace
    public final void mWhitespace() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:32:2: ( ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' ) )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:32:4: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )
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
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:35:2: ( 'a' .. 'z' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:35:4: 'a' .. 'z'
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
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:38:2: ( 'A' .. 'Z' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:38:4: 'A' .. 'Z'
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
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:41:2: ( LowerCase | UpperCase )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:
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

    // $ANTLR start SpecialChar
    public final void mSpecialChar() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:44:2: ( '_' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:44:4: '_'
            {
            match('_'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end SpecialChar

    // $ANTLR start Variable
    public final void mVariable() throws RecognitionException {
        try {
            int _type = Variable;
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:46:9: ( SpacedVariableName )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:46:11: SpacedVariableName
            {
            mSpacedVariableName(); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end Variable

    // $ANTLR start SpacedVariableName
    public final void mSpacedVariableName() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:49:2: ( Character ( Character | Digit | SpecialChar | Whitespace )* )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:49:4: Character ( Character | Digit | SpecialChar | Whitespace )*
            {
            mCharacter(); 
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:49:14: ( Character | Digit | SpecialChar | Whitespace )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='\t' && LA3_0<='\n')||(LA3_0>='\f' && LA3_0<='\r')||LA3_0==' '||(LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' '||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
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

        }
        finally {
        }
    }
    // $ANTLR end SpacedVariableName

    // $ANTLR start HideWhiteSpace
    public final void mHideWhiteSpace() throws RecognitionException {
        try {
            int _type = HideWhiteSpace;
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:52:2: ( ( Whitespace )* )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:52:4: ( Whitespace )*
            {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:52:4: ( Whitespace )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='\t' && LA4_0<='\n')||(LA4_0>='\f' && LA4_0<='\r')||LA4_0==' ') ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:52:4: Whitespace
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

    public void mTokens() throws RecognitionException {
        // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:1:8: ( Integer | OpenParen | CloseParen | Add | Subtract | Multiply | Divide | Comma | Variable | HideWhiteSpace )
        int alt5=10;
        switch ( input.LA(1) ) {
        case '+':
            {
            int LA5_1 = input.LA(2);

            if ( ((LA5_1>='0' && LA5_1<='9')) ) {
                alt5=1;
            }
            else {
                alt5=4;}
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
            alt5=1;
            }
            break;
        case '(':
            {
            alt5=2;
            }
            break;
        case ')':
            {
            alt5=3;
            }
            break;
        case '-':
            {
            int LA5_5 = input.LA(2);

            if ( ((LA5_5>='0' && LA5_5<='9')) ) {
                alt5=1;
            }
            else {
                alt5=5;}
            }
            break;
        case '*':
            {
            alt5=6;
            }
            break;
        case '/':
            {
            alt5=7;
            }
            break;
        case ',':
            {
            alt5=8;
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
            alt5=9;
            }
            break;
        default:
            alt5=10;}

        switch (alt5) {
            case 1 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:1:10: Integer
                {
                mInteger(); 

                }
                break;
            case 2 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:1:18: OpenParen
                {
                mOpenParen(); 

                }
                break;
            case 3 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:1:28: CloseParen
                {
                mCloseParen(); 

                }
                break;
            case 4 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:1:39: Add
                {
                mAdd(); 

                }
                break;
            case 5 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:1:43: Subtract
                {
                mSubtract(); 

                }
                break;
            case 6 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:1:52: Multiply
                {
                mMultiply(); 

                }
                break;
            case 7 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:1:61: Divide
                {
                mDivide(); 

                }
                break;
            case 8 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:1:68: Comma
                {
                mComma(); 

                }
                break;
            case 9 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:1:74: Variable
                {
                mVariable(); 

                }
                break;
            case 10 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:1:83: HideWhiteSpace
                {
                mHideWhiteSpace(); 

                }
                break;

        }

    }


 

}