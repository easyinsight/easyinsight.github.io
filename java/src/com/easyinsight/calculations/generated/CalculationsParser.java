// $ANTLR 3.1.2 C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g 2009-03-30 17:46:54
 package com.easyinsight.calculations.generated; 

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class CalculationsParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "FuncEval", "OpenParen", "CloseParen", "Add", "Subtract", "Multiply", "Divide", "Comma", "Dot", "Exp", "Decimal", "Variable", "UInteger", "Integer", "Character", "Digit", "SpecialChars", "VariableWhitespace", "Whitespace", "HideWhiteSpace", "LowerCase", "UpperCase"
    };
    public static final int CloseParen=6;
    public static final int HideWhiteSpace=23;
    public static final int FuncEval=4;
    public static final int Subtract=8;
    public static final int Multiply=9;
    public static final int Exp=13;
    public static final int Decimal=14;
    public static final int Digit=19;
    public static final int Add=7;
    public static final int EOF=-1;
    public static final int Divide=10;
    public static final int VariableWhitespace=21;
    public static final int Variable=15;
    public static final int OpenParen=5;
    public static final int UpperCase=25;
    public static final int Character=18;
    public static final int Dot=12;
    public static final int LowerCase=24;
    public static final int Whitespace=22;
    public static final int UInteger=16;
    public static final int Comma=11;
    public static final int SpecialChars=20;
    public static final int Integer=17;

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


    public static class expr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expr"
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:23:1: expr : term ( ( Add | Subtract ) term )* EOF ;
    public final CalculationsParser.expr_return expr() throws RecognitionException {
        CalculationsParser.expr_return retval = new CalculationsParser.expr_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Add2=null;
        Token Subtract3=null;
        Token EOF5=null;
        CalculationsParser.term_return term1 = null;

        CalculationsParser.term_return term4 = null;


        Object Add2_tree=null;
        Object Subtract3_tree=null;
        Object EOF5_tree=null;

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:23:6: ( term ( ( Add | Subtract ) term )* EOF )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:23:8: term ( ( Add | Subtract ) term )* EOF
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_term_in_expr115);
            term1=term();

            state._fsp--;

            adaptor.addChild(root_0, term1.getTree());
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:23:13: ( ( Add | Subtract ) term )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=Add && LA2_0<=Subtract)) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:23:14: ( Add | Subtract ) term
            	    {
            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:23:14: ( Add | Subtract )
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
            	            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:23:15: Add
            	            {
            	            Add2=(Token)match(input,Add,FOLLOW_Add_in_expr119); 
            	            Add2_tree = (Object)adaptor.create(Add2);
            	            root_0 = (Object)adaptor.becomeRoot(Add2_tree, root_0);


            	            }
            	            break;
            	        case 2 :
            	            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:23:22: Subtract
            	            {
            	            Subtract3=(Token)match(input,Subtract,FOLLOW_Subtract_in_expr124); 
            	            Subtract3_tree = (Object)adaptor.create(Subtract3);
            	            root_0 = (Object)adaptor.becomeRoot(Subtract3_tree, root_0);


            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_term_in_expr128);
            	    term4=term();

            	    state._fsp--;

            	    adaptor.addChild(root_0, term4.getTree());

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            EOF5=(Token)match(input,EOF,FOLLOW_EOF_in_expr132); 

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
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:25:1: term : unaryOperator ( ( Multiply | Divide ) unaryOperator )* ;
    public final CalculationsParser.term_return term() throws RecognitionException {
        CalculationsParser.term_return retval = new CalculationsParser.term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Multiply7=null;
        Token Divide8=null;
        CalculationsParser.unaryOperator_return unaryOperator6 = null;

        CalculationsParser.unaryOperator_return unaryOperator9 = null;


        Object Multiply7_tree=null;
        Object Divide8_tree=null;

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:25:6: ( unaryOperator ( ( Multiply | Divide ) unaryOperator )* )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:25:8: unaryOperator ( ( Multiply | Divide ) unaryOperator )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_unaryOperator_in_term141);
            unaryOperator6=unaryOperator();

            state._fsp--;

            adaptor.addChild(root_0, unaryOperator6.getTree());
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:25:22: ( ( Multiply | Divide ) unaryOperator )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>=Multiply && LA4_0<=Divide)) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:25:23: ( Multiply | Divide ) unaryOperator
            	    {
            	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:25:23: ( Multiply | Divide )
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
            	            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:25:24: Multiply
            	            {
            	            Multiply7=(Token)match(input,Multiply,FOLLOW_Multiply_in_term145); 
            	            Multiply7_tree = (Object)adaptor.create(Multiply7);
            	            root_0 = (Object)adaptor.becomeRoot(Multiply7_tree, root_0);


            	            }
            	            break;
            	        case 2 :
            	            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:25:36: Divide
            	            {
            	            Divide8=(Token)match(input,Divide,FOLLOW_Divide_in_term150); 
            	            Divide8_tree = (Object)adaptor.create(Divide8);
            	            root_0 = (Object)adaptor.becomeRoot(Divide8_tree, root_0);


            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_unaryOperator_in_term154);
            	    unaryOperator9=unaryOperator();

            	    state._fsp--;

            	    adaptor.addChild(root_0, unaryOperator9.getTree());

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
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:27:1: unaryOperator : ( Add | Subtract )? exponent ;
    public final CalculationsParser.unaryOperator_return unaryOperator() throws RecognitionException {
        CalculationsParser.unaryOperator_return retval = new CalculationsParser.unaryOperator_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Add10=null;
        Token Subtract11=null;
        CalculationsParser.exponent_return exponent12 = null;


        Object Add10_tree=null;
        Object Subtract11_tree=null;

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:2: ( ( Add | Subtract )? exponent )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:4: ( Add | Subtract )? exponent
            {
            root_0 = (Object)adaptor.nil();

            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:4: ( Add | Subtract )?
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
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:5: Add
                    {
                    Add10=(Token)match(input,Add,FOLLOW_Add_in_unaryOperator166); 
                    Add10_tree = (Object)adaptor.create(Add10);
                    root_0 = (Object)adaptor.becomeRoot(Add10_tree, root_0);


                    }
                    break;
                case 2 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:12: Subtract
                    {
                    Subtract11=(Token)match(input,Subtract,FOLLOW_Subtract_in_unaryOperator171); 
                    Subtract11_tree = (Object)adaptor.create(Subtract11);
                    root_0 = (Object)adaptor.becomeRoot(Subtract11_tree, root_0);


                    }
                    break;

            }

            pushFollow(FOLLOW_exponent_in_unaryOperator176);
            exponent12=exponent();

            state._fsp--;

            adaptor.addChild(root_0, exponent12.getTree());

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
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:29:1: exponent : factor ( Exp unaryOperator )? ;
    public final CalculationsParser.exponent_return exponent() throws RecognitionException {
        CalculationsParser.exponent_return retval = new CalculationsParser.exponent_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Exp14=null;
        CalculationsParser.factor_return factor13 = null;

        CalculationsParser.unaryOperator_return unaryOperator15 = null;


        Object Exp14_tree=null;

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:29:9: ( factor ( Exp unaryOperator )? )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:29:11: factor ( Exp unaryOperator )?
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_factor_in_exponent182);
            factor13=factor();

            state._fsp--;

            adaptor.addChild(root_0, factor13.getTree());
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:29:18: ( Exp unaryOperator )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==Exp) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:29:19: Exp unaryOperator
                    {
                    Exp14=(Token)match(input,Exp,FOLLOW_Exp_in_exponent185); 
                    Exp14_tree = (Object)adaptor.create(Exp14);
                    root_0 = (Object)adaptor.becomeRoot(Exp14_tree, root_0);

                    pushFollow(FOLLOW_unaryOperator_in_exponent188);
                    unaryOperator15=unaryOperator();

                    state._fsp--;

                    adaptor.addChild(root_0, unaryOperator15.getTree());

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
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:31:1: factor : ( symbol | parenExpr );
    public final CalculationsParser.factor_return factor() throws RecognitionException {
        CalculationsParser.factor_return retval = new CalculationsParser.factor_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        CalculationsParser.symbol_return symbol16 = null;

        CalculationsParser.parenExpr_return parenExpr17 = null;



        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:31:8: ( symbol | parenExpr )
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
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:31:10: symbol
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_symbol_in_factor198);
                    symbol16=symbol();

                    state._fsp--;

                    adaptor.addChild(root_0, symbol16.getTree());

                    }
                    break;
                case 2 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:31:19: parenExpr
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_parenExpr_in_factor202);
                    parenExpr17=parenExpr();

                    state._fsp--;

                    adaptor.addChild(root_0, parenExpr17.getTree());

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
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:33:1: parenExpr : OpenParen expr CloseParen -> expr ;
    public final CalculationsParser.parenExpr_return parenExpr() throws RecognitionException {
        CalculationsParser.parenExpr_return retval = new CalculationsParser.parenExpr_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token OpenParen18=null;
        Token CloseParen20=null;
        CalculationsParser.expr_return expr19 = null;


        Object OpenParen18_tree=null;
        Object CloseParen20_tree=null;
        RewriteRuleTokenStream stream_CloseParen=new RewriteRuleTokenStream(adaptor,"token CloseParen");
        RewriteRuleTokenStream stream_OpenParen=new RewriteRuleTokenStream(adaptor,"token OpenParen");
        RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");
        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:34:2: ( OpenParen expr CloseParen -> expr )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:34:4: OpenParen expr CloseParen
            {
            OpenParen18=(Token)match(input,OpenParen,FOLLOW_OpenParen_in_parenExpr211);  
            stream_OpenParen.add(OpenParen18);

            pushFollow(FOLLOW_expr_in_parenExpr213);
            expr19=expr();

            state._fsp--;

            stream_expr.add(expr19.getTree());
            CloseParen20=(Token)match(input,CloseParen,FOLLOW_CloseParen_in_parenExpr215);  
            stream_CloseParen.add(CloseParen20);



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
            // 34:30: -> expr
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
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:35:1: symbol : ( Decimal | Variable | function );
    public final CalculationsParser.symbol_return symbol() throws RecognitionException {
        CalculationsParser.symbol_return retval = new CalculationsParser.symbol_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Decimal21=null;
        Token Variable22=null;
        CalculationsParser.function_return function23 = null;


        Object Decimal21_tree=null;
        Object Variable22_tree=null;

        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:35:8: ( Decimal | Variable | function )
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
                else if ( (LA8_2==EOF||(LA8_2>=Add && LA8_2<=Divide)||LA8_2==Exp) ) {
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
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:35:10: Decimal
                    {
                    root_0 = (Object)adaptor.nil();

                    Decimal21=(Token)match(input,Decimal,FOLLOW_Decimal_in_symbol226); 
                    Decimal21_tree = (Object)adaptor.create(Decimal21);
                    adaptor.addChild(root_0, Decimal21_tree);


                    }
                    break;
                case 2 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:35:20: Variable
                    {
                    root_0 = (Object)adaptor.nil();

                    Variable22=(Token)match(input,Variable,FOLLOW_Variable_in_symbol230); 
                    Variable22_tree = (Object)adaptor.create(Variable22);
                    adaptor.addChild(root_0, Variable22_tree);


                    }
                    break;
                case 3 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:35:31: function
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_function_in_symbol234);
                    function23=function();

                    state._fsp--;

                    adaptor.addChild(root_0, function23.getTree());

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
    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:1: function : Variable OpenParen ( expr ( Comma expr )* )? CloseParen -> ^( FuncEval Variable ( expr ( expr )* )? ) ;
    public final CalculationsParser.function_return function() throws RecognitionException {
        CalculationsParser.function_return retval = new CalculationsParser.function_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Variable24=null;
        Token OpenParen25=null;
        Token Comma27=null;
        Token CloseParen29=null;
        CalculationsParser.expr_return expr26 = null;

        CalculationsParser.expr_return expr28 = null;


        Object Variable24_tree=null;
        Object OpenParen25_tree=null;
        Object Comma27_tree=null;
        Object CloseParen29_tree=null;
        RewriteRuleTokenStream stream_Variable=new RewriteRuleTokenStream(adaptor,"token Variable");
        RewriteRuleTokenStream stream_CloseParen=new RewriteRuleTokenStream(adaptor,"token CloseParen");
        RewriteRuleTokenStream stream_OpenParen=new RewriteRuleTokenStream(adaptor,"token OpenParen");
        RewriteRuleTokenStream stream_Comma=new RewriteRuleTokenStream(adaptor,"token Comma");
        RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");
        try {
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:9: ( Variable OpenParen ( expr ( Comma expr )* )? CloseParen -> ^( FuncEval Variable ( expr ( expr )* )? ) )
            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:11: Variable OpenParen ( expr ( Comma expr )* )? CloseParen
            {
            Variable24=(Token)match(input,Variable,FOLLOW_Variable_in_function240);  
            stream_Variable.add(Variable24);

            OpenParen25=(Token)match(input,OpenParen,FOLLOW_OpenParen_in_function242);  
            stream_OpenParen.add(OpenParen25);

            // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:30: ( expr ( Comma expr )* )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==OpenParen||(LA10_0>=Add && LA10_0<=Subtract)||(LA10_0>=Decimal && LA10_0<=Variable)) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:31: expr ( Comma expr )*
                    {
                    pushFollow(FOLLOW_expr_in_function245);
                    expr26=expr();

                    state._fsp--;

                    stream_expr.add(expr26.getTree());
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:36: ( Comma expr )*
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( (LA9_0==Comma) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:37: Comma expr
                    	    {
                    	    Comma27=(Token)match(input,Comma,FOLLOW_Comma_in_function248);  
                    	    stream_Comma.add(Comma27);

                    	    pushFollow(FOLLOW_expr_in_function250);
                    	    expr28=expr();

                    	    state._fsp--;

                    	    stream_expr.add(expr28.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop9;
                        }
                    } while (true);


                    }
                    break;

            }

            CloseParen29=(Token)match(input,CloseParen,FOLLOW_CloseParen_in_function256);  
            stream_CloseParen.add(CloseParen29);



            // AST REWRITE
            // elements: expr, expr, Variable
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 36:63: -> ^( FuncEval Variable ( expr ( expr )* )? )
            {
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:66: ^( FuncEval Variable ( expr ( expr )* )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FuncEval, "FuncEval"), root_1);

                adaptor.addChild(root_1, stream_Variable.nextNode());
                // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:86: ( expr ( expr )* )?
                if ( stream_expr.hasNext()||stream_expr.hasNext() ) {
                    adaptor.addChild(root_1, stream_expr.nextTree());
                    // C:\\Users\\abaldwin\\Documents\\EasyInsight\\code\\java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:92: ( expr )*
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


 

    public static final BitSet FOLLOW_term_in_expr115 = new BitSet(new long[]{0x0000000000000180L});
    public static final BitSet FOLLOW_Add_in_expr119 = new BitSet(new long[]{0x000000000000C1A0L});
    public static final BitSet FOLLOW_Subtract_in_expr124 = new BitSet(new long[]{0x000000000000C1A0L});
    public static final BitSet FOLLOW_term_in_expr128 = new BitSet(new long[]{0x0000000000000180L});
    public static final BitSet FOLLOW_EOF_in_expr132 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryOperator_in_term141 = new BitSet(new long[]{0x0000000000000602L});
    public static final BitSet FOLLOW_Multiply_in_term145 = new BitSet(new long[]{0x000000000000C1A0L});
    public static final BitSet FOLLOW_Divide_in_term150 = new BitSet(new long[]{0x000000000000C1A0L});
    public static final BitSet FOLLOW_unaryOperator_in_term154 = new BitSet(new long[]{0x0000000000000602L});
    public static final BitSet FOLLOW_Add_in_unaryOperator166 = new BitSet(new long[]{0x000000000000C1A0L});
    public static final BitSet FOLLOW_Subtract_in_unaryOperator171 = new BitSet(new long[]{0x000000000000C1A0L});
    public static final BitSet FOLLOW_exponent_in_unaryOperator176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_factor_in_exponent182 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_Exp_in_exponent185 = new BitSet(new long[]{0x000000000000C1A0L});
    public static final BitSet FOLLOW_unaryOperator_in_exponent188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_symbol_in_factor198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parenExpr_in_factor202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OpenParen_in_parenExpr211 = new BitSet(new long[]{0x000000000000C1A0L});
    public static final BitSet FOLLOW_expr_in_parenExpr213 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_CloseParen_in_parenExpr215 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Decimal_in_symbol226 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Variable_in_symbol230 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_symbol234 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Variable_in_function240 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_OpenParen_in_function242 = new BitSet(new long[]{0x000000000000C1E0L});
    public static final BitSet FOLLOW_expr_in_function245 = new BitSet(new long[]{0x0000000000000840L});
    public static final BitSet FOLLOW_Comma_in_function248 = new BitSet(new long[]{0x000000000000C1A0L});
    public static final BitSet FOLLOW_expr_in_function250 = new BitSet(new long[]{0x0000000000000840L});
    public static final BitSet FOLLOW_CloseParen_in_function256 = new BitSet(new long[]{0x0000000000000002L});

}