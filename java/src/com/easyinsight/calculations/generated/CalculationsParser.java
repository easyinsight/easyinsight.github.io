// $ANTLR 3.4 /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g 2012-12-07 14:22:42
 package com.easyinsight.calculations.generated; 

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class CalculationsParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Add", "And", "BracketedVariable", "Character", "CloseBrace", "CloseParen", "Comma", "Decimal", "Digit", "Divide", "Dot", "Equals", "Exp", "FuncEval", "GreaterThan", "GreaterThanEqualTo", "HideWhiteSpace", "Integer", "InternationalCharacters", "LessThan", "LessThanEqualTo", "LowerCase", "Multiply", "NoBracketSpecialChars", "NoBracketsVariable", "Not", "NotEquals", "OpenBrace", "OpenParen", "Or", "Quote", "SpecialChars", "String", "Subtract", "UInteger", "UpperCase", "Variable", "VariableSpecialChars", "VariableWhitespace", "Whitespace"
    };

    public static final int EOF=-1;
    public static final int Add=4;
    public static final int And=5;
    public static final int BracketedVariable=6;
    public static final int Character=7;
    public static final int CloseBrace=8;
    public static final int CloseParen=9;
    public static final int Comma=10;
    public static final int Decimal=11;
    public static final int Digit=12;
    public static final int Divide=13;
    public static final int Dot=14;
    public static final int Equals=15;
    public static final int Exp=16;
    public static final int FuncEval=17;
    public static final int GreaterThan=18;
    public static final int GreaterThanEqualTo=19;
    public static final int HideWhiteSpace=20;
    public static final int Integer=21;
    public static final int InternationalCharacters=22;
    public static final int LessThan=23;
    public static final int LessThanEqualTo=24;
    public static final int LowerCase=25;
    public static final int Multiply=26;
    public static final int NoBracketSpecialChars=27;
    public static final int NoBracketsVariable=28;
    public static final int Not=29;
    public static final int NotEquals=30;
    public static final int OpenBrace=31;
    public static final int OpenParen=32;
    public static final int Or=33;
    public static final int Quote=34;
    public static final int SpecialChars=35;
    public static final int String=36;
    public static final int Subtract=37;
    public static final int UInteger=38;
    public static final int UpperCase=39;
    public static final int Variable=40;
    public static final int VariableSpecialChars=41;
    public static final int VariableWhitespace=42;
    public static final int Whitespace=43;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

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
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:36:1: expr : evaluation ( ( And ^| Or ^) evaluation )* ;
    public final CalculationsParser.expr_return expr() throws RecognitionException {
        CalculationsParser.expr_return retval = new CalculationsParser.expr_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token And2=null;
        Token Or3=null;
        CalculationsParser.evaluation_return evaluation1 =null;

        CalculationsParser.evaluation_return evaluation4 =null;


        Object And2_tree=null;
        Object Or3_tree=null;

        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:36:6: ( evaluation ( ( And ^| Or ^) evaluation )* )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:36:8: evaluation ( ( And ^| Or ^) evaluation )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_evaluation_in_expr212);
            evaluation1=evaluation();

            state._fsp--;

            adaptor.addChild(root_0, evaluation1.getTree());

            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:36:19: ( ( And ^| Or ^) evaluation )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==And||LA2_0==Or) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:36:20: ( And ^| Or ^) evaluation
            	    {
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:36:20: ( And ^| Or ^)
            	    int alt1=2;
            	    int LA1_0 = input.LA(1);

            	    if ( (LA1_0==And) ) {
            	        alt1=1;
            	    }
            	    else if ( (LA1_0==Or) ) {
            	        alt1=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 1, 0, input);

            	        throw nvae;

            	    }
            	    switch (alt1) {
            	        case 1 :
            	            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:36:21: And ^
            	            {
            	            And2=(Token)match(input,And,FOLLOW_And_in_expr216); 
            	            And2_tree = 
            	            (Object)adaptor.create(And2)
            	            ;
            	            root_0 = (Object)adaptor.becomeRoot(And2_tree, root_0);


            	            }
            	            break;
            	        case 2 :
            	            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:36:28: Or ^
            	            {
            	            Or3=(Token)match(input,Or,FOLLOW_Or_in_expr221); 
            	            Or3_tree = 
            	            (Object)adaptor.create(Or3)
            	            ;
            	            root_0 = (Object)adaptor.becomeRoot(Or3_tree, root_0);


            	            }
            	            break;

            	    }


            	    pushFollow(FOLLOW_evaluation_in_expr225);
            	    evaluation4=evaluation();

            	    state._fsp--;

            	    adaptor.addChild(root_0, evaluation4.getTree());

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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "expr"


    public static class startExpr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "startExpr"
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:39:1: startExpr : expr EOF !;
    public final CalculationsParser.startExpr_return startExpr() throws RecognitionException {
        CalculationsParser.startExpr_return retval = new CalculationsParser.startExpr_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token EOF6=null;
        CalculationsParser.expr_return expr5 =null;


        Object EOF6_tree=null;

        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:40:2: ( expr EOF !)
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:40:4: expr EOF !
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_expr_in_startExpr237);
            expr5=expr();

            state._fsp--;

            adaptor.addChild(root_0, expr5.getTree());

            EOF6=(Token)match(input,EOF,FOLLOW_EOF_in_startExpr239); 

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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "startExpr"


    public static class evaluation_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "evaluation"
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:42:1: evaluation : sum ( ( LessThanEqualTo ^| GreaterThanEqualTo ^| Equals ^| NotEquals ^| GreaterThan ^| LessThan ^) sum )* ;
    public final CalculationsParser.evaluation_return evaluation() throws RecognitionException {
        CalculationsParser.evaluation_return retval = new CalculationsParser.evaluation_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LessThanEqualTo8=null;
        Token GreaterThanEqualTo9=null;
        Token Equals10=null;
        Token NotEquals11=null;
        Token GreaterThan12=null;
        Token LessThan13=null;
        CalculationsParser.sum_return sum7 =null;

        CalculationsParser.sum_return sum14 =null;


        Object LessThanEqualTo8_tree=null;
        Object GreaterThanEqualTo9_tree=null;
        Object Equals10_tree=null;
        Object NotEquals11_tree=null;
        Object GreaterThan12_tree=null;
        Object LessThan13_tree=null;

        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:2: ( sum ( ( LessThanEqualTo ^| GreaterThanEqualTo ^| Equals ^| NotEquals ^| GreaterThan ^| LessThan ^) sum )* )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:4: sum ( ( LessThanEqualTo ^| GreaterThanEqualTo ^| Equals ^| NotEquals ^| GreaterThan ^| LessThan ^) sum )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_sum_in_evaluation249);
            sum7=sum();

            state._fsp--;

            adaptor.addChild(root_0, sum7.getTree());

            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:8: ( ( LessThanEqualTo ^| GreaterThanEqualTo ^| Equals ^| NotEquals ^| GreaterThan ^| LessThan ^) sum )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==Equals||(LA4_0 >= GreaterThan && LA4_0 <= GreaterThanEqualTo)||(LA4_0 >= LessThan && LA4_0 <= LessThanEqualTo)||LA4_0==NotEquals) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:9: ( LessThanEqualTo ^| GreaterThanEqualTo ^| Equals ^| NotEquals ^| GreaterThan ^| LessThan ^) sum
            	    {
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:9: ( LessThanEqualTo ^| GreaterThanEqualTo ^| Equals ^| NotEquals ^| GreaterThan ^| LessThan ^)
            	    int alt3=6;
            	    switch ( input.LA(1) ) {
            	    case LessThanEqualTo:
            	        {
            	        alt3=1;
            	        }
            	        break;
            	    case GreaterThanEqualTo:
            	        {
            	        alt3=2;
            	        }
            	        break;
            	    case Equals:
            	        {
            	        alt3=3;
            	        }
            	        break;
            	    case NotEquals:
            	        {
            	        alt3=4;
            	        }
            	        break;
            	    case GreaterThan:
            	        {
            	        alt3=5;
            	        }
            	        break;
            	    case LessThan:
            	        {
            	        alt3=6;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 3, 0, input);

            	        throw nvae;

            	    }

            	    switch (alt3) {
            	        case 1 :
            	            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:10: LessThanEqualTo ^
            	            {
            	            LessThanEqualTo8=(Token)match(input,LessThanEqualTo,FOLLOW_LessThanEqualTo_in_evaluation253); 
            	            LessThanEqualTo8_tree = 
            	            (Object)adaptor.create(LessThanEqualTo8)
            	            ;
            	            root_0 = (Object)adaptor.becomeRoot(LessThanEqualTo8_tree, root_0);


            	            }
            	            break;
            	        case 2 :
            	            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:29: GreaterThanEqualTo ^
            	            {
            	            GreaterThanEqualTo9=(Token)match(input,GreaterThanEqualTo,FOLLOW_GreaterThanEqualTo_in_evaluation258); 
            	            GreaterThanEqualTo9_tree = 
            	            (Object)adaptor.create(GreaterThanEqualTo9)
            	            ;
            	            root_0 = (Object)adaptor.becomeRoot(GreaterThanEqualTo9_tree, root_0);


            	            }
            	            break;
            	        case 3 :
            	            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:51: Equals ^
            	            {
            	            Equals10=(Token)match(input,Equals,FOLLOW_Equals_in_evaluation263); 
            	            Equals10_tree = 
            	            (Object)adaptor.create(Equals10)
            	            ;
            	            root_0 = (Object)adaptor.becomeRoot(Equals10_tree, root_0);


            	            }
            	            break;
            	        case 4 :
            	            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:61: NotEquals ^
            	            {
            	            NotEquals11=(Token)match(input,NotEquals,FOLLOW_NotEquals_in_evaluation268); 
            	            NotEquals11_tree = 
            	            (Object)adaptor.create(NotEquals11)
            	            ;
            	            root_0 = (Object)adaptor.becomeRoot(NotEquals11_tree, root_0);


            	            }
            	            break;
            	        case 5 :
            	            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:74: GreaterThan ^
            	            {
            	            GreaterThan12=(Token)match(input,GreaterThan,FOLLOW_GreaterThan_in_evaluation273); 
            	            GreaterThan12_tree = 
            	            (Object)adaptor.create(GreaterThan12)
            	            ;
            	            root_0 = (Object)adaptor.becomeRoot(GreaterThan12_tree, root_0);


            	            }
            	            break;
            	        case 6 :
            	            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:43:89: LessThan ^
            	            {
            	            LessThan13=(Token)match(input,LessThan,FOLLOW_LessThan_in_evaluation278); 
            	            LessThan13_tree = 
            	            (Object)adaptor.create(LessThan13)
            	            ;
            	            root_0 = (Object)adaptor.becomeRoot(LessThan13_tree, root_0);


            	            }
            	            break;

            	    }


            	    pushFollow(FOLLOW_sum_in_evaluation282);
            	    sum14=sum();

            	    state._fsp--;

            	    adaptor.addChild(root_0, sum14.getTree());

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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "evaluation"


    public static class sum_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "sum"
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:45:1: sum : term ( ( Add ^| Subtract ^) term )* ;
    public final CalculationsParser.sum_return sum() throws RecognitionException {
        CalculationsParser.sum_return retval = new CalculationsParser.sum_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token Add16=null;
        Token Subtract17=null;
        CalculationsParser.term_return term15 =null;

        CalculationsParser.term_return term18 =null;


        Object Add16_tree=null;
        Object Subtract17_tree=null;

        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:45:5: ( term ( ( Add ^| Subtract ^) term )* )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:45:7: term ( ( Add ^| Subtract ^) term )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_term_in_sum292);
            term15=term();

            state._fsp--;

            adaptor.addChild(root_0, term15.getTree());

            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:45:12: ( ( Add ^| Subtract ^) term )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==Add||LA6_0==Subtract) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:45:13: ( Add ^| Subtract ^) term
            	    {
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:45:13: ( Add ^| Subtract ^)
            	    int alt5=2;
            	    int LA5_0 = input.LA(1);

            	    if ( (LA5_0==Add) ) {
            	        alt5=1;
            	    }
            	    else if ( (LA5_0==Subtract) ) {
            	        alt5=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 5, 0, input);

            	        throw nvae;

            	    }
            	    switch (alt5) {
            	        case 1 :
            	            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:45:14: Add ^
            	            {
            	            Add16=(Token)match(input,Add,FOLLOW_Add_in_sum296); 
            	            Add16_tree = 
            	            (Object)adaptor.create(Add16)
            	            ;
            	            root_0 = (Object)adaptor.becomeRoot(Add16_tree, root_0);


            	            }
            	            break;
            	        case 2 :
            	            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:45:21: Subtract ^
            	            {
            	            Subtract17=(Token)match(input,Subtract,FOLLOW_Subtract_in_sum301); 
            	            Subtract17_tree = 
            	            (Object)adaptor.create(Subtract17)
            	            ;
            	            root_0 = (Object)adaptor.becomeRoot(Subtract17_tree, root_0);


            	            }
            	            break;

            	    }


            	    pushFollow(FOLLOW_term_in_sum305);
            	    term18=term();

            	    state._fsp--;

            	    adaptor.addChild(root_0, term18.getTree());

            	    }
            	    break;

            	default :
            	    break loop6;
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "sum"


    public static class term_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "term"
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:47:1: term : unaryOperator ( ( Multiply ^| Divide ^) unaryOperator )* ;
    public final CalculationsParser.term_return term() throws RecognitionException {
        CalculationsParser.term_return retval = new CalculationsParser.term_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token Multiply20=null;
        Token Divide21=null;
        CalculationsParser.unaryOperator_return unaryOperator19 =null;

        CalculationsParser.unaryOperator_return unaryOperator22 =null;


        Object Multiply20_tree=null;
        Object Divide21_tree=null;

        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:47:6: ( unaryOperator ( ( Multiply ^| Divide ^) unaryOperator )* )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:47:8: unaryOperator ( ( Multiply ^| Divide ^) unaryOperator )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_unaryOperator_in_term315);
            unaryOperator19=unaryOperator();

            state._fsp--;

            adaptor.addChild(root_0, unaryOperator19.getTree());

            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:47:22: ( ( Multiply ^| Divide ^) unaryOperator )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==Divide||LA8_0==Multiply) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:47:23: ( Multiply ^| Divide ^) unaryOperator
            	    {
            	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:47:23: ( Multiply ^| Divide ^)
            	    int alt7=2;
            	    int LA7_0 = input.LA(1);

            	    if ( (LA7_0==Multiply) ) {
            	        alt7=1;
            	    }
            	    else if ( (LA7_0==Divide) ) {
            	        alt7=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 7, 0, input);

            	        throw nvae;

            	    }
            	    switch (alt7) {
            	        case 1 :
            	            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:47:24: Multiply ^
            	            {
            	            Multiply20=(Token)match(input,Multiply,FOLLOW_Multiply_in_term319); 
            	            Multiply20_tree = 
            	            (Object)adaptor.create(Multiply20)
            	            ;
            	            root_0 = (Object)adaptor.becomeRoot(Multiply20_tree, root_0);


            	            }
            	            break;
            	        case 2 :
            	            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:47:36: Divide ^
            	            {
            	            Divide21=(Token)match(input,Divide,FOLLOW_Divide_in_term324); 
            	            Divide21_tree = 
            	            (Object)adaptor.create(Divide21)
            	            ;
            	            root_0 = (Object)adaptor.becomeRoot(Divide21_tree, root_0);


            	            }
            	            break;

            	    }


            	    pushFollow(FOLLOW_unaryOperator_in_term328);
            	    unaryOperator22=unaryOperator();

            	    state._fsp--;

            	    adaptor.addChild(root_0, unaryOperator22.getTree());

            	    }
            	    break;

            	default :
            	    break loop8;
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "term"


    public static class unaryOperator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "unaryOperator"
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:49:1: unaryOperator : ( Add ^| Subtract ^| Not ^)? exponent ;
    public final CalculationsParser.unaryOperator_return unaryOperator() throws RecognitionException {
        CalculationsParser.unaryOperator_return retval = new CalculationsParser.unaryOperator_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token Add23=null;
        Token Subtract24=null;
        Token Not25=null;
        CalculationsParser.exponent_return exponent26 =null;


        Object Add23_tree=null;
        Object Subtract24_tree=null;
        Object Not25_tree=null;

        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:50:2: ( ( Add ^| Subtract ^| Not ^)? exponent )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:50:4: ( Add ^| Subtract ^| Not ^)? exponent
            {
            root_0 = (Object)adaptor.nil();


            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:50:4: ( Add ^| Subtract ^| Not ^)?
            int alt9=4;
            switch ( input.LA(1) ) {
                case Add:
                    {
                    alt9=1;
                    }
                    break;
                case Subtract:
                    {
                    alt9=2;
                    }
                    break;
                case Not:
                    {
                    alt9=3;
                    }
                    break;
            }

            switch (alt9) {
                case 1 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:50:5: Add ^
                    {
                    Add23=(Token)match(input,Add,FOLLOW_Add_in_unaryOperator340); 
                    Add23_tree = 
                    (Object)adaptor.create(Add23)
                    ;
                    root_0 = (Object)adaptor.becomeRoot(Add23_tree, root_0);


                    }
                    break;
                case 2 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:50:12: Subtract ^
                    {
                    Subtract24=(Token)match(input,Subtract,FOLLOW_Subtract_in_unaryOperator345); 
                    Subtract24_tree = 
                    (Object)adaptor.create(Subtract24)
                    ;
                    root_0 = (Object)adaptor.becomeRoot(Subtract24_tree, root_0);


                    }
                    break;
                case 3 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:50:24: Not ^
                    {
                    Not25=(Token)match(input,Not,FOLLOW_Not_in_unaryOperator350); 
                    Not25_tree = 
                    (Object)adaptor.create(Not25)
                    ;
                    root_0 = (Object)adaptor.becomeRoot(Not25_tree, root_0);


                    }
                    break;

            }


            pushFollow(FOLLOW_exponent_in_unaryOperator355);
            exponent26=exponent();

            state._fsp--;

            adaptor.addChild(root_0, exponent26.getTree());

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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "unaryOperator"


    public static class exponent_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "exponent"
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:51:1: exponent : factor ( Exp ^ unaryOperator )? ;
    public final CalculationsParser.exponent_return exponent() throws RecognitionException {
        CalculationsParser.exponent_return retval = new CalculationsParser.exponent_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token Exp28=null;
        CalculationsParser.factor_return factor27 =null;

        CalculationsParser.unaryOperator_return unaryOperator29 =null;


        Object Exp28_tree=null;

        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:51:9: ( factor ( Exp ^ unaryOperator )? )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:51:11: factor ( Exp ^ unaryOperator )?
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_factor_in_exponent361);
            factor27=factor();

            state._fsp--;

            adaptor.addChild(root_0, factor27.getTree());

            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:51:18: ( Exp ^ unaryOperator )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==Exp) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:51:19: Exp ^ unaryOperator
                    {
                    Exp28=(Token)match(input,Exp,FOLLOW_Exp_in_exponent364); 
                    Exp28_tree = 
                    (Object)adaptor.create(Exp28)
                    ;
                    root_0 = (Object)adaptor.becomeRoot(Exp28_tree, root_0);


                    pushFollow(FOLLOW_unaryOperator_in_exponent367);
                    unaryOperator29=unaryOperator();

                    state._fsp--;

                    adaptor.addChild(root_0, unaryOperator29.getTree());

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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "exponent"


    public static class factor_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "factor"
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:53:1: factor : ( symbol | parenExpr );
    public final CalculationsParser.factor_return factor() throws RecognitionException {
        CalculationsParser.factor_return retval = new CalculationsParser.factor_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        CalculationsParser.symbol_return symbol30 =null;

        CalculationsParser.parenExpr_return parenExpr31 =null;



        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:53:8: ( symbol | parenExpr )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==Decimal||LA11_0==String||LA11_0==Variable) ) {
                alt11=1;
            }
            else if ( (LA11_0==OpenParen) ) {
                alt11=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }
            switch (alt11) {
                case 1 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:53:10: symbol
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_symbol_in_factor377);
                    symbol30=symbol();

                    state._fsp--;

                    adaptor.addChild(root_0, symbol30.getTree());

                    }
                    break;
                case 2 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:53:19: parenExpr
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_parenExpr_in_factor381);
                    parenExpr31=parenExpr();

                    state._fsp--;

                    adaptor.addChild(root_0, parenExpr31.getTree());

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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "factor"


    public static class parenExpr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "parenExpr"
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:55:1: parenExpr : OpenParen expr CloseParen -> expr ;
    public final CalculationsParser.parenExpr_return parenExpr() throws RecognitionException {
        CalculationsParser.parenExpr_return retval = new CalculationsParser.parenExpr_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token OpenParen32=null;
        Token CloseParen34=null;
        CalculationsParser.expr_return expr33 =null;


        Object OpenParen32_tree=null;
        Object CloseParen34_tree=null;
        RewriteRuleTokenStream stream_CloseParen=new RewriteRuleTokenStream(adaptor,"token CloseParen");
        RewriteRuleTokenStream stream_OpenParen=new RewriteRuleTokenStream(adaptor,"token OpenParen");
        RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:56:2: ( OpenParen expr CloseParen -> expr )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:56:4: OpenParen expr CloseParen
            {
            OpenParen32=(Token)match(input,OpenParen,FOLLOW_OpenParen_in_parenExpr390);  
            stream_OpenParen.add(OpenParen32);


            pushFollow(FOLLOW_expr_in_parenExpr392);
            expr33=expr();

            state._fsp--;

            stream_expr.add(expr33.getTree());

            CloseParen34=(Token)match(input,CloseParen,FOLLOW_CloseParen_in_parenExpr394);  
            stream_CloseParen.add(CloseParen34);


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
            // 56:30: -> expr
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "parenExpr"


    public static class symbol_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "symbol"
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:57:1: symbol : ( literal | Variable | function );
    public final CalculationsParser.symbol_return symbol() throws RecognitionException {
        CalculationsParser.symbol_return retval = new CalculationsParser.symbol_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token Variable36=null;
        CalculationsParser.literal_return literal35 =null;

        CalculationsParser.function_return function37 =null;


        Object Variable36_tree=null;

        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:57:8: ( literal | Variable | function )
            int alt12=3;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==Decimal||LA12_0==String) ) {
                alt12=1;
            }
            else if ( (LA12_0==Variable) ) {
                int LA12_2 = input.LA(2);

                if ( (LA12_2==OpenParen) ) {
                    alt12=3;
                }
                else if ( (LA12_2==EOF||(LA12_2 >= Add && LA12_2 <= And)||(LA12_2 >= CloseParen && LA12_2 <= Comma)||LA12_2==Divide||(LA12_2 >= Equals && LA12_2 <= Exp)||(LA12_2 >= GreaterThan && LA12_2 <= GreaterThanEqualTo)||(LA12_2 >= LessThan && LA12_2 <= LessThanEqualTo)||LA12_2==Multiply||LA12_2==NotEquals||LA12_2==Or||LA12_2==Subtract) ) {
                    alt12=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 2, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;

            }
            switch (alt12) {
                case 1 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:57:10: literal
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_literal_in_symbol405);
                    literal35=literal();

                    state._fsp--;

                    adaptor.addChild(root_0, literal35.getTree());

                    }
                    break;
                case 2 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:57:20: Variable
                    {
                    root_0 = (Object)adaptor.nil();


                    Variable36=(Token)match(input,Variable,FOLLOW_Variable_in_symbol409); 
                    Variable36_tree = 
                    (Object)adaptor.create(Variable36)
                    ;
                    adaptor.addChild(root_0, Variable36_tree);


                    }
                    break;
                case 3 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:57:31: function
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_function_in_symbol413);
                    function37=function();

                    state._fsp--;

                    adaptor.addChild(root_0, function37.getTree());

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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "symbol"


    public static class function_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "function"
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:58:1: function : Variable OpenParen ( expr ( Comma expr )* )? CloseParen -> ^( FuncEval Variable ( expr ( expr )* )? ) ;
    public final CalculationsParser.function_return function() throws RecognitionException {
        CalculationsParser.function_return retval = new CalculationsParser.function_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token Variable38=null;
        Token OpenParen39=null;
        Token Comma41=null;
        Token CloseParen43=null;
        CalculationsParser.expr_return expr40 =null;

        CalculationsParser.expr_return expr42 =null;


        Object Variable38_tree=null;
        Object OpenParen39_tree=null;
        Object Comma41_tree=null;
        Object CloseParen43_tree=null;
        RewriteRuleTokenStream stream_Variable=new RewriteRuleTokenStream(adaptor,"token Variable");
        RewriteRuleTokenStream stream_CloseParen=new RewriteRuleTokenStream(adaptor,"token CloseParen");
        RewriteRuleTokenStream stream_OpenParen=new RewriteRuleTokenStream(adaptor,"token OpenParen");
        RewriteRuleTokenStream stream_Comma=new RewriteRuleTokenStream(adaptor,"token Comma");
        RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");
        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:58:9: ( Variable OpenParen ( expr ( Comma expr )* )? CloseParen -> ^( FuncEval Variable ( expr ( expr )* )? ) )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:58:11: Variable OpenParen ( expr ( Comma expr )* )? CloseParen
            {
            Variable38=(Token)match(input,Variable,FOLLOW_Variable_in_function419);  
            stream_Variable.add(Variable38);


            OpenParen39=(Token)match(input,OpenParen,FOLLOW_OpenParen_in_function421);  
            stream_OpenParen.add(OpenParen39);


            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:58:30: ( expr ( Comma expr )* )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==Add||LA14_0==Decimal||LA14_0==Not||LA14_0==OpenParen||(LA14_0 >= String && LA14_0 <= Subtract)||LA14_0==Variable) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:58:31: expr ( Comma expr )*
                    {
                    pushFollow(FOLLOW_expr_in_function424);
                    expr40=expr();

                    state._fsp--;

                    stream_expr.add(expr40.getTree());

                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:58:36: ( Comma expr )*
                    loop13:
                    do {
                        int alt13=2;
                        int LA13_0 = input.LA(1);

                        if ( (LA13_0==Comma) ) {
                            alt13=1;
                        }


                        switch (alt13) {
                    	case 1 :
                    	    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:58:37: Comma expr
                    	    {
                    	    Comma41=(Token)match(input,Comma,FOLLOW_Comma_in_function427);  
                    	    stream_Comma.add(Comma41);


                    	    pushFollow(FOLLOW_expr_in_function429);
                    	    expr42=expr();

                    	    state._fsp--;

                    	    stream_expr.add(expr42.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop13;
                        }
                    } while (true);


                    }
                    break;

            }


            CloseParen43=(Token)match(input,CloseParen,FOLLOW_CloseParen_in_function435);  
            stream_CloseParen.add(CloseParen43);


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
            // 58:63: -> ^( FuncEval Variable ( expr ( expr )* )? )
            {
                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:58:66: ^( FuncEval Variable ( expr ( expr )* )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(FuncEval, "FuncEval")
                , root_1);

                adaptor.addChild(root_1, 
                stream_Variable.nextNode()
                );

                // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:58:86: ( expr ( expr )* )?
                if ( stream_expr.hasNext()||stream_expr.hasNext() ) {
                    adaptor.addChild(root_1, stream_expr.nextTree());

                    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:58:92: ( expr )*
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "function"


    public static class literal_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "literal"
    // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:62:1: literal : ( Decimal | String );
    public final CalculationsParser.literal_return literal() throws RecognitionException {
        CalculationsParser.literal_return retval = new CalculationsParser.literal_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set44=null;

        Object set44_tree=null;

        try {
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:62:9: ( Decimal | String )
            // /Users/Alan/Documents/EasyInsight/code/java/src/com/easyinsight/calculations/Calculations.g:
            {
            root_0 = (Object)adaptor.nil();


            set44=(Token)input.LT(1);

            if ( input.LA(1)==Decimal||input.LA(1)==String ) {
                input.consume();
                adaptor.addChild(root_0, 
                (Object)adaptor.create(set44)
                );
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "literal"

    // Delegated rules


 

    public static final BitSet FOLLOW_evaluation_in_expr212 = new BitSet(new long[]{0x0000000200000022L});
    public static final BitSet FOLLOW_And_in_expr216 = new BitSet(new long[]{0x0000013120000810L});
    public static final BitSet FOLLOW_Or_in_expr221 = new BitSet(new long[]{0x0000013120000810L});
    public static final BitSet FOLLOW_evaluation_in_expr225 = new BitSet(new long[]{0x0000000200000022L});
    public static final BitSet FOLLOW_expr_in_startExpr237 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_startExpr239 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sum_in_evaluation249 = new BitSet(new long[]{0x00000000418C8002L});
    public static final BitSet FOLLOW_LessThanEqualTo_in_evaluation253 = new BitSet(new long[]{0x0000013120000810L});
    public static final BitSet FOLLOW_GreaterThanEqualTo_in_evaluation258 = new BitSet(new long[]{0x0000013120000810L});
    public static final BitSet FOLLOW_Equals_in_evaluation263 = new BitSet(new long[]{0x0000013120000810L});
    public static final BitSet FOLLOW_NotEquals_in_evaluation268 = new BitSet(new long[]{0x0000013120000810L});
    public static final BitSet FOLLOW_GreaterThan_in_evaluation273 = new BitSet(new long[]{0x0000013120000810L});
    public static final BitSet FOLLOW_LessThan_in_evaluation278 = new BitSet(new long[]{0x0000013120000810L});
    public static final BitSet FOLLOW_sum_in_evaluation282 = new BitSet(new long[]{0x00000000418C8002L});
    public static final BitSet FOLLOW_term_in_sum292 = new BitSet(new long[]{0x0000002000000012L});
    public static final BitSet FOLLOW_Add_in_sum296 = new BitSet(new long[]{0x0000013120000810L});
    public static final BitSet FOLLOW_Subtract_in_sum301 = new BitSet(new long[]{0x0000013120000810L});
    public static final BitSet FOLLOW_term_in_sum305 = new BitSet(new long[]{0x0000002000000012L});
    public static final BitSet FOLLOW_unaryOperator_in_term315 = new BitSet(new long[]{0x0000000004002002L});
    public static final BitSet FOLLOW_Multiply_in_term319 = new BitSet(new long[]{0x0000013120000810L});
    public static final BitSet FOLLOW_Divide_in_term324 = new BitSet(new long[]{0x0000013120000810L});
    public static final BitSet FOLLOW_unaryOperator_in_term328 = new BitSet(new long[]{0x0000000004002002L});
    public static final BitSet FOLLOW_Add_in_unaryOperator340 = new BitSet(new long[]{0x0000011100000800L});
    public static final BitSet FOLLOW_Subtract_in_unaryOperator345 = new BitSet(new long[]{0x0000011100000800L});
    public static final BitSet FOLLOW_Not_in_unaryOperator350 = new BitSet(new long[]{0x0000011100000800L});
    public static final BitSet FOLLOW_exponent_in_unaryOperator355 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_factor_in_exponent361 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_Exp_in_exponent364 = new BitSet(new long[]{0x0000013120000810L});
    public static final BitSet FOLLOW_unaryOperator_in_exponent367 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_symbol_in_factor377 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parenExpr_in_factor381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OpenParen_in_parenExpr390 = new BitSet(new long[]{0x0000013120000810L});
    public static final BitSet FOLLOW_expr_in_parenExpr392 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_CloseParen_in_parenExpr394 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_symbol405 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Variable_in_symbol409 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_symbol413 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Variable_in_function419 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_OpenParen_in_function421 = new BitSet(new long[]{0x0000013120000A10L});
    public static final BitSet FOLLOW_expr_in_function424 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_Comma_in_function427 = new BitSet(new long[]{0x0000013120000810L});
    public static final BitSet FOLLOW_expr_in_function429 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_CloseParen_in_function435 = new BitSet(new long[]{0x0000000000000002L});

}