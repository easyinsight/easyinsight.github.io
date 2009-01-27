// $ANTLR 3.0.1 C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g 2008-02-10 12:20:44
package test;
import org.antlr.runtime.*;

public class HtmlDocLexer extends Lexer {
    public static final int DOC=4;
    public static final int T10=10;
    public static final int T11=11;
    public static final int T12=12;
    public static final int BODY=6;
    public static final int T13=13;
    public static final int T8=8;
    public static final int T9=9;
    public static final int TEXT=7;
    public static final int Tokens=14;
    public static final int EOF=-1;
    public static final int TITLE=5;
    public HtmlDocLexer() {;} 
    public HtmlDocLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g"; }

    // $ANTLR start DOC
    public final void mDOC() throws RecognitionException {
        try {
            int _type = DOC;
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:3:5: ( 'doc' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:3:7: 'doc'
            {
            match("doc"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end DOC

    // $ANTLR start TITLE
    public final void mTITLE() throws RecognitionException {
        try {
            int _type = TITLE;
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:4:7: ( 'title' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:4:9: 'title'
            {
            match("title"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end TITLE

    // $ANTLR start BODY
    public final void mBODY() throws RecognitionException {
        try {
            int _type = BODY;
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:5:6: ( 'body' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:5:8: 'body'
            {
            match("body"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end BODY

    // $ANTLR start T8
    public final void mT8() throws RecognitionException {
        try {
            int _type = T8;
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:6:4: ( '<html>' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:6:6: '<html>'
            {
            match("<html>"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T8

    // $ANTLR start T9
    public final void mT9() throws RecognitionException {
        try {
            int _type = T9;
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:7:4: ( '</html>' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:7:6: '</html>'
            {
            match("</html>"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T9

    // $ANTLR start T10
    public final void mT10() throws RecognitionException {
        try {
            int _type = T10;
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:8:5: ( '<title>' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:8:7: '<title>'
            {
            match("<title>"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T10

    // $ANTLR start T11
    public final void mT11() throws RecognitionException {
        try {
            int _type = T11;
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:9:5: ( '</title>' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:9:7: '</title>'
            {
            match("</title>"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T11

    // $ANTLR start T12
    public final void mT12() throws RecognitionException {
        try {
            int _type = T12;
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:10:5: ( '<body>' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:10:7: '<body>'
            {
            match("<body>"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T12

    // $ANTLR start T13
    public final void mT13() throws RecognitionException {
        try {
            int _type = T13;
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:11:5: ( '</body>' )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:11:7: '</body>'
            {
            match("</body>"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T13

    // $ANTLR start TEXT
    public final void mTEXT() throws RecognitionException {
        try {
            int _type = TEXT;
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:18:6: ( (~ ( '<' ) )* )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:18:8: (~ ( '<' ) )*
            {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:18:8: (~ ( '<' ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='\u0000' && LA1_0<=';')||(LA1_0>='=' && LA1_0<='\uFFFE')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:18:9: ~ ( '<' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<=';')||(input.LA(1)>='=' && input.LA(1)<='\uFFFE') ) {
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
            	    break loop1;
                }
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end TEXT

    public void mTokens() throws RecognitionException {
        // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:1:8: ( DOC | TITLE | BODY | T8 | T9 | T10 | T11 | T12 | T13 | TEXT )
        int alt2=10;
        switch ( input.LA(1) ) {
        case 'd':
            {
            int LA2_1 = input.LA(2);

            if ( (LA2_1=='o') ) {
                int LA2_6 = input.LA(3);

                if ( (LA2_6=='c') ) {
                    int LA2_13 = input.LA(4);

                    if ( ((LA2_13>='\u0000' && LA2_13<=';')||(LA2_13>='=' && LA2_13<='\uFFFE')) ) {
                        alt2=10;
                    }
                    else {
                        alt2=1;}
                }
                else {
                    alt2=10;}
            }
            else {
                alt2=10;}
            }
            break;
        case 't':
            {
            int LA2_2 = input.LA(2);

            if ( (LA2_2=='i') ) {
                int LA2_7 = input.LA(3);

                if ( (LA2_7=='t') ) {
                    int LA2_14 = input.LA(4);

                    if ( (LA2_14=='l') ) {
                        int LA2_20 = input.LA(5);

                        if ( (LA2_20=='e') ) {
                            int LA2_22 = input.LA(6);

                            if ( ((LA2_22>='\u0000' && LA2_22<=';')||(LA2_22>='=' && LA2_22<='\uFFFE')) ) {
                                alt2=10;
                            }
                            else {
                                alt2=2;}
                        }
                        else {
                            alt2=10;}
                    }
                    else {
                        alt2=10;}
                }
                else {
                    alt2=10;}
            }
            else {
                alt2=10;}
            }
            break;
        case 'b':
            {
            int LA2_3 = input.LA(2);

            if ( (LA2_3=='o') ) {
                int LA2_8 = input.LA(3);

                if ( (LA2_8=='d') ) {
                    int LA2_15 = input.LA(4);

                    if ( (LA2_15=='y') ) {
                        int LA2_21 = input.LA(5);

                        if ( ((LA2_21>='\u0000' && LA2_21<=';')||(LA2_21>='=' && LA2_21<='\uFFFE')) ) {
                            alt2=10;
                        }
                        else {
                            alt2=3;}
                    }
                    else {
                        alt2=10;}
                }
                else {
                    alt2=10;}
            }
            else {
                alt2=10;}
            }
            break;
        case '<':
            {
            switch ( input.LA(2) ) {
            case 'b':
                {
                alt2=8;
                }
                break;
            case 'h':
                {
                alt2=4;
                }
                break;
            case '/':
                {
                switch ( input.LA(3) ) {
                case 't':
                    {
                    alt2=7;
                    }
                    break;
                case 'b':
                    {
                    alt2=9;
                    }
                    break;
                case 'h':
                    {
                    alt2=5;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("1:1: Tokens : ( DOC | TITLE | BODY | T8 | T9 | T10 | T11 | T12 | T13 | TEXT );", 2, 11, input);

                    throw nvae;
                }

                }
                break;
            case 't':
                {
                alt2=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("1:1: Tokens : ( DOC | TITLE | BODY | T8 | T9 | T10 | T11 | T12 | T13 | TEXT );", 2, 4, input);

                throw nvae;
            }

            }
            break;
        default:
            alt2=10;}

        switch (alt2) {
            case 1 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:1:10: DOC
                {
                mDOC(); 

                }
                break;
            case 2 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:1:14: TITLE
                {
                mTITLE(); 

                }
                break;
            case 3 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:1:20: BODY
                {
                mBODY(); 

                }
                break;
            case 4 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:1:25: T8
                {
                mT8(); 

                }
                break;
            case 5 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:1:28: T9
                {
                mT9(); 

                }
                break;
            case 6 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:1:31: T10
                {
                mT10(); 

                }
                break;
            case 7 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:1:35: T11
                {
                mT11(); 

                }
                break;
            case 8 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:1:39: T12
                {
                mT12(); 

                }
                break;
            case 9 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:1:43: T13
                {
                mT13(); 

                }
                break;
            case 10 :
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:1:47: TEXT
                {
                mTEXT(); 

                }
                break;

        }

    }


 

}