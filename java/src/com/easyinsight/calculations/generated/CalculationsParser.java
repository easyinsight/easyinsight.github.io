// $ANTLR 3.0.1 C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g 2008-12-20 15:01:29
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

        public CalculationsParser(TokenStream input) {
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
    public String getGrammarFileName() { return "C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g"; }


    public static class expr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start expr
    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:23:1: expr : term ( ( Add | Subtract ) term )* EOF ;
    public final expr_return expr() throws RecognitionException {
        expr_return retval = new expr_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Add2=null;
        Token Subtract3=null;
        Token EOF5=null;
        term_return term1 = null;

        term_return term4 = null;


        Object Add2_tree=null;
        Object Subtract3_tree=null;
        Object EOF5_tree=null;

        try {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:23:6: ( term ( ( Add | Subtract ) term )* EOF )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:23:8: term ( ( Add | Subtract ) term )* EOF
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_term_in_expr115);
            term1=term();
            _fsp--;

            adaptor.addChild(root_0, term1.getTree());
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:23:13: ( ( Add | Subtract ) term )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=Add && LA2_0<=Subtract)) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:23:14: ( Add | Subtract ) term
            	    {
            	    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:23:14: ( Add | Subtract )
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
            	            new NoViableAltException("23:14: ( Add | Subtract )", 1, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt1) {
            	        case 1 :
            	            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:23:15: Add
            	            {
            	            Add2=(Token)input.LT(1);
            	            match(input,Add,FOLLOW_Add_in_expr119); 
            	            Add2_tree = (Object)adaptor.create(Add2);
            	            root_0 = (Object)adaptor.becomeRoot(Add2_tree, root_0);


            	            }
            	            break;
            	        case 2 :
            	            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:23:22: Subtract
            	            {
            	            Subtract3=(Token)input.LT(1);
            	            match(input,Subtract,FOLLOW_Subtract_in_expr124); 
            	            Subtract3_tree = (Object)adaptor.create(Subtract3);
            	            root_0 = (Object)adaptor.becomeRoot(Subtract3_tree, root_0);


            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_term_in_expr128);
            	    term4=term();
            	    _fsp--;

            	    adaptor.addChild(root_0, term4.getTree());

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            EOF5=(Token)input.LT(1);
            match(input,EOF,FOLLOW_EOF_in_expr132); 
            EOF5_tree = (Object)adaptor.create(EOF5);
            adaptor.addChild(root_0, EOF5_tree);


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
    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:25:1: term : unaryOperator ( ( Multiply | Divide ) unaryOperator )* ;
    public final term_return term() throws RecognitionException {
        term_return retval = new term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Multiply7=null;
        Token Divide8=null;
        unaryOperator_return unaryOperator6 = null;

        unaryOperator_return unaryOperator9 = null;


        Object Multiply7_tree=null;
        Object Divide8_tree=null;

        try {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:25:6: ( unaryOperator ( ( Multiply | Divide ) unaryOperator )* )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:25:8: unaryOperator ( ( Multiply | Divide ) unaryOperator )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_unaryOperator_in_term140);
            unaryOperator6=unaryOperator();
            _fsp--;

            adaptor.addChild(root_0, unaryOperator6.getTree());
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:25:22: ( ( Multiply | Divide ) unaryOperator )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>=Multiply && LA4_0<=Divide)) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:25:23: ( Multiply | Divide ) unaryOperator
            	    {
            	    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:25:23: ( Multiply | Divide )
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
            	            new NoViableAltException("25:23: ( Multiply | Divide )", 3, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt3) {
            	        case 1 :
            	            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:25:24: Multiply
            	            {
            	            Multiply7=(Token)input.LT(1);
            	            match(input,Multiply,FOLLOW_Multiply_in_term144); 
            	            Multiply7_tree = (Object)adaptor.create(Multiply7);
            	            root_0 = (Object)adaptor.becomeRoot(Multiply7_tree, root_0);


            	            }
            	            break;
            	        case 2 :
            	            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:25:36: Divide
            	            {
            	            Divide8=(Token)input.LT(1);
            	            match(input,Divide,FOLLOW_Divide_in_term149); 
            	            Divide8_tree = (Object)adaptor.create(Divide8);
            	            root_0 = (Object)adaptor.becomeRoot(Divide8_tree, root_0);


            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_unaryOperator_in_term153);
            	    unaryOperator9=unaryOperator();
            	    _fsp--;

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
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end term

    public static class unaryOperator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start unaryOperator
    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:27:1: unaryOperator : ( Add | Subtract )? exponent ;
    public final unaryOperator_return unaryOperator() throws RecognitionException {
        unaryOperator_return retval = new unaryOperator_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Add10=null;
        Token Subtract11=null;
        exponent_return exponent12 = null;


        Object Add10_tree=null;
        Object Subtract11_tree=null;

        try {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:2: ( ( Add | Subtract )? exponent )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:4: ( Add | Subtract )? exponent
            {
            root_0 = (Object)adaptor.nil();

            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:4: ( Add | Subtract )?
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
                    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:5: Add
                    {
                    Add10=(Token)input.LT(1);
                    match(input,Add,FOLLOW_Add_in_unaryOperator165); 
                    Add10_tree = (Object)adaptor.create(Add10);
                    root_0 = (Object)adaptor.becomeRoot(Add10_tree, root_0);


                    }
                    break;
                case 2 :
                    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:28:12: Subtract
                    {
                    Subtract11=(Token)input.LT(1);
                    match(input,Subtract,FOLLOW_Subtract_in_unaryOperator170); 
                    Subtract11_tree = (Object)adaptor.create(Subtract11);
                    root_0 = (Object)adaptor.becomeRoot(Subtract11_tree, root_0);


                    }
                    break;

            }

            pushFollow(FOLLOW_exponent_in_unaryOperator175);
            exponent12=exponent();
            _fsp--;

            adaptor.addChild(root_0, exponent12.getTree());

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
    // $ANTLR end unaryOperator

    public static class exponent_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start exponent
    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:29:1: exponent : factor ( Exp unaryOperator )? ;
    public final exponent_return exponent() throws RecognitionException {
        exponent_return retval = new exponent_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Exp14=null;
        factor_return factor13 = null;

        unaryOperator_return unaryOperator15 = null;


        Object Exp14_tree=null;

        try {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:29:9: ( factor ( Exp unaryOperator )? )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:29:11: factor ( Exp unaryOperator )?
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_factor_in_exponent181);
            factor13=factor();
            _fsp--;

            adaptor.addChild(root_0, factor13.getTree());
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:29:18: ( Exp unaryOperator )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==Exp) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:29:19: Exp unaryOperator
                    {
                    Exp14=(Token)input.LT(1);
                    match(input,Exp,FOLLOW_Exp_in_exponent184); 
                    Exp14_tree = (Object)adaptor.create(Exp14);
                    root_0 = (Object)adaptor.becomeRoot(Exp14_tree, root_0);

                    pushFollow(FOLLOW_unaryOperator_in_exponent187);
                    unaryOperator15=unaryOperator();
                    _fsp--;

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
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end exponent

    public static class factor_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start factor
    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:31:1: factor : ( symbol | parenExpr );
    public final factor_return factor() throws RecognitionException {
        factor_return retval = new factor_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        symbol_return symbol16 = null;

        parenExpr_return parenExpr17 = null;



        try {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:31:8: ( symbol | parenExpr )
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
                    new NoViableAltException("31:1: factor : ( symbol | parenExpr );", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:31:10: symbol
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_symbol_in_factor197);
                    symbol16=symbol();
                    _fsp--;

                    adaptor.addChild(root_0, symbol16.getTree());

                    }
                    break;
                case 2 :
                    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:31:19: parenExpr
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_parenExpr_in_factor201);
                    parenExpr17=parenExpr();
                    _fsp--;

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
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end factor

    public static class parenExpr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start parenExpr
    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:33:1: parenExpr : OpenParen expr CloseParen -> expr ;
    public final parenExpr_return parenExpr() throws RecognitionException {
        parenExpr_return retval = new parenExpr_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token OpenParen18=null;
        Token CloseParen20=null;
        expr_return expr19 = null;


        Object OpenParen18_tree=null;
        Object CloseParen20_tree=null;
        RewriteRuleTokenStream stream_CloseParen=new RewriteRuleTokenStream(adaptor,"token CloseParen");
        RewriteRuleTokenStream stream_OpenParen=new RewriteRuleTokenStream(adaptor,"token OpenParen");
        RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");
        try {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:34:2: ( OpenParen expr CloseParen -> expr )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:34:4: OpenParen expr CloseParen
            {
            OpenParen18=(Token)input.LT(1);
            match(input,OpenParen,FOLLOW_OpenParen_in_parenExpr210); 
            stream_OpenParen.add(OpenParen18);

            pushFollow(FOLLOW_expr_in_parenExpr212);
            expr19=expr();
            _fsp--;

            stream_expr.add(expr19.getTree());
            CloseParen20=(Token)input.LT(1);
            match(input,CloseParen,FOLLOW_CloseParen_in_parenExpr214); 
            stream_CloseParen.add(CloseParen20);


            // AST REWRITE
            // elements: expr
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 34:30: -> expr
            {
                adaptor.addChild(root_0, stream_expr.next());

            }



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
    // $ANTLR end parenExpr

    public static class symbol_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start symbol
    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:35:1: symbol : ( Decimal | Variable | function );
    public final symbol_return symbol() throws RecognitionException {
        symbol_return retval = new symbol_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Decimal21=null;
        Token Variable22=null;
        function_return function23 = null;


        Object Decimal21_tree=null;
        Object Variable22_tree=null;

        try {
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:35:8: ( Decimal | Variable | function )
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
                        new NoViableAltException("35:1: symbol : ( Decimal | Variable | function );", 8, 2, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("35:1: symbol : ( Decimal | Variable | function );", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:35:10: Decimal
                    {
                    root_0 = (Object)adaptor.nil();

                    Decimal21=(Token)input.LT(1);
                    match(input,Decimal,FOLLOW_Decimal_in_symbol225); 
                    Decimal21_tree = (Object)adaptor.create(Decimal21);
                    adaptor.addChild(root_0, Decimal21_tree);


                    }
                    break;
                case 2 :
                    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:35:20: Variable
                    {
                    root_0 = (Object)adaptor.nil();

                    Variable22=(Token)input.LT(1);
                    match(input,Variable,FOLLOW_Variable_in_symbol229); 
                    Variable22_tree = (Object)adaptor.create(Variable22);
                    adaptor.addChild(root_0, Variable22_tree);


                    }
                    break;
                case 3 :
                    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:35:31: function
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_function_in_symbol233);
                    function23=function();
                    _fsp--;

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
    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:1: function : Variable OpenParen ( expr ( Comma expr )* )? CloseParen -> ^( FuncEval Variable ( expr ( expr )* )? ) ;
    public final function_return function() throws RecognitionException {
        function_return retval = new function_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Variable24=null;
        Token OpenParen25=null;
        Token Comma27=null;
        Token CloseParen29=null;
        expr_return expr26 = null;

        expr_return expr28 = null;


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
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:9: ( Variable OpenParen ( expr ( Comma expr )* )? CloseParen -> ^( FuncEval Variable ( expr ( expr )* )? ) )
            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:11: Variable OpenParen ( expr ( Comma expr )* )? CloseParen
            {
            Variable24=(Token)input.LT(1);
            match(input,Variable,FOLLOW_Variable_in_function239); 
            stream_Variable.add(Variable24);

            OpenParen25=(Token)input.LT(1);
            match(input,OpenParen,FOLLOW_OpenParen_in_function241); 
            stream_OpenParen.add(OpenParen25);

            // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:30: ( expr ( Comma expr )* )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==OpenParen||(LA10_0>=Add && LA10_0<=Subtract)||(LA10_0>=Decimal && LA10_0<=Variable)) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:31: expr ( Comma expr )*
                    {
                    pushFollow(FOLLOW_expr_in_function244);
                    expr26=expr();
                    _fsp--;

                    stream_expr.add(expr26.getTree());
                    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:36: ( Comma expr )*
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( (LA9_0==Comma) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:37: Comma expr
                    	    {
                    	    Comma27=(Token)input.LT(1);
                    	    match(input,Comma,FOLLOW_Comma_in_function247); 
                    	    stream_Comma.add(Comma27);

                    	    pushFollow(FOLLOW_expr_in_function249);
                    	    expr28=expr();
                    	    _fsp--;

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

            CloseParen29=(Token)input.LT(1);
            match(input,CloseParen,FOLLOW_CloseParen_in_function255); 
            stream_CloseParen.add(CloseParen29);


            // AST REWRITE
            // elements: Variable, expr, expr
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 36:63: -> ^( FuncEval Variable ( expr ( expr )* )? )
            {
                // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:66: ^( FuncEval Variable ( expr ( expr )* )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(adaptor.create(FuncEval, "FuncEval"), root_1);

                adaptor.addChild(root_1, stream_Variable.next());
                // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:86: ( expr ( expr )* )?
                if ( stream_expr.hasNext()||stream_expr.hasNext() ) {
                    adaptor.addChild(root_1, stream_expr.next());
                    // C:\\Users\\Alan\\Documents\\Intellij\\EasyInsight\\Java\\src\\com\\easyinsight\\calculations\\Calculations.g:36:92: ( expr )*
                    while ( stream_expr.hasNext() ) {
                        adaptor.addChild(root_1, stream_expr.next());

                    }
                    stream_expr.reset();

                }
                stream_expr.reset();
                stream_expr.reset();

                adaptor.addChild(root_0, root_1);
                }

            }



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


 

    public static final BitSet FOLLOW_term_in_expr115 = new BitSet(new long[]{0x0000000000000180L});
    public static final BitSet FOLLOW_Add_in_expr119 = new BitSet(new long[]{0x000000000000C1A0L});
    public static final BitSet FOLLOW_Subtract_in_expr124 = new BitSet(new long[]{0x000000000000C1A0L});
    public static final BitSet FOLLOW_term_in_expr128 = new BitSet(new long[]{0x0000000000000180L});
    public static final BitSet FOLLOW_EOF_in_expr132 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryOperator_in_term140 = new BitSet(new long[]{0x0000000000000602L});
    public static final BitSet FOLLOW_Multiply_in_term144 = new BitSet(new long[]{0x000000000000C1A0L});
    public static final BitSet FOLLOW_Divide_in_term149 = new BitSet(new long[]{0x000000000000C1A0L});
    public static final BitSet FOLLOW_unaryOperator_in_term153 = new BitSet(new long[]{0x0000000000000602L});
    public static final BitSet FOLLOW_Add_in_unaryOperator165 = new BitSet(new long[]{0x000000000000C020L});
    public static final BitSet FOLLOW_Subtract_in_unaryOperator170 = new BitSet(new long[]{0x000000000000C020L});
    public static final BitSet FOLLOW_exponent_in_unaryOperator175 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_factor_in_exponent181 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_Exp_in_exponent184 = new BitSet(new long[]{0x000000000000C1A0L});
    public static final BitSet FOLLOW_unaryOperator_in_exponent187 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_symbol_in_factor197 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parenExpr_in_factor201 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OpenParen_in_parenExpr210 = new BitSet(new long[]{0x000000000000C1A0L});
    public static final BitSet FOLLOW_expr_in_parenExpr212 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_CloseParen_in_parenExpr214 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Decimal_in_symbol225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Variable_in_symbol229 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_symbol233 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Variable_in_function239 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_OpenParen_in_function241 = new BitSet(new long[]{0x000000000000C1E0L});
    public static final BitSet FOLLOW_expr_in_function244 = new BitSet(new long[]{0x0000000000000840L});
    public static final BitSet FOLLOW_Comma_in_function247 = new BitSet(new long[]{0x000000000000C1A0L});
    public static final BitSet FOLLOW_expr_in_function249 = new BitSet(new long[]{0x0000000000000840L});
    public static final BitSet FOLLOW_CloseParen_in_function255 = new BitSet(new long[]{0x0000000000000002L});

}