// $ANTLR 3.1.2 C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g 2010-11-08 19:44:52
 package com.easyinsight.calculations.generated; 

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.debug.*;
import java.io.IOException;

import org.antlr.runtime.tree.*;

public class CalculationsParser extends DebugParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "FuncEval", "OpenParen", "CloseParen", "Add", "Subtract", "Multiply", "Divide", "Comma", "Dot", "Exp", "OpenBrace", "CloseBrace", "Quote", "Variable", "Decimal", "String", "UInteger", "Integer", "BracketedVariable", "NoBracketsVariable", "Character", "Digit", "VariableWhitespace", "SpecialChars", "Whitespace", "HideWhiteSpace", "LowerCase", "UpperCase", "VariableSpecialChars", "NoBracketSpecialChars"
    };
    public static final int OpenBrace=14;
    public static final int CloseBrace=15;
    public static final int CloseParen=6;
    public static final int HideWhiteSpace=29;
    public static final int FuncEval=4;
    public static final int Subtract=8;
    public static final int Multiply=9;
    public static final int Exp=13;
    public static final int Decimal=18;
    public static final int Quote=16;
    public static final int BracketedVariable=22;
    public static final int Digit=25;
    public static final int Add=7;
    public static final int EOF=-1;
    public static final int Divide=10;
    public static final int VariableWhitespace=26;
    public static final int Variable=17;
    public static final int OpenParen=5;
    public static final int UpperCase=31;
    public static final int Character=24;
    public static final int Dot=12;
    public static final int LowerCase=30;
    public static final int NoBracketsVariable=23;
    public static final int String=19;
    public static final int Whitespace=28;
    public static final int UInteger=20;
    public static final int VariableSpecialChars=32;
    public static final int Comma=11;
    public static final int SpecialChars=27;
    public static final int Integer=21;
    public static final int NoBracketSpecialChars=33;

    // delegates
    // delegators

    public static final String[] ruleNames = new String[] {
        "invalidRule", "factor", "expr", "parenExpr", "unaryOperator", 
        "symbol", "literal", "term", "startExpr", "exponent", "function"
    };
     
        public int ruleLevel = 0;
        public int getRuleLevel() { return ruleLevel; }
        public void incRuleLevel() { ruleLevel++; }
        public void decRuleLevel() { ruleLevel--; }
        public CalculationsParser(TokenStream input) {
            this(input, DebugEventSocketProxy.DEFAULT_DEBUGGER_PORT, new RecognizerSharedState());
        }
        public CalculationsParser(TokenStream input, int port, RecognizerSharedState state) {
            super(input, state);
            DebugEventSocketProxy proxy =
                new DebugEventSocketProxy(this,port,adaptor);
            setDebugListener(proxy);
            setTokenStream(new DebugTokenStream(input,proxy));
            try {
                proxy.handshake();
            }
            catch (IOException ioe) {
                reportError(ioe);
            }
            TreeAdaptor adap = new CommonTreeAdaptor();
            setTreeAdaptor(adap);
            proxy.setTreeAdaptor(adap);
        }
    public CalculationsParser(TokenStream input, DebugEventListener dbg) {
        super(input, dbg);

         
        TreeAdaptor adap = new CommonTreeAdaptor();
        setTreeAdaptor(adap);

    }
    protected boolean evalPredicate(boolean result, String predicate) {
        dbg.semanticPredicate(result, predicate);
        return result;
    }

    protected DebugTreeAdaptor adaptor;
    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = new DebugTreeAdaptor(dbg,adaptor);

    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }


    public String[] getTokenNames() { return CalculationsParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g"; }


    public static class expr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expr"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:27:1: expr : term ( ( Add | Subtract ) term )* ;
    public final CalculationsParser.expr_return expr() throws RecognitionException {
        CalculationsParser.expr_return retval = new CalculationsParser.expr_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Add2=null;
        Token Subtract3=null;
        CalculationsParser.term_return term1 = null;

        CalculationsParser.term_return term4 = null;


        Object Add2_tree=null;
        Object Subtract3_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "expr");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(27, 1);

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:27:6: ( term ( ( Add | Subtract ) term )* )
            dbg.enterAlt(1);

            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:27:8: term ( ( Add | Subtract ) term )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(27,8);
            pushFollow(FOLLOW_term_in_expr140);
            term1=term();

            state._fsp--;

            adaptor.addChild(root_0, term1.getTree());
            dbg.location(27,13);
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:27:13: ( ( Add | Subtract ) term )*
            try { dbg.enterSubRule(2);

            loop2:
            do {
                int alt2=2;
                try { dbg.enterDecision(2);

                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=Add && LA2_0<=Subtract)) ) {
                    alt2=1;
                }


                } finally {dbg.exitDecision(2);}

                switch (alt2) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:27:14: ( Add | Subtract ) term
            	    {
            	    dbg.location(27,14);
            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:27:14: ( Add | Subtract )
            	    int alt1=2;
            	    try { dbg.enterSubRule(1);
            	    try { dbg.enterDecision(1);

            	    int LA1_0 = input.LA(1);

            	    if ( (LA1_0==Add) ) {
            	        alt1=1;
            	    }
            	    else if ( (LA1_0==Subtract) ) {
            	        alt1=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 1, 0, input);

            	        dbg.recognitionException(nvae);
            	        throw nvae;
            	    }
            	    } finally {dbg.exitDecision(1);}

            	    switch (alt1) {
            	        case 1 :
            	            dbg.enterAlt(1);

            	            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:27:15: Add
            	            {
            	            dbg.location(27,18);
            	            Add2=(Token)match(input,Add,FOLLOW_Add_in_expr144); 
            	            Add2_tree = (Object)adaptor.create(Add2);
            	            root_0 = (Object)adaptor.becomeRoot(Add2_tree, root_0);


            	            }
            	            break;
            	        case 2 :
            	            dbg.enterAlt(2);

            	            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:27:22: Subtract
            	            {
            	            dbg.location(27,30);
            	            Subtract3=(Token)match(input,Subtract,FOLLOW_Subtract_in_expr149); 
            	            Subtract3_tree = (Object)adaptor.create(Subtract3);
            	            root_0 = (Object)adaptor.becomeRoot(Subtract3_tree, root_0);


            	            }
            	            break;

            	    }
            	    } finally {dbg.exitSubRule(1);}

            	    dbg.location(27,33);
            	    pushFollow(FOLLOW_term_in_expr153);
            	    term4=term();

            	    state._fsp--;

            	    adaptor.addChild(root_0, term4.getTree());

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);
            } finally {dbg.exitSubRule(2);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(27, 39);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "expr");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "expr"

    public static class startExpr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "startExpr"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:1: startExpr : expr EOF ;
    public final CalculationsParser.startExpr_return startExpr() throws RecognitionException {
        CalculationsParser.startExpr_return retval = new CalculationsParser.startExpr_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token EOF6=null;
        CalculationsParser.expr_return expr5 = null;


        Object EOF6_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "startExpr");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(28, 1);

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:29:2: ( expr EOF )
            dbg.enterAlt(1);

            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:29:4: expr EOF
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(29,4);
            pushFollow(FOLLOW_expr_in_startExpr163);
            expr5=expr();

            state._fsp--;

            adaptor.addChild(root_0, expr5.getTree());
            dbg.location(29,12);
            EOF6=(Token)match(input,EOF,FOLLOW_EOF_in_startExpr165); 

            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(29, 13);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "startExpr");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "startExpr"

    public static class term_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "term"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:32:1: term : unaryOperator ( ( Multiply | Divide ) unaryOperator )* ;
    public final CalculationsParser.term_return term() throws RecognitionException {
        CalculationsParser.term_return retval = new CalculationsParser.term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Multiply8=null;
        Token Divide9=null;
        CalculationsParser.unaryOperator_return unaryOperator7 = null;

        CalculationsParser.unaryOperator_return unaryOperator10 = null;


        Object Multiply8_tree=null;
        Object Divide9_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "term");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(32, 1);

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:32:6: ( unaryOperator ( ( Multiply | Divide ) unaryOperator )* )
            dbg.enterAlt(1);

            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:32:8: unaryOperator ( ( Multiply | Divide ) unaryOperator )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(32,8);
            pushFollow(FOLLOW_unaryOperator_in_term175);
            unaryOperator7=unaryOperator();

            state._fsp--;

            adaptor.addChild(root_0, unaryOperator7.getTree());
            dbg.location(32,22);
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:32:22: ( ( Multiply | Divide ) unaryOperator )*
            try { dbg.enterSubRule(4);

            loop4:
            do {
                int alt4=2;
                try { dbg.enterDecision(4);

                int LA4_0 = input.LA(1);

                if ( ((LA4_0>=Multiply && LA4_0<=Divide)) ) {
                    alt4=1;
                }


                } finally {dbg.exitDecision(4);}

                switch (alt4) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:32:23: ( Multiply | Divide ) unaryOperator
            	    {
            	    dbg.location(32,23);
            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:32:23: ( Multiply | Divide )
            	    int alt3=2;
            	    try { dbg.enterSubRule(3);
            	    try { dbg.enterDecision(3);

            	    int LA3_0 = input.LA(1);

            	    if ( (LA3_0==Multiply) ) {
            	        alt3=1;
            	    }
            	    else if ( (LA3_0==Divide) ) {
            	        alt3=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 3, 0, input);

            	        dbg.recognitionException(nvae);
            	        throw nvae;
            	    }
            	    } finally {dbg.exitDecision(3);}

            	    switch (alt3) {
            	        case 1 :
            	            dbg.enterAlt(1);

            	            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:32:24: Multiply
            	            {
            	            dbg.location(32,32);
            	            Multiply8=(Token)match(input,Multiply,FOLLOW_Multiply_in_term179); 
            	            Multiply8_tree = (Object)adaptor.create(Multiply8);
            	            root_0 = (Object)adaptor.becomeRoot(Multiply8_tree, root_0);


            	            }
            	            break;
            	        case 2 :
            	            dbg.enterAlt(2);

            	            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:32:36: Divide
            	            {
            	            dbg.location(32,42);
            	            Divide9=(Token)match(input,Divide,FOLLOW_Divide_in_term184); 
            	            Divide9_tree = (Object)adaptor.create(Divide9);
            	            root_0 = (Object)adaptor.becomeRoot(Divide9_tree, root_0);


            	            }
            	            break;

            	    }
            	    } finally {dbg.exitSubRule(3);}

            	    dbg.location(32,45);
            	    pushFollow(FOLLOW_unaryOperator_in_term188);
            	    unaryOperator10=unaryOperator();

            	    state._fsp--;

            	    adaptor.addChild(root_0, unaryOperator10.getTree());

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);
            } finally {dbg.exitSubRule(4);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(32, 60);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "term");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "term"

    public static class unaryOperator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "unaryOperator"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:34:1: unaryOperator : ( Add | Subtract )? exponent ;
    public final CalculationsParser.unaryOperator_return unaryOperator() throws RecognitionException {
        CalculationsParser.unaryOperator_return retval = new CalculationsParser.unaryOperator_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Add11=null;
        Token Subtract12=null;
        CalculationsParser.exponent_return exponent13 = null;


        Object Add11_tree=null;
        Object Subtract12_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "unaryOperator");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(34, 1);

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:35:2: ( ( Add | Subtract )? exponent )
            dbg.enterAlt(1);

            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:35:4: ( Add | Subtract )? exponent
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(35,4);
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:35:4: ( Add | Subtract )?
            int alt5=3;
            try { dbg.enterSubRule(5);
            try { dbg.enterDecision(5);

            int LA5_0 = input.LA(1);

            if ( (LA5_0==Add) ) {
                alt5=1;
            }
            else if ( (LA5_0==Subtract) ) {
                alt5=2;
            }
            } finally {dbg.exitDecision(5);}

            switch (alt5) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:35:5: Add
                    {
                    dbg.location(35,8);
                    Add11=(Token)match(input,Add,FOLLOW_Add_in_unaryOperator200); 
                    Add11_tree = (Object)adaptor.create(Add11);
                    root_0 = (Object)adaptor.becomeRoot(Add11_tree, root_0);


                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:35:12: Subtract
                    {
                    dbg.location(35,20);
                    Subtract12=(Token)match(input,Subtract,FOLLOW_Subtract_in_unaryOperator205); 
                    Subtract12_tree = (Object)adaptor.create(Subtract12);
                    root_0 = (Object)adaptor.becomeRoot(Subtract12_tree, root_0);


                    }
                    break;

            }
            } finally {dbg.exitSubRule(5);}

            dbg.location(35,24);
            pushFollow(FOLLOW_exponent_in_unaryOperator210);
            exponent13=exponent();

            state._fsp--;

            adaptor.addChild(root_0, exponent13.getTree());

            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(35, 32);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "unaryOperator");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "unaryOperator"

    public static class exponent_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "exponent"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:1: exponent : factor ( Exp unaryOperator )? ;
    public final CalculationsParser.exponent_return exponent() throws RecognitionException {
        CalculationsParser.exponent_return retval = new CalculationsParser.exponent_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Exp15=null;
        CalculationsParser.factor_return factor14 = null;

        CalculationsParser.unaryOperator_return unaryOperator16 = null;


        Object Exp15_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "exponent");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(36, 1);

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:9: ( factor ( Exp unaryOperator )? )
            dbg.enterAlt(1);

            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:11: factor ( Exp unaryOperator )?
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(36,11);
            pushFollow(FOLLOW_factor_in_exponent216);
            factor14=factor();

            state._fsp--;

            adaptor.addChild(root_0, factor14.getTree());
            dbg.location(36,18);
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:18: ( Exp unaryOperator )?
            int alt6=2;
            try { dbg.enterSubRule(6);
            try { dbg.enterDecision(6);

            int LA6_0 = input.LA(1);

            if ( (LA6_0==Exp) ) {
                alt6=1;
            }
            } finally {dbg.exitDecision(6);}

            switch (alt6) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:19: Exp unaryOperator
                    {
                    dbg.location(36,22);
                    Exp15=(Token)match(input,Exp,FOLLOW_Exp_in_exponent219); 
                    Exp15_tree = (Object)adaptor.create(Exp15);
                    root_0 = (Object)adaptor.becomeRoot(Exp15_tree, root_0);

                    dbg.location(36,24);
                    pushFollow(FOLLOW_unaryOperator_in_exponent222);
                    unaryOperator16=unaryOperator();

                    state._fsp--;

                    adaptor.addChild(root_0, unaryOperator16.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(6);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(36, 39);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "exponent");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "exponent"

    public static class factor_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "factor"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:38:1: factor : ( symbol | parenExpr );
    public final CalculationsParser.factor_return factor() throws RecognitionException {
        CalculationsParser.factor_return retval = new CalculationsParser.factor_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        CalculationsParser.symbol_return symbol17 = null;

        CalculationsParser.parenExpr_return parenExpr18 = null;



        try { dbg.enterRule(getGrammarFileName(), "factor");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(38, 1);

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:38:8: ( symbol | parenExpr )
            int alt7=2;
            try { dbg.enterDecision(7);

            int LA7_0 = input.LA(1);

            if ( ((LA7_0>=Variable && LA7_0<=String)) ) {
                alt7=1;
            }
            else if ( (LA7_0==OpenParen) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(7);}

            switch (alt7) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:38:10: symbol
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(38,10);
                    pushFollow(FOLLOW_symbol_in_factor232);
                    symbol17=symbol();

                    state._fsp--;

                    adaptor.addChild(root_0, symbol17.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:38:19: parenExpr
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(38,19);
                    pushFollow(FOLLOW_parenExpr_in_factor236);
                    parenExpr18=parenExpr();

                    state._fsp--;

                    adaptor.addChild(root_0, parenExpr18.getTree());

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
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(38, 28);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "factor");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "factor"

    public static class parenExpr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parenExpr"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:40:1: parenExpr : OpenParen expr CloseParen -> expr ;
    public final CalculationsParser.parenExpr_return parenExpr() throws RecognitionException {
        CalculationsParser.parenExpr_return retval = new CalculationsParser.parenExpr_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token OpenParen19=null;
        Token CloseParen21=null;
        CalculationsParser.expr_return expr20 = null;


        Object OpenParen19_tree=null;
        Object CloseParen21_tree=null;
        RewriteRuleTokenStream stream_CloseParen=new RewriteRuleTokenStream(adaptor,"token CloseParen");
        RewriteRuleTokenStream stream_OpenParen=new RewriteRuleTokenStream(adaptor,"token OpenParen");
        RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");
        try { dbg.enterRule(getGrammarFileName(), "parenExpr");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(40, 1);

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:41:2: ( OpenParen expr CloseParen -> expr )
            dbg.enterAlt(1);

            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:41:4: OpenParen expr CloseParen
            {
            dbg.location(41,4);
            OpenParen19=(Token)match(input,OpenParen,FOLLOW_OpenParen_in_parenExpr245);  
            stream_OpenParen.add(OpenParen19);

            dbg.location(41,14);
            pushFollow(FOLLOW_expr_in_parenExpr247);
            expr20=expr();

            state._fsp--;

            stream_expr.add(expr20.getTree());
            dbg.location(41,19);
            CloseParen21=(Token)match(input,CloseParen,FOLLOW_CloseParen_in_parenExpr249);  
            stream_CloseParen.add(CloseParen21);



            // AST REWRITE
            // elements: expr
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 41:30: -> expr
            {
                dbg.location(41,33);
                adaptor.addChild(root_0, stream_expr.nextTree());

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(41, 37);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "parenExpr");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "parenExpr"

    public static class symbol_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "symbol"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:42:1: symbol : ( literal | Variable | function );
    public final CalculationsParser.symbol_return symbol() throws RecognitionException {
        CalculationsParser.symbol_return retval = new CalculationsParser.symbol_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Variable23=null;
        CalculationsParser.literal_return literal22 = null;

        CalculationsParser.function_return function24 = null;


        Object Variable23_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "symbol");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(42, 1);

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:42:8: ( literal | Variable | function )
            int alt8=3;
            try { dbg.enterDecision(8);

            int LA8_0 = input.LA(1);

            if ( ((LA8_0>=Decimal && LA8_0<=String)) ) {
                alt8=1;
            }
            else if ( (LA8_0==Variable) ) {
                int LA8_2 = input.LA(2);

                if ( (LA8_2==OpenParen) ) {
                    alt8=3;
                }
                else if ( (LA8_2==EOF||(LA8_2>=CloseParen && LA8_2<=Comma)||LA8_2==Exp) ) {
                    alt8=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 2, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(8);}

            switch (alt8) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:42:10: literal
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(42,10);
                    pushFollow(FOLLOW_literal_in_symbol260);
                    literal22=literal();

                    state._fsp--;

                    adaptor.addChild(root_0, literal22.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:42:20: Variable
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(42,20);
                    Variable23=(Token)match(input,Variable,FOLLOW_Variable_in_symbol264); 
                    Variable23_tree = (Object)adaptor.create(Variable23);
                    adaptor.addChild(root_0, Variable23_tree);


                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:42:31: function
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(42,31);
                    pushFollow(FOLLOW_function_in_symbol268);
                    function24=function();

                    state._fsp--;

                    adaptor.addChild(root_0, function24.getTree());

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
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(42, 39);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "symbol");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "symbol"

    public static class function_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "function"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:43:1: function : Variable OpenParen ( expr ( Comma expr )* )? CloseParen -> ^( FuncEval Variable ( expr ( expr )* )? ) ;
    public final CalculationsParser.function_return function() throws RecognitionException {
        CalculationsParser.function_return retval = new CalculationsParser.function_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Variable25=null;
        Token OpenParen26=null;
        Token Comma28=null;
        Token CloseParen30=null;
        CalculationsParser.expr_return expr27 = null;

        CalculationsParser.expr_return expr29 = null;


        Object Variable25_tree=null;
        Object OpenParen26_tree=null;
        Object Comma28_tree=null;
        Object CloseParen30_tree=null;
        RewriteRuleTokenStream stream_Variable=new RewriteRuleTokenStream(adaptor,"token Variable");
        RewriteRuleTokenStream stream_CloseParen=new RewriteRuleTokenStream(adaptor,"token CloseParen");
        RewriteRuleTokenStream stream_OpenParen=new RewriteRuleTokenStream(adaptor,"token OpenParen");
        RewriteRuleTokenStream stream_Comma=new RewriteRuleTokenStream(adaptor,"token Comma");
        RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");
        try { dbg.enterRule(getGrammarFileName(), "function");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(43, 1);

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:43:9: ( Variable OpenParen ( expr ( Comma expr )* )? CloseParen -> ^( FuncEval Variable ( expr ( expr )* )? ) )
            dbg.enterAlt(1);

            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:43:11: Variable OpenParen ( expr ( Comma expr )* )? CloseParen
            {
            dbg.location(43,11);
            Variable25=(Token)match(input,Variable,FOLLOW_Variable_in_function274);  
            stream_Variable.add(Variable25);

            dbg.location(43,20);
            OpenParen26=(Token)match(input,OpenParen,FOLLOW_OpenParen_in_function276);  
            stream_OpenParen.add(OpenParen26);

            dbg.location(43,30);
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:43:30: ( expr ( Comma expr )* )?
            int alt10=2;
            try { dbg.enterSubRule(10);
            try { dbg.enterDecision(10);

            int LA10_0 = input.LA(1);

            if ( (LA10_0==OpenParen||(LA10_0>=Add && LA10_0<=Subtract)||(LA10_0>=Variable && LA10_0<=String)) ) {
                alt10=1;
            }
            } finally {dbg.exitDecision(10);}

            switch (alt10) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:43:31: expr ( Comma expr )*
                    {
                    dbg.location(43,31);
                    pushFollow(FOLLOW_expr_in_function279);
                    expr27=expr();

                    state._fsp--;

                    stream_expr.add(expr27.getTree());
                    dbg.location(43,36);
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:43:36: ( Comma expr )*
                    try { dbg.enterSubRule(9);

                    loop9:
                    do {
                        int alt9=2;
                        try { dbg.enterDecision(9);

                        int LA9_0 = input.LA(1);

                        if ( (LA9_0==Comma) ) {
                            alt9=1;
                        }


                        } finally {dbg.exitDecision(9);}

                        switch (alt9) {
                    	case 1 :
                    	    dbg.enterAlt(1);

                    	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:43:37: Comma expr
                    	    {
                    	    dbg.location(43,37);
                    	    Comma28=(Token)match(input,Comma,FOLLOW_Comma_in_function282);  
                    	    stream_Comma.add(Comma28);

                    	    dbg.location(43,43);
                    	    pushFollow(FOLLOW_expr_in_function284);
                    	    expr29=expr();

                    	    state._fsp--;

                    	    stream_expr.add(expr29.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop9;
                        }
                    } while (true);
                    } finally {dbg.exitSubRule(9);}


                    }
                    break;

            }
            } finally {dbg.exitSubRule(10);}

            dbg.location(43,52);
            CloseParen30=(Token)match(input,CloseParen,FOLLOW_CloseParen_in_function290);  
            stream_CloseParen.add(CloseParen30);



            // AST REWRITE
            // elements: Variable, expr, expr
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 43:63: -> ^( FuncEval Variable ( expr ( expr )* )? )
            {
                dbg.location(43,66);
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:43:66: ^( FuncEval Variable ( expr ( expr )* )? )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(43,68);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FuncEval, "FuncEval"), root_1);

                dbg.location(43,77);
                adaptor.addChild(root_1, stream_Variable.nextNode());
                dbg.location(43,86);
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:43:86: ( expr ( expr )* )?
                if ( stream_expr.hasNext()||stream_expr.hasNext() ) {
                    dbg.location(43,87);
                    adaptor.addChild(root_1, stream_expr.nextTree());
                    dbg.location(43,92);
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:43:92: ( expr )*
                    while ( stream_expr.hasNext() ) {
                        dbg.location(43,93);
                        adaptor.addChild(root_1, stream_expr.nextTree());

                    }
                    stream_expr.reset();

                }
                stream_expr.reset();
                stream_expr.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(43, 102);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "function");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "function"

    public static class literal_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "literal"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:47:1: literal : ( Decimal | String );
    public final CalculationsParser.literal_return literal() throws RecognitionException {
        CalculationsParser.literal_return retval = new CalculationsParser.literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set31=null;

        Object set31_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "literal");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(47, 1);

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:47:9: ( Decimal | String )
            dbg.enterAlt(1);

            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(47,9);
            set31=(Token)input.LT(1);
            if ( (input.LA(1)>=Decimal && input.LA(1)<=String) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set31));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(47, 27);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "literal");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "literal"

    // Delegated rules


 

    public static final BitSet FOLLOW_term_in_expr140 = new BitSet(new long[]{0x0000000000000182L});
    public static final BitSet FOLLOW_Add_in_expr144 = new BitSet(new long[]{0x00000000000E01A0L});
    public static final BitSet FOLLOW_Subtract_in_expr149 = new BitSet(new long[]{0x00000000000E01A0L});
    public static final BitSet FOLLOW_term_in_expr153 = new BitSet(new long[]{0x0000000000000182L});
    public static final BitSet FOLLOW_expr_in_startExpr163 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_startExpr165 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryOperator_in_term175 = new BitSet(new long[]{0x0000000000000602L});
    public static final BitSet FOLLOW_Multiply_in_term179 = new BitSet(new long[]{0x00000000000E01A0L});
    public static final BitSet FOLLOW_Divide_in_term184 = new BitSet(new long[]{0x00000000000E01A0L});
    public static final BitSet FOLLOW_unaryOperator_in_term188 = new BitSet(new long[]{0x0000000000000602L});
    public static final BitSet FOLLOW_Add_in_unaryOperator200 = new BitSet(new long[]{0x00000000000E01A0L});
    public static final BitSet FOLLOW_Subtract_in_unaryOperator205 = new BitSet(new long[]{0x00000000000E01A0L});
    public static final BitSet FOLLOW_exponent_in_unaryOperator210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_factor_in_exponent216 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_Exp_in_exponent219 = new BitSet(new long[]{0x00000000000E01A0L});
    public static final BitSet FOLLOW_unaryOperator_in_exponent222 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_symbol_in_factor232 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parenExpr_in_factor236 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OpenParen_in_parenExpr245 = new BitSet(new long[]{0x00000000000E01A0L});
    public static final BitSet FOLLOW_expr_in_parenExpr247 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_CloseParen_in_parenExpr249 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_symbol260 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Variable_in_symbol264 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_symbol268 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Variable_in_function274 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_OpenParen_in_function276 = new BitSet(new long[]{0x00000000000E01E0L});
    public static final BitSet FOLLOW_expr_in_function279 = new BitSet(new long[]{0x0000000000000840L});
    public static final BitSet FOLLOW_Comma_in_function282 = new BitSet(new long[]{0x00000000000E01A0L});
    public static final BitSet FOLLOW_expr_in_function284 = new BitSet(new long[]{0x0000000000000840L});
    public static final BitSet FOLLOW_CloseParen_in_function290 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_literal0 = new BitSet(new long[]{0x0000000000000002L});

}