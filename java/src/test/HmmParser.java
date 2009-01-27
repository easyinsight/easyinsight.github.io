// $ANTLR 3.0.1 C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g 2008-02-12 18:56:29

package test;

import org.antlr.runtime.*;


import org.antlr.runtime.tree.*;

public class HmmParser extends Parser {
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

        public HmmParser(TokenStream input) {
            super(input);
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g"; }


    public static class expr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start expr
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:5:1: expr : term ( ( Add | Subtract ) term )* ;
    public final expr_return expr() throws RecognitionException {
        expr_return retval = new expr_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set2=null;
        term_return term1 = null;

        term_return term3 = null;


        Object set2_tree=null;

        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:5:6: ( term ( ( Add | Subtract ) term )* )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:5:8: term ( ( Add | Subtract ) term )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_term_in_expr20);
            term1=term();
            _fsp--;

            adaptor.addChild(root_0, term1.getTree());
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:5:13: ( ( Add | Subtract ) term )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=Add && LA1_0<=Subtract)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:5:14: ( Add | Subtract ) term
            	    {
            	    set2=(Token)input.LT(1);
            	    if ( (input.LA(1)>=Add && input.LA(1)<=Subtract) ) {
            	        input.consume();
            	        adaptor.addChild(root_0, adaptor.create(set2));
            	        errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_expr23);    throw mse;
            	    }

            	    pushFollow(FOLLOW_term_in_expr31);
            	    term3=term();
            	    _fsp--;

