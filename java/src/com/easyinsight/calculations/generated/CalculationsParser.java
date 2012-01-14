// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g 2012-01-13 21:50:10
 package com.easyinsight.calculations.generated; 

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class CalculationsParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "FuncEval", "OpenParen", "CloseParen", "Add", "Subtract", "Multiply", "Divide", "Comma", "Dot", "Exp", "OpenBrace", "CloseBrace", "Quote", "Variable", "Decimal", "String", "UInteger", "Integer", "BracketedVariable", "NoBracketsVariable", "Character", "Digit", "VariableWhitespace", "SpecialChars", "Whitespace", "HideWhiteSpace", "LowerCase", "UpperCase", "InternationalCharacters", "VariableSpecialChars", "NoBracketSpecialChars"
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
    public static final int VariableSpecialChars=33;
    public static final int Comma=11;
    public static final int SpecialChars=27;
    public static final int InternationalCharacters=32;
    public static final int Integer=21;
    public static final int NoBracketSpecialChars=34;

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
    public String getGrammarFileName() { return "/Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g"; }


    public static class expr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expr"
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:27:1: expr : term ( ( Add | Subtract ) term )* ;
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

        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:27:6: ( term ( ( Add | Subtract ) term )* )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:27:8: term ( ( Add | Subtract ) term )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_term_in_expr140);
            term1=term();

            state._fsp--;

            adaptor.addChild(root_0, term1.getTree());
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:27:13: ( ( Add | Subtract ) term )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=Add && LA2_0<=Subtract)) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:27:14: ( Add | Subtract ) term
            	    {
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:27:14: ( Add | Subtract )
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
            	            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:27:15: Add
            	            {
            	            Add2=(Token)match(input,Add,FOLLOW_Add_in_expr144); 
            	            Add2_tree = (Object)adaptor.create(Add2);
            	            root_0 = (Object)adaptor.becomeRoot(Add2_tree, root_0);


            	            }
            	            break;
            	        case 2 :
            	            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:27:22: Subtract
            	            {
            	            Subtract3=(Token)match(input,Subtract,FOLLOW_Subtract_in_expr149); 
            	            Subtract3_tree = (Object)adaptor.create(Subtract3);
            	            root_0 = (Object)adaptor.becomeRoot(Subtract3_tree, root_0);


            	            }
            	            break;

            	    }

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

    public static class startExpr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "startExpr"
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:28:1: startExpr : expr EOF ;
    public final CalculationsParser.startExpr_return startExpr() throws RecognitionException {
        CalculationsParser.startExpr_return retval = new CalculationsParser.startExpr_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token EOF6=null;
        CalculationsParser.expr_return expr5 = null;


        Object EOF6_tree=null;

        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:29:2: ( expr EOF )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:29:4: expr EOF
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_expr_in_startExpr163);
            expr5=expr();

            state._fsp--;

            adaptor.addChild(root_0, expr5.getTree());
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
        return retval;
    }
    // $ANTLR end "startExpr"

    public static class term_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "term"
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:32:1: term : unaryOperator ( ( Multiply | Divide ) unaryOperator )* ;
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
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:32:6: ( unaryOperator ( ( Multiply | Divide ) unaryOperator )* )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:32:8: unaryOperator ( ( Multiply | Divide ) unaryOperator )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_unaryOperator_in_term175);
            unaryOperator7=unaryOperator();

            state._fsp--;

            adaptor.addChild(root_0, unaryOperator7.getTree());
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:32:22: ( ( Multiply | Divide ) unaryOperator )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>=Multiply && LA4_0<=Divide)) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:32:23: ( Multiply | Divide ) unaryOperator
            	    {
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:32:23: ( Multiply | Divide )
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
            	            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:32:24: Multiply
            	            {
            	            Multiply8=(Token)match(input,Multiply,FOLLOW_Multiply_in_term179); 
            	            Multiply8_tree = (Object)adaptor.create(Multiply8);
            	            root_0 = (Object)adaptor.becomeRoot(Multiply8_tree, root_0);


            	            }
            	            break;
            	        case 2 :
            	            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:32:36: Divide
            	            {
            	            Divide9=(Token)match(input,Divide,FOLLOW_Divide_in_term184); 
            	            Divide9_tree = (Object)adaptor.create(Divide9);
            	            root_0 = (Object)adaptor.becomeRoot(Divide9_tree, root_0);


            	            }
            	            break;

            	    }

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
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:34:1: unaryOperator : ( Add | Subtract )? exponent ;
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
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:35:2: ( ( Add | Subtract )? exponent )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:35:4: ( Add | Subtract )? exponent
            {
            root_0 = (Object)adaptor.nil();

            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:35:4: ( Add | Subtract )?
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
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:35:5: Add
                    {
                    Add11=(Token)match(input,Add,FOLLOW_Add_in_unaryOperator200); 
                    Add11_tree = (Object)adaptor.create(Add11);
                    root_0 = (Object)adaptor.becomeRoot(Add11_tree, root_0);


                    }
                    break;
                case 2 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:35:12: Subtract
                    {
                    Subtract12=(Token)match(input,Subtract,FOLLOW_Subtract_in_unaryOperator205); 
                    Subtract12_tree = (Object)adaptor.create(Subtract12);
                    root_0 = (Object)adaptor.becomeRoot(Subtract12_tree, root_0);


                    }
                    break;

            }

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
        return retval;
    }
    // $ANTLR end "unaryOperator"

    public static class exponent_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "exponent"
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:36:1: exponent : factor ( Exp unaryOperator )? ;
    public final CalculationsParser.exponent_return exponent() throws RecognitionException {
        CalculationsParser.exponent_return retval = new CalculationsParser.exponent_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Exp15=null;
        CalculationsParser.factor_return factor14 = null;

        CalculationsParser.unaryOperator_return unaryOperator16 = null;


        Object Exp15_tree=null;

        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:36:9: ( factor ( Exp unaryOperator )? )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:36:11: factor ( Exp unaryOperator )?
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_factor_in_exponent216);
            factor14=factor();

            state._fsp--;

            adaptor.addChild(root_0, factor14.getTree());
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:36:18: ( Exp unaryOperator )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==Exp) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:36:19: Exp unaryOperator
                    {
                    Exp15=(Token)match(input,Exp,FOLLOW_Exp_in_exponent219); 
                    Exp15_tree = (Object)adaptor.create(Exp15);
                    root_0 = (Object)adaptor.becomeRoot(Exp15_tree, root_0);

                    pushFollow(FOLLOW_unaryOperator_in_exponent222);
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
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:38:1: factor : ( symbol | parenExpr );
    public final CalculationsParser.factor_return factor() throws RecognitionException {
        CalculationsParser.factor_return retval = new CalculationsParser.factor_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        CalculationsParser.symbol_return symbol17 = null;

        CalculationsParser.parenExpr_return parenExpr18 = null;



        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:38:8: ( symbol | parenExpr )
            int alt7=2;
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

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:38:10: symbol
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_symbol_in_factor232);
                    symbol17=symbol();

                    state._fsp--;

                    adaptor.addChild(root_0, symbol17.getTree());

                    }
                    break;
                case 2 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:38:19: parenExpr
                    {
                    root_0 = (Object)adaptor.nil();

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
        return retval;
    }
    // $ANTLR end "factor"

    public static class parenExpr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parenExpr"
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:40:1: parenExpr : OpenParen expr CloseParen -> expr ;
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
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:41:2: ( OpenParen expr CloseParen -> expr )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:41:4: OpenParen expr CloseParen
            {
            OpenParen19=(Token)match(input,OpenParen,FOLLOW_OpenParen_in_parenExpr245);  
            stream_OpenParen.add(OpenParen19);

            pushFollow(FOLLOW_expr_in_parenExpr247);
            expr20=expr();

            state._fsp--;

            stream_expr.add(expr20.getTree());
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
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:42:1: symbol : ( literal | Variable | function );
    public final CalculationsParser.symbol_return symbol() throws RecognitionException {
        CalculationsParser.symbol_return retval = new CalculationsParser.symbol_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Variable23=null;
        CalculationsParser.literal_return literal22 = null;

        CalculationsParser.function_return function24 = null;


        Object Variable23_tree=null;

        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:42:8: ( literal | Variable | function )
            int alt8=3;
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
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:42:10: literal
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_literal_in_symbol260);
                    literal22=literal();

                    state._fsp--;

                    adaptor.addChild(root_0, literal22.getTree());

                    }
                    break;
                case 2 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:42:20: Variable
                    {
                    root_0 = (Object)adaptor.nil();

                    Variable23=(Token)match(input,Variable,FOLLOW_Variable_in_symbol264); 
                    Variable23_tree = (Object)adaptor.create(Variable23);
                    adaptor.addChild(root_0, Variable23_tree);


                    }
                    break;
                case 3 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:42:31: function
                    {
                    root_0 = (Object)adaptor.nil();

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
        return retval;
    }
    // $ANTLR end "symbol"

    public static class function_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "function"
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:1: function : Variable OpenParen ( expr ( Comma expr )* )? CloseParen -> ^( FuncEval Variable ( expr ( expr )* )? ) ;
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
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:9: ( Variable OpenParen ( expr ( Comma expr )* )? CloseParen -> ^( FuncEval Variable ( expr ( expr )* )? ) )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:11: Variable OpenParen ( expr ( Comma expr )* )? CloseParen
            {
            Variable25=(Token)match(input,Variable,FOLLOW_Variable_in_function274);  
            stream_Variable.add(Variable25);

            OpenParen26=(Token)match(input,OpenParen,FOLLOW_OpenParen_in_function276);  
            stream_OpenParen.add(OpenParen26);

            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:30: ( expr ( Comma expr )* )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==OpenParen||(LA10_0>=Add && LA10_0<=Subtract)||(LA10_0>=Variable && LA10_0<=String)) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:31: expr ( Comma expr )*
                    {
                    pushFollow(FOLLOW_expr_in_function279);
                    expr27=expr();

                    state._fsp--;

                    stream_expr.add(expr27.getTree());
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:36: ( Comma expr )*
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( (LA9_0==Comma) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:37: Comma expr
                    	    {
                    	    Comma28=(Token)match(input,Comma,FOLLOW_Comma_in_function282);  
                    	    stream_Comma.add(Comma28);

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


                    }
                    break;

            }

            CloseParen30=(Token)match(input,CloseParen,FOLLOW_CloseParen_in_function290);  
            stream_CloseParen.add(CloseParen30);



            // AST REWRITE
            // elements: expr, Variable, expr
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
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:66: ^( FuncEval Variable ( expr ( expr )* )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FuncEval, "FuncEval"), root_1);

                adaptor.addChild(root_1, stream_Variable.nextNode());
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:86: ( expr ( expr )* )?
                if ( stream_expr.hasNext()||stream_expr.hasNext() ) {
                    adaptor.addChild(root_1, stream_expr.nextTree());
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:92: ( expr )*
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

    public static class literal_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "literal"
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:47:1: literal : ( Decimal | String );
    public final CalculationsParser.literal_return literal() throws RecognitionException {
        CalculationsParser.literal_return retval = new CalculationsParser.literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set31=null;

        Object set31_tree=null;

        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:47:9: ( Decimal | String )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            {
            root_0 = (Object)adaptor.nil();

            set31=(Token)input.LT(1);
            if ( (input.LA(1)>=Decimal && input.LA(1)<=String) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set31));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
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