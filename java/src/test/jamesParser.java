// $ANTLR 3.0.1 C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g 2008-02-11 22:29:46
package test;
import org.antlr.runtime.*;

public class jamesParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Add", "Subtract", "Multiply", "Divide", "OpenParen", "CloseParen", "Integer", "Variable", "Comma", "Digit", "UInteger", "Whitespace", "LowerCase", "UpperCase", "Character", "SpecialChar", "SpacedVariableName", "HideWhiteSpace"
    };
    public static final int CloseParen=9;
    public static final int HideWhiteSpace=21;
    public static final int Subtract=5;
    public static final int SpacedVariableName=20;
    public static final int Multiply=6;
    public static final int Digit=13;
    public static final int Add=4;
    public static final int EOF=-1;
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

        public jamesParser(TokenStream input) {
            super(input);
        }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g"; }



    // $ANTLR start expr
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:5:1: expr : term ( ( Add | Subtract ) term )* ;
    public final void expr() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:5:6: ( term ( ( Add | Subtract ) term )* )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:5:8: term ( ( Add | Subtract ) term )*
            {
            pushFollow(FOLLOW_term_in_expr20);
            term();
            _fsp--;

            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:5:13: ( ( Add | Subtract ) term )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=Add && LA1_0<=Subtract)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:5:14: ( Add | Subtract ) term
            	    {
            	    if ( (input.LA(1)>=Add && input.LA(1)<=Subtract) ) {
            	        input.consume();
            	        errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_expr23);    throw mse;
            	    }

            	    pushFollow(FOLLOW_term_in_expr31);
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
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:6:1: term : factor ( ( Multiply | Divide ) factor )* ;
    public final void term() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:6:6: ( factor ( ( Multiply | Divide ) factor )* )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:6:8: factor ( ( Multiply | Divide ) factor )*
            {
            pushFollow(FOLLOW_factor_in_term40);
            factor();
            _fsp--;

            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:6:15: ( ( Multiply | Divide ) factor )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=Multiply && LA2_0<=Divide)) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:6:16: ( Multiply | Divide ) factor
            	    {
            	    if ( (input.LA(1)>=Multiply && input.LA(1)<=Divide) ) {
            	        input.consume();
            	        errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_term43);    throw mse;
            	    }

            	    pushFollow(FOLLOW_factor_in_term51);
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
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:7:1: factor : ( symbol | OpenParen expr CloseParen );
    public final void factor() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:7:8: ( symbol | OpenParen expr CloseParen )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( ((LA3_0>=Integer && LA3_0<=Variable)) ) {
                alt3=1;
            }
            else if ( (LA3_0==OpenParen) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("7:1: factor : ( symbol | OpenParen expr CloseParen );", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:7:10: symbol
                    {
                    pushFollow(FOLLOW_symbol_in_factor60);
                    symbol();
                    _fsp--;


                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:7:19: OpenParen expr CloseParen
                    {
                    match(input,OpenParen,FOLLOW_OpenParen_in_factor64); 
                    pushFollow(FOLLOW_expr_in_factor66);
                    expr();
                    _fsp--;

                    match(input,CloseParen,FOLLOW_CloseParen_in_factor68); 

                    }
                    break;

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


    // $ANTLR start symbol
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:8:1: symbol : ( Integer | Variable | function );
    public final void symbol() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:8:8: ( Integer | Variable | function )
            int alt4=3;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==Integer) ) {
                alt4=1;
            }
            else if ( (LA4_0==Variable) ) {
                int LA4_2 = input.LA(2);

                if ( (LA4_2==OpenParen) ) {
                    alt4=3;
                }
                else if ( ((LA4_2>=Add && LA4_2<=Divide)||LA4_2==CloseParen||LA4_2==Comma) ) {
                    alt4=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("8:1: symbol : ( Integer | Variable | function );", 4, 2, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("8:1: symbol : ( Integer | Variable | function );", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:8:10: Integer
                    {
                    match(input,Integer,FOLLOW_Integer_in_symbol75); 

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:8:20: Variable
                    {
                    match(input,Variable,FOLLOW_Variable_in_symbol79); 

                    }
                    break;
                case 3 :
                    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:8:31: function
                    {
                    pushFollow(FOLLOW_function_in_symbol83);
                    function();
                    _fsp--;


                    }
                    break;

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
    // $ANTLR end symbol


    // $ANTLR start function
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:9:1: function : Variable OpenParen ( expr ( Comma expr )* )? CloseParen ;
    public final void function() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:9:9: ( Variable OpenParen ( expr ( Comma expr )* )? CloseParen )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:9:11: Variable OpenParen ( expr ( Comma expr )* )? CloseParen
            {
            match(input,Variable,FOLLOW_Variable_in_function89); 
            match(input,OpenParen,FOLLOW_OpenParen_in_function91); 
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:9:30: ( expr ( Comma expr )* )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==OpenParen||(LA6_0>=Integer && LA6_0<=Variable)) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:9:31: expr ( Comma expr )*
                    {
                    pushFollow(FOLLOW_expr_in_function94);
                    expr();
                    _fsp--;

                    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:9:36: ( Comma expr )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0==Comma) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\james.g:9:37: Comma expr
                    	    {
                    	    match(input,Comma,FOLLOW_Comma_in_function97); 
                    	    pushFollow(FOLLOW_expr_in_function99);
                    	    expr();
                    	    _fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);


                    }
                    break;

            }

            match(input,CloseParen,FOLLOW_CloseParen_in_function105); 

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
    // $ANTLR end function


 

    public static final BitSet FOLLOW_term_in_expr20 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_set_in_expr23 = new BitSet(new long[]{0x0000000000000D00L});
    public static final BitSet FOLLOW_term_in_expr31 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_factor_in_term40 = new BitSet(new long[]{0x00000000000000C2L});
    public static final BitSet FOLLOW_set_in_term43 = new BitSet(new long[]{0x0000000000000D00L});
    public static final BitSet FOLLOW_factor_in_term51 = new BitSet(new long[]{0x00000000000000C2L});
    public static final BitSet FOLLOW_symbol_in_factor60 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OpenParen_in_factor64 = new BitSet(new long[]{0x0000000000000D00L});
    public static final BitSet FOLLOW_expr_in_factor66 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_CloseParen_in_factor68 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Integer_in_symbol75 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Variable_in_symbol79 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_symbol83 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Variable_in_function89 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_OpenParen_in_function91 = new BitSet(new long[]{0x0000000000000F00L});
    public static final BitSet FOLLOW_expr_in_function94 = new BitSet(new long[]{0x0000000000001200L});
    public static final BitSet FOLLOW_Comma_in_function97 = new BitSet(new long[]{0x0000000000000D00L});
    public static final BitSet FOLLOW_expr_in_function99 = new BitSet(new long[]{0x0000000000001200L});
    public static final BitSet FOLLOW_CloseParen_in_function105 = new BitSet(new long[]{0x0000000000000002L});

}