            	    adaptor.addChild(root_0, term3.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

                retval.tree = (Object)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end expr

    public static class term_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start term
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:6:1: term : factor ( ( Multiply | Divide ) factor )* ;
    public final term_return term() throws RecognitionException {
        term_return retval = new term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set5=null;
        factor_return factor4 = null;

        factor_return factor6 = null;


        Object set5_tree=null;

        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:6:6: ( factor ( ( Multiply | Divide ) factor )* )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:6:8: factor ( ( Multiply | Divide ) factor )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_factor_in_term40);
            factor4=factor();
            _fsp--;

            adaptor.addChild(root_0, factor4.getTree());
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:6:15: ( ( Multiply | Divide ) factor )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=Multiply && LA2_0<=Divide)) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:6:16: ( Multiply | Divide ) factor
            	    {
            	    set5=(Token)input.LT(1);
            	    if ( (input.LA(1)>=Multiply && input.LA(1)<=Divide) ) {
            	        input.consume();
            	        adaptor.addChild(root_0, adaptor.create(set5));
            	        errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_term43);    throw mse;
            	    }

            	    pushFollow(FOLLOW_factor_in_term51);
            	    factor6=factor();
            	    _fsp--;

            	    adaptor.addChild(root_0, factor6.getTree());

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

                retval.tree = (Object)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end term

    public static class factor_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start factor
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:7:1: factor : ( symbol | OpenParen expr CloseParen );
    public final factor_return factor() throws RecognitionException {
        factor_return retval = new factor_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token OpenParen8=null;
        Token CloseParen10=null;
        symbol_return symbol7 = null;

        expr_return expr9 = null;


        Object OpenParen8_tree=null;
        Object CloseParen10_tree=null;

        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:7:8: ( symbol | OpenParen expr CloseParen )
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
                    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:7:10: symbol
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_symbol_in_factor60);
                    symbol7=symbol();
                    _fsp--;

                    adaptor.addChild(root_0, symbol7.getTree());

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:7:19: OpenParen expr CloseParen
                    {
                    root_0 = (Object)adaptor.nil();

                    OpenParen8=(Token)input.LT(1);
                    match(input,OpenParen,FOLLOW_OpenParen_in_factor64); 
                    OpenParen8_tree = (Object)adaptor.create(OpenParen8);
                    adaptor.addChild(root_0, OpenParen8_tree);

                    pushFollow(FOLLOW_expr_in_factor66);
                    expr9=expr();
                    _fsp--;

                    adaptor.addChild(root_0, expr9.getTree());
                    CloseParen10=(Token)input.LT(1);
                    match(input,CloseParen,FOLLOW_CloseParen_in_factor68); 
                    CloseParen10_tree = (Object)adaptor.create(CloseParen10);
                    adaptor.addChild(root_0, CloseParen10_tree);


                    }
                    break;

            }
            retval.stop = input.LT(-1);

                retval.tree = (Object)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end factor

    public static class symbol_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start symbol
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:8:1: symbol : ( Integer | Variable | function );
    public final symbol_return symbol() throws RecognitionException {
        symbol_return retval = new symbol_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Integer11=null;
        Token Variable12=null;
        function_return function13 = null;


        Object Integer11_tree=null;
        Object Variable12_tree=null;

        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:8:8: ( Integer | Variable | function )
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
                    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:8:10: Integer
                    {
                    root_0 = (Object)adaptor.nil();

                    Integer11=(Token)input.LT(1);
                    match(input,Integer,FOLLOW_Integer_in_symbol75); 
                    Integer11_tree = (Object)adaptor.create(Integer11);
                    adaptor.addChild(root_0, Integer11_tree);


                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:8:20: Variable
                    {
                    root_0 = (Object)adaptor.nil();

                    Variable12=(Token)input.LT(1);
                    match(input,Variable,FOLLOW_Variable_in_symbol79); 
                    Variable12_tree = (Object)adaptor.create(Variable12);
                    adaptor.addChild(root_0, Variable12_tree);


                    }
                    break;
                case 3 :
                    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:8:31: function
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_function_in_symbol83);
                    function13=function();
                    _fsp--;

                    adaptor.addChild(root_0, function13.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

                retval.tree = (Object)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end symbol

    public static class function_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start function
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:9:1: function : Variable OpenParen ( expr ( Comma expr )* )? CloseParen ;
    public final function_return function() throws RecognitionException {
        function_return retval = new function_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Variable14=null;
        Token OpenParen15=null;
        Token Comma17=null;
        Token CloseParen19=null;
        expr_return expr16 = null;

        expr_return expr18 = null;


        Object Variable14_tree=null;
        Object OpenParen15_tree=null;
        Object Comma17_tree=null;
        Object CloseParen19_tree=null;

        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:9:9: ( Variable OpenParen ( expr ( Comma expr )* )? CloseParen )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:9:11: Variable OpenParen ( expr ( Comma expr )* )? CloseParen
            {
            root_0 = (Object)adaptor.nil();

            Variable14=(Token)input.LT(1);
            match(input,Variable,FOLLOW_Variable_in_function89); 
            Variable14_tree = (Object)adaptor.create(Variable14);
            adaptor.addChild(root_0, Variable14_tree);

            OpenParen15=(Token)input.LT(1);
            match(input,OpenParen,FOLLOW_OpenParen_in_function91); 
            OpenParen15_tree = (Object)adaptor.create(OpenParen15);
            adaptor.addChild(root_0, OpenParen15_tree);

            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:9:30: ( expr ( Comma expr )* )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==OpenParen||(LA6_0>=Integer && LA6_0<=Variable)) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:9:31: expr ( Comma expr )*
                    {
                    pushFollow(FOLLOW_expr_in_function94);
                    expr16=expr();
                    _fsp--;

                    adaptor.addChild(root_0, expr16.getTree());
                    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:9:36: ( Comma expr )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0==Comma) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Hmm.g:9:37: Comma expr
                    	    {
                    	    Comma17=(Token)input.LT(1);
                    	    match(input,Comma,FOLLOW_Comma_in_function97); 
                    	    Comma17_tree = (Object)adaptor.create(Comma17);
                    	    adaptor.addChild(root_0, Comma17_tree);

                    	    pushFollow(FOLLOW_expr_in_function99);
                    	    expr18=expr();
                    	    _fsp--;

                    	    adaptor.addChild(root_0, expr18.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);


                    }
                    break;

            }

            CloseParen19=(Token)input.LT(1);
            match(input,CloseParen,FOLLOW_CloseParen_in_function105); 
            CloseParen19_tree = (Object)adaptor.create(CloseParen19);
            adaptor.addChild(root_0, CloseParen19_tree);


            }

            retval.stop = input.LT(-1);

                retval.tree = (Object)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
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