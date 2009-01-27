// $ANTLR 3.0.1 C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Learning.g 2008-02-10 12:15:50

package test;
import org.antlr.runtime.*;

public class LearningParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "PLUS", "MINUS", "MULT", "DIV", "NUMBER", "DIGIT", "WHITESPACE"
    };
    public static final int NUMBER=8;
    public static final int WHITESPACE=10;
    public static final int PLUS=4;
    public static final int DIGIT=9;
    public static final int MINUS=5;
    public static final int MULT=6;
    public static final int DIV=7;
    public static final int EOF=-1;

        public LearningParser(TokenStream input) {
            super(input);
        }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Learning.g"; }

    
        public static void main(String[] args) throws Exception {
            LearningLexer lex = new LearningLexer(new ANTLRFileStream(args[0]));
           	CommonTokenStream tokens = new CommonTokenStream(lex);
    
            LearningParser parser = new LearningParser(tokens);
    
            try {
                parser.expr();
            } catch (RecognitionException e)  {
                e.printStackTrace();
            }
        }



    // $ANTLR start expr
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Learning.g:31:1: expr : term ( ( PLUS | MINUS ) term )* ;
    public final void expr() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Learning.g:31:6: ( term ( ( PLUS | MINUS ) term )* )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Learning.g:31:8: term ( ( PLUS | MINUS ) term )*
            {
            pushFollow(FOLLOW_term_in_expr71);
            term();
            _fsp--;

            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Learning.g:31:13: ( ( PLUS | MINUS ) term )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=PLUS && LA1_0<=MINUS)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Learning.g:31:15: ( PLUS | MINUS ) term
            	    {
            	    if ( (input.LA(1)>=PLUS && input.LA(1)<=MINUS) ) {
            	        input.consume();
            	        errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_expr75);    throw mse;
            	    }

            	    pushFollow(FOLLOW_term_in_expr86);
            	    term();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end expr


    // $ANTLR start term
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Learning.g:33:1: term : factor ( ( MULT | DIV ) factor )* ;
    public final void term() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Learning.g:33:6: ( factor ( ( MULT | DIV ) factor )* )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Learning.g:33:8: factor ( ( MULT | DIV ) factor )*
            {
            pushFollow(FOLLOW_factor_in_term98);
            factor();
            _fsp--;

            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Learning.g:33:15: ( ( MULT | DIV ) factor )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=MULT && LA2_0<=DIV)) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Learning.g:33:17: ( MULT | DIV ) factor
            	    {
            	    if ( (input.LA(1)>=MULT && input.LA(1)<=DIV) ) {
            	        input.consume();
            	        errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_term102);    throw mse;
            	    }

            	    pushFollow(FOLLOW_factor_in_term112);
            	    factor();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end term


    // $ANTLR start factor
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Learning.g:35:1: factor : NUMBER ;
    public final void factor() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Learning.g:35:8: ( NUMBER )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Learning.g:35:10: NUMBER
            {
            match(input,NUMBER,FOLLOW_NUMBER_in_factor124); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end factor


 

    public static final BitSet FOLLOW_term_in_expr71 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_set_in_expr75 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_term_in_expr86 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_factor_in_term98 = new BitSet(new long[]{0x00000000000000C2L});
    public static final BitSet FOLLOW_set_in_term102 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_factor_in_term112 = new BitSet(new long[]{0x00000000000000C2L});
    public static final BitSet FOLLOW_NUMBER_in_factor124 = new BitSet(new long[]{0x0000000000000002L});

}