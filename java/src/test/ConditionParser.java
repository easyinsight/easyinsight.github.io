// $ANTLR 3.0.1 C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Condition.g 2008-02-10 12:26:08

package test;

import org.antlr.runtime.*;


import org.antlr.runtime.tree.*;

public class ConditionParser extends Parser {
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

        public ConditionParser(TokenStream input) {
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
    public String getGrammarFileName() { return "C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Condition.g"; }


    public static class expr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start expr
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Condition.g:14:1: expr : term ( ( PLUS | MINUS ) term )* ;
    public final expr_return expr() throws RecognitionException {
        expr_return retval = new expr_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set2=null;
        term_return term1 = null;

        term_return term3 = null;


        Object set2_tree=null;

        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Condition.g:14:6: ( term ( ( PLUS | MINUS ) term )* )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Condition.g:14:8: term ( ( PLUS | MINUS ) term )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_term_in_expr63);
            term1=term();
            _fsp--;

            adaptor.addChild(root_0, term1.getTree());
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Condition.g:14:13: ( ( PLUS | MINUS ) term )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=PLUS && LA1_0<=MINUS)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Condition.g:14:15: ( PLUS | MINUS ) term
            	    {
            	    set2=(Token)input.LT(1);
            	    if ( (input.LA(1)>=PLUS && input.LA(1)<=MINUS) ) {
            	        input.consume();
            	        adaptor.addChild(root_0, adaptor.create(set2));
            	        errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_expr67);    throw mse;
            	    }

            	    pushFollow(FOLLOW_term_in_expr78);
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
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Condition.g:16:1: term : factor ( ( MULT | DIV ) factor )* ;
    public final term_return term() throws RecognitionException {
        term_return retval = new term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set5=null;
        factor_return factor4 = null;

        factor_return factor6 = null;


        Object set5_tree=null;

        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Condition.g:16:6: ( factor ( ( MULT | DIV ) factor )* )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Condition.g:16:8: factor ( ( MULT | DIV ) factor )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_factor_in_term90);
            factor4=factor();
            _fsp--;

            adaptor.addChild(root_0, factor4.getTree());
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Condition.g:16:15: ( ( MULT | DIV ) factor )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=MULT && LA2_0<=DIV)) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Condition.g:16:17: ( MULT | DIV ) factor
            	    {
            	    set5=(Token)input.LT(1);
            	    if ( (input.LA(1)>=MULT && input.LA(1)<=DIV) ) {
            	        input.consume();
            	        adaptor.addChild(root_0, adaptor.create(set5));
            	        errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_term94);    throw mse;
            	    }

            	    pushFollow(FOLLOW_factor_in_term104);
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
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Condition.g:18:1: factor : NUMBER ;
    public final factor_return factor() throws RecognitionException {
        factor_return retval = new factor_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NUMBER7=null;

        Object NUMBER7_tree=null;

        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Condition.g:18:8: ( NUMBER )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\Condition.g:18:10: NUMBER
            {
            root_0 = (Object)adaptor.nil();

            NUMBER7=(Token)input.LT(1);
            match(input,NUMBER,FOLLOW_NUMBER_in_factor116); 
            NUMBER7_tree = (Object)adaptor.create(NUMBER7);
            adaptor.addChild(root_0, NUMBER7_tree);


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


 

    public static final BitSet FOLLOW_term_in_expr63 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_set_in_expr67 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_term_in_expr78 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_factor_in_term90 = new BitSet(new long[]{0x00000000000000C2L});
    public static final BitSet FOLLOW_set_in_term94 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_factor_in_term104 = new BitSet(new long[]{0x00000000000000C2L});
    public static final BitSet FOLLOW_NUMBER_in_factor116 = new BitSet(new long[]{0x0000000000000002L});

}