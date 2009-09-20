// $ANTLR 3.1.2 C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g 2009-09-20 11:56:40
 package com.easyinsight.calculations.generated; 

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class CalculationsParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "FuncEval", "OpenParen", "CloseParen", "Add", "Subtract", "Multiply", "Divide", "Comma", "Dot", "Exp", "OpenBrace", "CloseBrace", "Decimal", "Variable", "UInteger", "Integer", "BracketedVariable", "NoBracketsVariable", "Whitespace", "HideWhiteSpace", "Digit", "VariableWhitespace", "LowerCase", "UpperCase", "Character", "SpecialChars", "NoBracketSpecialChars"
    };
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
    public static final int Add=7;
    public static final int EOF=-1;
    public static final int Divide=10;
    public static final int VariableWhitespace=25;
    public static final int Variable=17;
    public static final int OpenParen=5;
    public static final int UpperCase=27;
    public static final int Character=28;
    public static final int Dot=12;
    public static final int LowerCase=26;
    public static final int NoBracketsVariable=21;
    public static final int Whitespace=22;
    public static final int UInteger=18;
    public static final int Comma=11;
    public static final int SpecialChars=29;
    public static final int Integer=19;
    public static final int NoBracketSpecialChars=30;

    // delegates
    // delegators


        public CalculationsParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public CalculationsParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return CalculationsParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g"; }


    public static class startExpr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "startExpr"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:25:1: startExpr : expr EOF ;
    public final CalculationsParser.startExpr_return startExpr() throws RecognitionException {
        CalculationsParser.startExpr_return retval = new CalculationsParser.startExpr_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token EOF2=null;
        CalculationsParser.expr_return expr1 = null;


        Object EOF2_tree=null;

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:26:2: ( expr EOF )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:26:4: expr EOF
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_expr_in_startExpr132);
            expr1=expr();

            state._fsp--;

            adaptor.addChild(root_0, expr1.getTree());
            EOF2=(Token)match(input,EOF,FOLLOW_EOF_in_startExpr134); 

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
        return retval;
    }
    // $ANTLR end "startExpr"

    public static class expr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expr"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:1: expr : term ( ( Add | Subtract ) term )* ;
    public final CalculationsParser.expr_return expr() throws RecognitionException {
        CalculationsParser.expr_return retval = new CalculationsParser.expr_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Add4=null;
        Token Subtract5=null;
        CalculationsParser.term_return term3 = null;

        CalculationsParser.term_return term6 = null;


        Object Add4_tree=null;
        Object Subtract5_tree=null;

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:6: ( term ( ( Add | Subtract ) term )* )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:8: term ( ( Add | Subtract ) term )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_term_in_expr143);
            term3=term();

            state._fsp--;

            adaptor.addChild(root_0, term3.getTree());
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:13: ( ( Add | Subtract ) term )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=Add && LA2_0<=Subtract)) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:14: ( Add | Subtract ) term
            	    {
            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:14: ( Add | Subtract )
            	    int alt1=2;
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

            	        throw nvae;
            	    }
            	    switch (alt1) {
            	        case 1 :
            	            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:15: Add
            	            {
            	            Add4=(Token)match(input,Add,FOLLOW_Add_in_expr147); 
            	            Add4_tree = (Object)adaptor.create(Add4);
            	            root_0 = (Object)adaptor.becomeRoot(Add4_tree, root_0);


            	            }
            	            break;
            	        case 2 :
            	            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:22: Subtract
            	            {
            	            Subtract5=(Token)match(input,Subtract,FOLLOW_Subtract_in_expr152); 
            	            Subtract5_tree = (Object)adaptor.create(Subtract5);
            	            root_0 = (Object)adaptor.becomeRoot(Subtract5_tree, root_0);


            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_term_in_expr156);
            	    term6=term();

            	    state._fsp--;

            	    adaptor.addChild(root_0, term6.getTree());

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
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "expr"

    public static class term_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "term"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:30:1: term : unaryOperator ( ( Multiply | Divide ) unaryOperator )* ;
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

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:30:6: ( unaryOperator ( ( Multiply | Divide ) unaryOperator )* )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:30:8: unaryOperator ( ( Multiply | Divide ) unaryOperator )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_unaryOperator_in_term166);
            unaryOperator7=unaryOperator();

            state._fsp--;

            adaptor.addChild(root_0, unaryOperator7.getTree());
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:30:22: ( ( Multiply | Divide ) unaryOperator )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>=Multiply && LA4_0<=Divide)) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:30:23: ( Multiply | Divide ) unaryOperator
            	    {
            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:30:23: ( Multiply | Divide )
            	    int alt3=2;
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

            	        throw nvae;
            	    }
            	    switch (alt3) {
            	        case 1 :
            	            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:30:24: Multiply
            	            {
            	            Multiply8=(Token)match(input,Multiply,FOLLOW_Multiply_in_term170); 
            	            Multiply8_tree = (Object)adaptor.create(Multiply8);
            	            root_0 = (Object)adaptor.becomeRoot(Multiply8_tree, root_0);


            	            }
            	            break;
            	        case 2 :
            	            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:30:36: Divide
            	            {
            	            Divide9=(Token)match(input,Divide,FOLLOW_Divide_in_term175); 
            	            Divide9_tree = (Object)adaptor.create(Divide9);
            	            root_0 = (Object)adaptor.becomeRoot(Divide9_tree, root_0);


            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_unaryOperator_in_term179);
            	    unaryOperator10=unaryOperator();

            	    state._fsp--;

            	    adaptor.addChild(root_0, unaryOperator10.getTree());

            	    }
            	    break;

            	default :
            	    break loop4;
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
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "term"

    public static class unaryOperator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "unaryOperator"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:32:1: unaryOperator : ( Add | Subtract )? exponent ;
    public final CalculationsParser.unaryOperator_return unaryOperator() throws RecognitionException {
        CalculationsParser.unaryOperator_return retval = new CalculationsParser.unaryOperator_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Add11=null;
        Token Subtract12=null;
        CalculationsParser.exponent_return exponent13 = null;


        Object Add11_tree=null;
        Object Subtract12_tree=null;

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:33:2: ( ( Add | Subtract )? exponent )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:33:4: ( Add | Subtract )? exponent
            {
            root_0 = (Object)adaptor.nil();

            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:33:4: ( Add | Subtract )?
            int alt5=3;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==Add) ) {
                alt5=1;
            }
            else if ( (LA5_0==Subtract) ) {
                alt5=2;
            }
            switch (alt5) {
                case 1 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:33:5: Add
                    {
                    Add11=(Token)match(input,Add,FOLLOW_Add_in_unaryOperator191); 
                    Add11_tree = (Object)adaptor.create(Add11);
                    root_0 = (Object)adaptor.becomeRoot(Add11_tree, root_0);


                    }
                    break;
                case 2 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:33:12: Subtract
                    {
                    Subtract12=(Token)match(input,Subtract,FOLLOW_Subtract_in_unaryOperator196); 
                    Subtract12_tree = (Object)adaptor.create(Subtract12);
                    root_0 = (Object)adaptor.becomeRoot(Subtract12_tree, root_0);


                    }
                    break;

            }

            pushFollow(FOLLOW_exponent_in_unaryOperator201);
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
        return retval;
    }
    // $ANTLR end "unaryOperator"

    public static class exponent_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "exponent"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:34:1: exponent : factor ( Exp unaryOperator )? ;
    public final CalculationsParser.exponent_return exponent() throws RecognitionException {
        CalculationsParser.exponent_return retval = new CalculationsParser.exponent_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Exp15=null;
        CalculationsParser.factor_return factor14 = null;

        CalculationsParser.unaryOperator_return unaryOperator16 = null;


        Object Exp15_tree=null;

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:34:9: ( factor ( Exp unaryOperator )? )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:34:11: factor ( Exp unaryOperator )?
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_factor_in_exponent207);
            factor14=factor();

            state._fsp--;

            adaptor.addChild(root_0, factor14.getTree());
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:34:18: ( Exp unaryOperator )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==Exp) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:34:19: Exp unaryOperator
                    {
                    Exp15=(Token)match(input,Exp,FOLLOW_Exp_in_exponent210); 
                    Exp15_tree = (Object)adaptor.create(Exp15);
                    root_0 = (Object)adaptor.becomeRoot(Exp15_tree, root_0);

                    pushFollow(FOLLOW_unaryOperator_in_exponent213);
                    unaryOperator16=unaryOperator();

                    state._fsp--;

                    adaptor.addChild(root_0, unaryOperator16.getTree());

                    }
                    break;

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
        return retval;
    }
    // $ANTLR end "exponent"

    public static class factor_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "factor"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:1: factor : ( symbol | parenExpr );
    public final CalculationsParser.factor_return factor() throws RecognitionException {
        CalculationsParser.factor_return retval = new CalculationsParser.factor_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        CalculationsParser.symbol_return symbol17 = null;

        CalculationsParser.parenExpr_return parenExpr18 = null;



        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:8: ( symbol | parenExpr )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( ((LA7_0>=Decimal && LA7_0<=Variable)) ) {
                alt7=1;
            }
            else if ( (LA7_0==OpenParen) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:10: symbol
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_symbol_in_factor223);
                    symbol17=symbol();

                    state._fsp--;

                    adaptor.addChild(root_0, symbol17.getTree());

                    }
                    break;
                case 2 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:19: parenExpr
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_parenExpr_in_factor227);
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
        return retval;
    }
    // $ANTLR end "factor"

    public static class parenExpr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parenExpr"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:38:1: parenExpr : OpenParen expr CloseParen -> expr ;
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
        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:39:2: ( OpenParen expr CloseParen -> expr )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:39:4: OpenParen expr CloseParen
            {
            OpenParen19=(Token)match(input,OpenParen,FOLLOW_OpenParen_in_parenExpr236);  
            stream_OpenParen.add(OpenParen19);

            pushFollow(FOLLOW_expr_in_parenExpr238);
            expr20=expr();

            state._fsp--;

            stream_expr.add(expr20.getTree());
            CloseParen21=(Token)match(input,CloseParen,FOLLOW_CloseParen_in_parenExpr240);  
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
            // 39:30: -> expr
            {
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
        return retval;
    }
    // $ANTLR end "parenExpr"

    public static class symbol_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "symbol"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:40:1: symbol : ( Decimal | Variable | function );
    public final CalculationsParser.symbol_return symbol() throws RecognitionException {
        CalculationsParser.symbol_return retval = new CalculationsParser.symbol_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Decimal22=null;
        Token Variable23=null;
        CalculationsParser.function_return function24 = null;


        Object Decimal22_tree=null;
        Object Variable23_tree=null;

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:40:8: ( Decimal | Variable | function )
            int alt8=3;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==Decimal) ) {
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

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:40:10: Decimal
                    {
                    root_0 = (Object)adaptor.nil();

                    Decimal22=(Token)match(input,Decimal,FOLLOW_Decimal_in_symbol251); 
                    Decimal22_tree = (Object)adaptor.create(Decimal22);
                    adaptor.addChild(root_0, Decimal22_tree);


                    }
                    break;
                case 2 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:40:20: Variable
                    {
                    root_0 = (Object)adaptor.nil();

                    Variable23=(Token)match(input,Variable,FOLLOW_Variable_in_symbol255); 
                    Variable23_tree = (Object)adaptor.create(Variable23);
                    adaptor.addChild(root_0, Variable23_tree);


                    }
                    break;
                case 3 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:40:31: function
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_function_in_symbol259);
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
        return retval;
    }
    // $ANTLR end "symbol"

    public static class function_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "function"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:41:1: function : Variable OpenParen ( expr ( Comma expr )* )? CloseParen -> ^( FuncEval Variable ( expr ( expr )* )? ) ;
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
        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:41:9: ( Variable OpenParen ( expr ( Comma expr )* )? CloseParen -> ^( FuncEval Variable ( expr ( expr )* )? ) )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:41:11: Variable OpenParen ( expr ( Comma expr )* )? CloseParen
            {
            Variable25=(Token)match(input,Variable,FOLLOW_Variable_in_function265);  
            stream_Variable.add(Variable25);

            OpenParen26=(Token)match(input,OpenParen,FOLLOW_OpenParen_in_function267);  
            stream_OpenParen.add(OpenParen26);

            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:41:30: ( expr ( Comma expr )* )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==OpenParen||(LA10_0>=Add && LA10_0<=Subtract)||(LA10_0>=Decimal && LA10_0<=Variable)) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:41:31: expr ( Comma expr )*
                    {
                    pushFollow(FOLLOW_expr_in_function270);
                    expr27=expr();

                    state._fsp--;

                    stream_expr.add(expr27.getTree());
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:41:36: ( Comma expr )*
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( (LA9_0==Comma) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:41:37: Comma expr
                    	    {
                    	    Comma28=(Token)match(input,Comma,FOLLOW_Comma_in_function273);  
                    	    stream_Comma.add(Comma28);

                    	    pushFollow(FOLLOW_expr_in_function275);
                    	    expr29=expr();

                    	    state._fsp--;

                    	    stream_expr.add(expr29.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop9;
                        }
                    } while (true);


                    }
                    break;

            }

            CloseParen30=(Token)match(input,CloseParen,FOLLOW_CloseParen_in_function281);  
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
            // 41:63: -> ^( FuncEval Variable ( expr ( expr )* )? )
            {
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:41:66: ^( FuncEval Variable ( expr ( expr )* )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FuncEval, "FuncEval"), root_1);

                adaptor.addChild(root_1, stream_Variable.nextNode());
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:41:86: ( expr ( expr )* )?
                if ( stream_expr.hasNext()||stream_expr.hasNext() ) {
                    adaptor.addChild(root_1, stream_expr.nextTree());
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:41:92: ( expr )*
                    while ( stream_expr.hasNext() ) {
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
        return retval;
    }
    // $ANTLR end "function"

    // Delegated rules


 

    public static final BitSet FOLLOW_expr_in_startExpr132 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_startExpr134 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_expr143 = new BitSet(new long[]{0x0000000000000182L});
    public static final BitSet FOLLOW_Add_in_expr147 = new BitSet(new long[]{0x00000000000301A0L});
    public static final BitSet FOLLOW_Subtract_in_expr152 = new BitSet(new long[]{0x00000000000301A0L});
    public static final BitSet FOLLOW_term_in_expr156 = new BitSet(new long[]{0x0000000000000182L});
    public static final BitSet FOLLOW_unaryOperator_in_term166 = new BitSet(new long[]{0x0000000000000602L});
    public static final BitSet FOLLOW_Multiply_in_term170 = new BitSet(new long[]{0x00000000000301A0L});
    public static final BitSet FOLLOW_Divide_in_term175 = new BitSet(new long[]{0x00000000000301A0L});
    public static final BitSet FOLLOW_unaryOperator_in_term179 = new BitSet(new long[]{0x0000000000000602L});
    public static final BitSet FOLLOW_Add_in_unaryOperator191 = new BitSet(new long[]{0x00000000000301A0L});
    public static final BitSet FOLLOW_Subtract_in_unaryOperator196 = new BitSet(new long[]{0x00000000000301A0L});
    public static final BitSet FOLLOW_exponent_in_unaryOperator201 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_factor_in_exponent207 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_Exp_in_exponent210 = new BitSet(new long[]{0x00000000000301A0L});
    public static final BitSet FOLLOW_unaryOperator_in_exponent213 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_symbol_in_factor223 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parenExpr_in_factor227 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OpenParen_in_parenExpr236 = new BitSet(new long[]{0x00000000000301A0L});
    public static final BitSet FOLLOW_expr_in_parenExpr238 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_CloseParen_in_parenExpr240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Decimal_in_symbol251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Variable_in_symbol255 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_symbol259 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Variable_in_function265 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_OpenParen_in_function267 = new BitSet(new long[]{0x00000000000301E0L});
    public static final BitSet FOLLOW_expr_in_function270 = new BitSet(new long[]{0x0000000000000840L});
    public static final BitSet FOLLOW_Comma_in_function273 = new BitSet(new long[]{0x00000000000301A0L});
    public static final BitSet FOLLOW_expr_in_function275 = new BitSet(new long[]{0x0000000000000840L});
    public static final BitSet FOLLOW_CloseParen_in_function281 = new BitSet(new long[]{0x0000000000000002L});

}