// $ANTLR 3.5.1 /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g 2013-12-11 13:55:32
 package com.easyinsight.calculations.generated; 

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings("all")
public class CalculationsParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "Add", "And", "BracketedVariable", 
		"Character", "CloseBrace", "CloseParen", "Comma", "Decimal", "Digit", 
		"Divide", "Dot", "Equals", "Exp", "FuncEval", "GreaterThan", "GreaterThanEqualTo", 
		"HideWhiteSpace", "Integer", "InternationalCharacters", "LessThan", "LessThanEqualTo", 
		"LowerCase", "Multiply", "NoBracketSpecialChars", "NoBracketsVariable", 
		"Not", "NotEquals", "OpenBrace", "OpenParen", "Or", "Quote", "SpecialChars", 
		"String", "Subtract", "UInteger", "UpperCase", "VariableSpecialChars", 
		"VariableToken", "VariableWhitespace", "Whitespace"
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
	public static final int VariableSpecialChars=40;
	public static final int VariableToken=41;
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
	@Override public String[] getTokenNames() { return CalculationsParser.tokenNames; }
	@Override public String getGrammarFileName() { return "/Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g"; }


	public static class expr_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "expr"
	// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:37:1: expr : evaluation ( ( And ^| Or ^) evaluation )* ;
	public final CalculationsParser.expr_return expr() throws RecognitionException {
		CalculationsParser.expr_return retval = new CalculationsParser.expr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token And2=null;
		Token Or3=null;
		ParserRuleReturnScope evaluation1 =null;
		ParserRuleReturnScope evaluation4 =null;

		Object And2_tree=null;
		Object Or3_tree=null;

		try {
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:37:6: ( evaluation ( ( And ^| Or ^) evaluation )* )
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:37:8: evaluation ( ( And ^| Or ^) evaluation )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_evaluation_in_expr216);
			evaluation1=evaluation();
			state._fsp--;

			adaptor.addChild(root_0, evaluation1.getTree());

			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:37:19: ( ( And ^| Or ^) evaluation )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( (LA2_0==And||LA2_0==Or) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:37:20: ( And ^| Or ^) evaluation
					{
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:37:20: ( And ^| Or ^)
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
							// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:37:21: And ^
							{
							And2=(Token)match(input,And,FOLLOW_And_in_expr220); 
							And2_tree = (Object)adaptor.create(And2);
							root_0 = (Object)adaptor.becomeRoot(And2_tree, root_0);

							}
							break;
						case 2 :
							// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:37:28: Or ^
							{
							Or3=(Token)match(input,Or,FOLLOW_Or_in_expr225); 
							Or3_tree = (Object)adaptor.create(Or3);
							root_0 = (Object)adaptor.becomeRoot(Or3_tree, root_0);

							}
							break;

					}

					pushFollow(FOLLOW_evaluation_in_expr229);
					evaluation4=evaluation();
					state._fsp--;

					adaptor.addChild(root_0, evaluation4.getTree());

					}
					break;

				default :
					break loop2;
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "startExpr"
	// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:40:1: startExpr : expr EOF !;
	public final CalculationsParser.startExpr_return startExpr() throws RecognitionException {
		CalculationsParser.startExpr_return retval = new CalculationsParser.startExpr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token EOF6=null;
		ParserRuleReturnScope expr5 =null;

		Object EOF6_tree=null;

		try {
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:41:2: ( expr EOF !)
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:41:4: expr EOF !
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_expr_in_startExpr241);
			expr5=expr();
			state._fsp--;

			adaptor.addChild(root_0, expr5.getTree());

			EOF6=(Token)match(input,EOF,FOLLOW_EOF_in_startExpr243); 
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "evaluation"
	// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:43:1: evaluation : sum ( ( LessThanEqualTo ^| GreaterThanEqualTo ^| Equals ^| NotEquals ^| GreaterThan ^| LessThan ^) sum )* ;
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
		ParserRuleReturnScope sum7 =null;
		ParserRuleReturnScope sum14 =null;

		Object LessThanEqualTo8_tree=null;
		Object GreaterThanEqualTo9_tree=null;
		Object Equals10_tree=null;
		Object NotEquals11_tree=null;
		Object GreaterThan12_tree=null;
		Object LessThan13_tree=null;

		try {
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:44:2: ( sum ( ( LessThanEqualTo ^| GreaterThanEqualTo ^| Equals ^| NotEquals ^| GreaterThan ^| LessThan ^) sum )* )
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:44:4: sum ( ( LessThanEqualTo ^| GreaterThanEqualTo ^| Equals ^| NotEquals ^| GreaterThan ^| LessThan ^) sum )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_sum_in_evaluation253);
			sum7=sum();
			state._fsp--;

			adaptor.addChild(root_0, sum7.getTree());

			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:44:8: ( ( LessThanEqualTo ^| GreaterThanEqualTo ^| Equals ^| NotEquals ^| GreaterThan ^| LessThan ^) sum )*
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( (LA4_0==Equals||(LA4_0 >= GreaterThan && LA4_0 <= GreaterThanEqualTo)||(LA4_0 >= LessThan && LA4_0 <= LessThanEqualTo)||LA4_0==NotEquals) ) {
					alt4=1;
				}

				switch (alt4) {
				case 1 :
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:44:9: ( LessThanEqualTo ^| GreaterThanEqualTo ^| Equals ^| NotEquals ^| GreaterThan ^| LessThan ^) sum
					{
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:44:9: ( LessThanEqualTo ^| GreaterThanEqualTo ^| Equals ^| NotEquals ^| GreaterThan ^| LessThan ^)
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
							// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:44:10: LessThanEqualTo ^
							{
							LessThanEqualTo8=(Token)match(input,LessThanEqualTo,FOLLOW_LessThanEqualTo_in_evaluation257); 
							LessThanEqualTo8_tree = (Object)adaptor.create(LessThanEqualTo8);
							root_0 = (Object)adaptor.becomeRoot(LessThanEqualTo8_tree, root_0);

							}
							break;
						case 2 :
							// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:44:29: GreaterThanEqualTo ^
							{
							GreaterThanEqualTo9=(Token)match(input,GreaterThanEqualTo,FOLLOW_GreaterThanEqualTo_in_evaluation262); 
							GreaterThanEqualTo9_tree = (Object)adaptor.create(GreaterThanEqualTo9);
							root_0 = (Object)adaptor.becomeRoot(GreaterThanEqualTo9_tree, root_0);

							}
							break;
						case 3 :
							// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:44:51: Equals ^
							{
							Equals10=(Token)match(input,Equals,FOLLOW_Equals_in_evaluation267); 
							Equals10_tree = (Object)adaptor.create(Equals10);
							root_0 = (Object)adaptor.becomeRoot(Equals10_tree, root_0);

							}
							break;
						case 4 :
							// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:44:61: NotEquals ^
							{
							NotEquals11=(Token)match(input,NotEquals,FOLLOW_NotEquals_in_evaluation272); 
							NotEquals11_tree = (Object)adaptor.create(NotEquals11);
							root_0 = (Object)adaptor.becomeRoot(NotEquals11_tree, root_0);

							}
							break;
						case 5 :
							// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:44:74: GreaterThan ^
							{
							GreaterThan12=(Token)match(input,GreaterThan,FOLLOW_GreaterThan_in_evaluation277); 
							GreaterThan12_tree = (Object)adaptor.create(GreaterThan12);
							root_0 = (Object)adaptor.becomeRoot(GreaterThan12_tree, root_0);

							}
							break;
						case 6 :
							// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:44:89: LessThan ^
							{
							LessThan13=(Token)match(input,LessThan,FOLLOW_LessThan_in_evaluation282); 
							LessThan13_tree = (Object)adaptor.create(LessThan13);
							root_0 = (Object)adaptor.becomeRoot(LessThan13_tree, root_0);

							}
							break;

					}

					pushFollow(FOLLOW_sum_in_evaluation286);
					sum14=sum();
					state._fsp--;

					adaptor.addChild(root_0, sum14.getTree());

					}
					break;

				default :
					break loop4;
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "sum"
	// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:46:1: sum : term ( ( Add ^| Subtract ^) term )* ;
	public final CalculationsParser.sum_return sum() throws RecognitionException {
		CalculationsParser.sum_return retval = new CalculationsParser.sum_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token Add16=null;
		Token Subtract17=null;
		ParserRuleReturnScope term15 =null;
		ParserRuleReturnScope term18 =null;

		Object Add16_tree=null;
		Object Subtract17_tree=null;

		try {
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:46:5: ( term ( ( Add ^| Subtract ^) term )* )
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:46:7: term ( ( Add ^| Subtract ^) term )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_term_in_sum296);
			term15=term();
			state._fsp--;

			adaptor.addChild(root_0, term15.getTree());

			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:46:12: ( ( Add ^| Subtract ^) term )*
			loop6:
			while (true) {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( (LA6_0==Add||LA6_0==Subtract) ) {
					alt6=1;
				}

				switch (alt6) {
				case 1 :
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:46:13: ( Add ^| Subtract ^) term
					{
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:46:13: ( Add ^| Subtract ^)
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
							// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:46:14: Add ^
							{
							Add16=(Token)match(input,Add,FOLLOW_Add_in_sum300); 
							Add16_tree = (Object)adaptor.create(Add16);
							root_0 = (Object)adaptor.becomeRoot(Add16_tree, root_0);

							}
							break;
						case 2 :
							// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:46:21: Subtract ^
							{
							Subtract17=(Token)match(input,Subtract,FOLLOW_Subtract_in_sum305); 
							Subtract17_tree = (Object)adaptor.create(Subtract17);
							root_0 = (Object)adaptor.becomeRoot(Subtract17_tree, root_0);

							}
							break;

					}

					pushFollow(FOLLOW_term_in_sum309);
					term18=term();
					state._fsp--;

					adaptor.addChild(root_0, term18.getTree());

					}
					break;

				default :
					break loop6;
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "term"
	// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:48:1: term : unaryOperator ( ( Multiply ^| Divide ^) unaryOperator )* ;
	public final CalculationsParser.term_return term() throws RecognitionException {
		CalculationsParser.term_return retval = new CalculationsParser.term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token Multiply20=null;
		Token Divide21=null;
		ParserRuleReturnScope unaryOperator19 =null;
		ParserRuleReturnScope unaryOperator22 =null;

		Object Multiply20_tree=null;
		Object Divide21_tree=null;

		try {
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:48:6: ( unaryOperator ( ( Multiply ^| Divide ^) unaryOperator )* )
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:48:8: unaryOperator ( ( Multiply ^| Divide ^) unaryOperator )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_unaryOperator_in_term319);
			unaryOperator19=unaryOperator();
			state._fsp--;

			adaptor.addChild(root_0, unaryOperator19.getTree());

			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:48:22: ( ( Multiply ^| Divide ^) unaryOperator )*
			loop8:
			while (true) {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( (LA8_0==Divide||LA8_0==Multiply) ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:48:23: ( Multiply ^| Divide ^) unaryOperator
					{
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:48:23: ( Multiply ^| Divide ^)
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
							// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:48:24: Multiply ^
							{
							Multiply20=(Token)match(input,Multiply,FOLLOW_Multiply_in_term323); 
							Multiply20_tree = (Object)adaptor.create(Multiply20);
							root_0 = (Object)adaptor.becomeRoot(Multiply20_tree, root_0);

							}
							break;
						case 2 :
							// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:48:36: Divide ^
							{
							Divide21=(Token)match(input,Divide,FOLLOW_Divide_in_term328); 
							Divide21_tree = (Object)adaptor.create(Divide21);
							root_0 = (Object)adaptor.becomeRoot(Divide21_tree, root_0);

							}
							break;

					}

					pushFollow(FOLLOW_unaryOperator_in_term332);
					unaryOperator22=unaryOperator();
					state._fsp--;

					adaptor.addChild(root_0, unaryOperator22.getTree());

					}
					break;

				default :
					break loop8;
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "unaryOperator"
	// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:50:1: unaryOperator : ( Add ^| Subtract ^| Not ^)? exponent ;
	public final CalculationsParser.unaryOperator_return unaryOperator() throws RecognitionException {
		CalculationsParser.unaryOperator_return retval = new CalculationsParser.unaryOperator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token Add23=null;
		Token Subtract24=null;
		Token Not25=null;
		ParserRuleReturnScope exponent26 =null;

		Object Add23_tree=null;
		Object Subtract24_tree=null;
		Object Not25_tree=null;

		try {
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:51:2: ( ( Add ^| Subtract ^| Not ^)? exponent )
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:51:4: ( Add ^| Subtract ^| Not ^)? exponent
			{
			root_0 = (Object)adaptor.nil();


			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:51:4: ( Add ^| Subtract ^| Not ^)?
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
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:51:5: Add ^
					{
					Add23=(Token)match(input,Add,FOLLOW_Add_in_unaryOperator344); 
					Add23_tree = (Object)adaptor.create(Add23);
					root_0 = (Object)adaptor.becomeRoot(Add23_tree, root_0);

					}
					break;
				case 2 :
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:51:12: Subtract ^
					{
					Subtract24=(Token)match(input,Subtract,FOLLOW_Subtract_in_unaryOperator349); 
					Subtract24_tree = (Object)adaptor.create(Subtract24);
					root_0 = (Object)adaptor.becomeRoot(Subtract24_tree, root_0);

					}
					break;
				case 3 :
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:51:24: Not ^
					{
					Not25=(Token)match(input,Not,FOLLOW_Not_in_unaryOperator354); 
					Not25_tree = (Object)adaptor.create(Not25);
					root_0 = (Object)adaptor.becomeRoot(Not25_tree, root_0);

					}
					break;

			}

			pushFollow(FOLLOW_exponent_in_unaryOperator359);
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "exponent"
	// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:52:1: exponent : factor ( Exp ^ unaryOperator )? ;
	public final CalculationsParser.exponent_return exponent() throws RecognitionException {
		CalculationsParser.exponent_return retval = new CalculationsParser.exponent_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token Exp28=null;
		ParserRuleReturnScope factor27 =null;
		ParserRuleReturnScope unaryOperator29 =null;

		Object Exp28_tree=null;

		try {
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:52:9: ( factor ( Exp ^ unaryOperator )? )
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:52:11: factor ( Exp ^ unaryOperator )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_factor_in_exponent365);
			factor27=factor();
			state._fsp--;

			adaptor.addChild(root_0, factor27.getTree());

			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:52:18: ( Exp ^ unaryOperator )?
			int alt10=2;
			int LA10_0 = input.LA(1);
			if ( (LA10_0==Exp) ) {
				alt10=1;
			}
			switch (alt10) {
				case 1 :
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:52:19: Exp ^ unaryOperator
					{
					Exp28=(Token)match(input,Exp,FOLLOW_Exp_in_exponent368); 
					Exp28_tree = (Object)adaptor.create(Exp28);
					root_0 = (Object)adaptor.becomeRoot(Exp28_tree, root_0);

					pushFollow(FOLLOW_unaryOperator_in_exponent371);
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "factor"
	// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:54:1: factor : ( symbol | parenExpr );
	public final CalculationsParser.factor_return factor() throws RecognitionException {
		CalculationsParser.factor_return retval = new CalculationsParser.factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope symbol30 =null;
		ParserRuleReturnScope parenExpr31 =null;


		try {
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:54:8: ( symbol | parenExpr )
			int alt11=2;
			int LA11_0 = input.LA(1);
			if ( (LA11_0==BracketedVariable||LA11_0==Decimal||LA11_0==NoBracketsVariable||LA11_0==String) ) {
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
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:54:10: symbol
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_symbol_in_factor381);
					symbol30=symbol();
					state._fsp--;

					adaptor.addChild(root_0, symbol30.getTree());

					}
					break;
				case 2 :
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:54:19: parenExpr
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_parenExpr_in_factor385);
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "parenExpr"
	// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:56:1: parenExpr : OpenParen expr CloseParen -> expr ;
	public final CalculationsParser.parenExpr_return parenExpr() throws RecognitionException {
		CalculationsParser.parenExpr_return retval = new CalculationsParser.parenExpr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token OpenParen32=null;
		Token CloseParen34=null;
		ParserRuleReturnScope expr33 =null;

		Object OpenParen32_tree=null;
		Object CloseParen34_tree=null;
		RewriteRuleTokenStream stream_CloseParen=new RewriteRuleTokenStream(adaptor,"token CloseParen");
		RewriteRuleTokenStream stream_OpenParen=new RewriteRuleTokenStream(adaptor,"token OpenParen");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:57:2: ( OpenParen expr CloseParen -> expr )
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:57:4: OpenParen expr CloseParen
			{
			OpenParen32=(Token)match(input,OpenParen,FOLLOW_OpenParen_in_parenExpr394);  
			stream_OpenParen.add(OpenParen32);

			pushFollow(FOLLOW_expr_in_parenExpr396);
			expr33=expr();
			state._fsp--;

			stream_expr.add(expr33.getTree());
			CloseParen34=(Token)match(input,CloseParen,FOLLOW_CloseParen_in_parenExpr398);  
			stream_CloseParen.add(CloseParen34);

			// AST REWRITE
			// elements: expr
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 57:30: -> expr
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "symbol"
	// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:58:1: symbol : ( literal | variable | function );
	public final CalculationsParser.symbol_return symbol() throws RecognitionException {
		CalculationsParser.symbol_return retval = new CalculationsParser.symbol_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope literal35 =null;
		ParserRuleReturnScope variable36 =null;
		ParserRuleReturnScope function37 =null;


		try {
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:58:8: ( literal | variable | function )
			int alt12=3;
			alt12 = dfa12.predict(input);
			switch (alt12) {
				case 1 :
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:58:10: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_symbol409);
					literal35=literal();
					state._fsp--;

					adaptor.addChild(root_0, literal35.getTree());

					}
					break;
				case 2 :
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:58:20: variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_variable_in_symbol413);
					variable36=variable();
					state._fsp--;

					adaptor.addChild(root_0, variable36.getTree());

					}
					break;
				case 3 :
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:58:31: function
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_in_symbol417);
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "function"
	// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:59:1: function : variable OpenParen ( expr ( Comma expr )* )? CloseParen -> ^( FuncEval variable ( expr ( expr )* )? ) ;
	public final CalculationsParser.function_return function() throws RecognitionException {
		CalculationsParser.function_return retval = new CalculationsParser.function_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token OpenParen39=null;
		Token Comma41=null;
		Token CloseParen43=null;
		ParserRuleReturnScope variable38 =null;
		ParserRuleReturnScope expr40 =null;
		ParserRuleReturnScope expr42 =null;

		Object OpenParen39_tree=null;
		Object Comma41_tree=null;
		Object CloseParen43_tree=null;
		RewriteRuleTokenStream stream_CloseParen=new RewriteRuleTokenStream(adaptor,"token CloseParen");
		RewriteRuleTokenStream stream_OpenParen=new RewriteRuleTokenStream(adaptor,"token OpenParen");
		RewriteRuleTokenStream stream_Comma=new RewriteRuleTokenStream(adaptor,"token Comma");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");
		RewriteRuleSubtreeStream stream_variable=new RewriteRuleSubtreeStream(adaptor,"rule variable");

		try {
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:59:9: ( variable OpenParen ( expr ( Comma expr )* )? CloseParen -> ^( FuncEval variable ( expr ( expr )* )? ) )
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:59:11: variable OpenParen ( expr ( Comma expr )* )? CloseParen
			{
			pushFollow(FOLLOW_variable_in_function423);
			variable38=variable();
			state._fsp--;

			stream_variable.add(variable38.getTree());
			OpenParen39=(Token)match(input,OpenParen,FOLLOW_OpenParen_in_function425);  
			stream_OpenParen.add(OpenParen39);

			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:59:30: ( expr ( Comma expr )* )?
			int alt14=2;
			int LA14_0 = input.LA(1);
			if ( (LA14_0==Add||LA14_0==BracketedVariable||LA14_0==Decimal||(LA14_0 >= NoBracketsVariable && LA14_0 <= Not)||LA14_0==OpenParen||(LA14_0 >= String && LA14_0 <= Subtract)) ) {
				alt14=1;
			}
			switch (alt14) {
				case 1 :
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:59:31: expr ( Comma expr )*
					{
					pushFollow(FOLLOW_expr_in_function428);
					expr40=expr();
					state._fsp--;

					stream_expr.add(expr40.getTree());
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:59:36: ( Comma expr )*
					loop13:
					while (true) {
						int alt13=2;
						int LA13_0 = input.LA(1);
						if ( (LA13_0==Comma) ) {
							alt13=1;
						}

						switch (alt13) {
						case 1 :
							// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:59:37: Comma expr
							{
							Comma41=(Token)match(input,Comma,FOLLOW_Comma_in_function431);  
							stream_Comma.add(Comma41);

							pushFollow(FOLLOW_expr_in_function433);
							expr42=expr();
							state._fsp--;

							stream_expr.add(expr42.getTree());
							}
							break;

						default :
							break loop13;
						}
					}

					}
					break;

			}

			CloseParen43=(Token)match(input,CloseParen,FOLLOW_CloseParen_in_function439);  
			stream_CloseParen.add(CloseParen43);

			// AST REWRITE
			// elements: expr, expr, variable
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 59:63: -> ^( FuncEval variable ( expr ( expr )* )? )
			{
				// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:59:66: ^( FuncEval variable ( expr ( expr )* )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FuncEval, "FuncEval"), root_1);
				adaptor.addChild(root_1, stream_variable.nextTree());
				// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:59:86: ( expr ( expr )* )?
				if ( stream_expr.hasNext()||stream_expr.hasNext() ) {
					adaptor.addChild(root_1, stream_expr.nextTree());
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:59:92: ( expr )*
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


	public static class variable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "variable"
	// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:61:1: variable : ( namespacedVariable | normalVariable ) ;
	public final CalculationsParser.variable_return variable() throws RecognitionException {
		CalculationsParser.variable_return retval = new CalculationsParser.variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope namespacedVariable44 =null;
		ParserRuleReturnScope normalVariable45 =null;


		try {
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:61:9: ( ( namespacedVariable | normalVariable ) )
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:61:11: ( namespacedVariable | normalVariable )
			{
			root_0 = (Object)adaptor.nil();


			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:61:11: ( namespacedVariable | normalVariable )
			int alt15=2;
			int LA15_0 = input.LA(1);
			if ( (LA15_0==BracketedVariable) ) {
				alt15=1;
			}
			else if ( (LA15_0==NoBracketsVariable) ) {
				alt15=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 15, 0, input);
				throw nvae;
			}

			switch (alt15) {
				case 1 :
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:61:12: namespacedVariable
					{
					pushFollow(FOLLOW_namespacedVariable_in_variable465);
					namespacedVariable44=namespacedVariable();
					state._fsp--;

					adaptor.addChild(root_0, namespacedVariable44.getTree());

					}
					break;
				case 2 :
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:61:33: normalVariable
					{
					pushFollow(FOLLOW_normalVariable_in_variable469);
					normalVariable45=normalVariable();
					state._fsp--;

					adaptor.addChild(root_0, normalVariable45.getTree());

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
	// $ANTLR end "variable"


	public static class namespacedVariable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "namespacedVariable"
	// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:62:1: namespacedVariable : ( BracketedVariable ( Dot BracketedVariable )* ) -> ^( VariableToken BracketedVariable ( BracketedVariable )* ) ;
	public final CalculationsParser.namespacedVariable_return namespacedVariable() throws RecognitionException {
		CalculationsParser.namespacedVariable_return retval = new CalculationsParser.namespacedVariable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token BracketedVariable46=null;
		Token Dot47=null;
		Token BracketedVariable48=null;

		Object BracketedVariable46_tree=null;
		Object Dot47_tree=null;
		Object BracketedVariable48_tree=null;
		RewriteRuleTokenStream stream_Dot=new RewriteRuleTokenStream(adaptor,"token Dot");
		RewriteRuleTokenStream stream_BracketedVariable=new RewriteRuleTokenStream(adaptor,"token BracketedVariable");

		try {
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:63:2: ( ( BracketedVariable ( Dot BracketedVariable )* ) -> ^( VariableToken BracketedVariable ( BracketedVariable )* ) )
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:63:4: ( BracketedVariable ( Dot BracketedVariable )* )
			{
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:63:4: ( BracketedVariable ( Dot BracketedVariable )* )
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:63:5: BracketedVariable ( Dot BracketedVariable )*
			{
			BracketedVariable46=(Token)match(input,BracketedVariable,FOLLOW_BracketedVariable_in_namespacedVariable479);  
			stream_BracketedVariable.add(BracketedVariable46);

			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:63:23: ( Dot BracketedVariable )*
			loop16:
			while (true) {
				int alt16=2;
				int LA16_0 = input.LA(1);
				if ( (LA16_0==Dot) ) {
					alt16=1;
				}

				switch (alt16) {
				case 1 :
					// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:63:24: Dot BracketedVariable
					{
					Dot47=(Token)match(input,Dot,FOLLOW_Dot_in_namespacedVariable482);  
					stream_Dot.add(Dot47);

					BracketedVariable48=(Token)match(input,BracketedVariable,FOLLOW_BracketedVariable_in_namespacedVariable484);  
					stream_BracketedVariable.add(BracketedVariable48);

					}
					break;

				default :
					break loop16;
				}
			}

			}

			// AST REWRITE
			// elements: BracketedVariable, BracketedVariable
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 63:49: -> ^( VariableToken BracketedVariable ( BracketedVariable )* )
			{
				// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:63:52: ^( VariableToken BracketedVariable ( BracketedVariable )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(VariableToken, "VariableToken"), root_1);
				adaptor.addChild(root_1, stream_BracketedVariable.nextNode());
				// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:63:86: ( BracketedVariable )*
				while ( stream_BracketedVariable.hasNext() ) {
					adaptor.addChild(root_1, stream_BracketedVariable.nextNode());
				}
				stream_BracketedVariable.reset();

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
	// $ANTLR end "namespacedVariable"


	public static class normalVariable_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "normalVariable"
	// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:65:1: normalVariable : NoBracketsVariable -> ^( VariableToken NoBracketsVariable ) ;
	public final CalculationsParser.normalVariable_return normalVariable() throws RecognitionException {
		CalculationsParser.normalVariable_return retval = new CalculationsParser.normalVariable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token NoBracketsVariable49=null;

		Object NoBracketsVariable49_tree=null;
		RewriteRuleTokenStream stream_NoBracketsVariable=new RewriteRuleTokenStream(adaptor,"token NoBracketsVariable");

		try {
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:65:15: ( NoBracketsVariable -> ^( VariableToken NoBracketsVariable ) )
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:65:17: NoBracketsVariable
			{
			NoBracketsVariable49=(Token)match(input,NoBracketsVariable,FOLLOW_NoBracketsVariable_in_normalVariable505);  
			stream_NoBracketsVariable.add(NoBracketsVariable49);

			// AST REWRITE
			// elements: NoBracketsVariable
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 65:36: -> ^( VariableToken NoBracketsVariable )
			{
				// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:65:39: ^( VariableToken NoBracketsVariable )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(VariableToken, "VariableToken"), root_1);
				adaptor.addChild(root_1, stream_NoBracketsVariable.nextNode());
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
	// $ANTLR end "normalVariable"


	public static class literal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "literal"
	// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:71:1: literal : ( Decimal | String );
	public final CalculationsParser.literal_return literal() throws RecognitionException {
		CalculationsParser.literal_return retval = new CalculationsParser.literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set50=null;

		Object set50_tree=null;

		try {
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:71:9: ( Decimal | String )
			// /Users/Alan/Documents/EIGit/easyinsight/java/src/com/easyinsight/calculations/Calculations.g:
			{
			root_0 = (Object)adaptor.nil();


			set50=input.LT(1);
			if ( input.LA(1)==Decimal||input.LA(1)==String ) {
				input.consume();
				adaptor.addChild(root_0, (Object)adaptor.create(set50));
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


	protected DFA12 dfa12 = new DFA12(this);
	static final String DFA12_eotS =
		"\10\uffff";
	static final String DFA12_eofS =
		"\2\uffff\2\5\3\uffff\1\5";
	static final String DFA12_minS =
		"\1\6\1\uffff\2\4\1\6\2\uffff\1\4";
	static final String DFA12_maxS =
		"\1\44\1\uffff\2\45\1\6\2\uffff\1\45";
	static final String DFA12_acceptS =
		"\1\uffff\1\1\3\uffff\1\2\1\3\1\uffff";
	static final String DFA12_specialS =
		"\10\uffff}>";
	static final String[] DFA12_transitionS = {
			"\1\2\4\uffff\1\1\20\uffff\1\3\7\uffff\1\1",
			"",
			"\2\5\3\uffff\2\5\2\uffff\1\5\1\4\2\5\1\uffff\2\5\3\uffff\2\5\1\uffff"+
			"\1\5\3\uffff\1\5\1\uffff\1\6\1\5\3\uffff\1\5",
			"\2\5\3\uffff\2\5\2\uffff\1\5\1\uffff\2\5\1\uffff\2\5\3\uffff\2\5\1\uffff"+
			"\1\5\3\uffff\1\5\1\uffff\1\6\1\5\3\uffff\1\5",
			"\1\7",
			"",
			"",
			"\2\5\3\uffff\2\5\2\uffff\1\5\1\4\2\5\1\uffff\2\5\3\uffff\2\5\1\uffff"+
			"\1\5\3\uffff\1\5\1\uffff\1\6\1\5\3\uffff\1\5"
	};

	static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
	static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
	static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
	static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
	static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
	static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
	static final short[][] DFA12_transition;

	static {
		int numStates = DFA12_transitionS.length;
		DFA12_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
		}
	}

	protected class DFA12 extends DFA {

		public DFA12(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 12;
			this.eot = DFA12_eot;
			this.eof = DFA12_eof;
			this.min = DFA12_min;
			this.max = DFA12_max;
			this.accept = DFA12_accept;
			this.special = DFA12_special;
			this.transition = DFA12_transition;
		}
		@Override
		public String getDescription() {
			return "58:1: symbol : ( literal | variable | function );";
		}
	}

	public static final BitSet FOLLOW_evaluation_in_expr216 = new BitSet(new long[]{0x0000000200000022L});
	public static final BitSet FOLLOW_And_in_expr220 = new BitSet(new long[]{0x0000003130000850L});
	public static final BitSet FOLLOW_Or_in_expr225 = new BitSet(new long[]{0x0000003130000850L});
	public static final BitSet FOLLOW_evaluation_in_expr229 = new BitSet(new long[]{0x0000000200000022L});
	public static final BitSet FOLLOW_expr_in_startExpr241 = new BitSet(new long[]{0x0000000000000000L});
	public static final BitSet FOLLOW_EOF_in_startExpr243 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sum_in_evaluation253 = new BitSet(new long[]{0x00000000418C8002L});
	public static final BitSet FOLLOW_LessThanEqualTo_in_evaluation257 = new BitSet(new long[]{0x0000003130000850L});
	public static final BitSet FOLLOW_GreaterThanEqualTo_in_evaluation262 = new BitSet(new long[]{0x0000003130000850L});
	public static final BitSet FOLLOW_Equals_in_evaluation267 = new BitSet(new long[]{0x0000003130000850L});
	public static final BitSet FOLLOW_NotEquals_in_evaluation272 = new BitSet(new long[]{0x0000003130000850L});
	public static final BitSet FOLLOW_GreaterThan_in_evaluation277 = new BitSet(new long[]{0x0000003130000850L});
	public static final BitSet FOLLOW_LessThan_in_evaluation282 = new BitSet(new long[]{0x0000003130000850L});
	public static final BitSet FOLLOW_sum_in_evaluation286 = new BitSet(new long[]{0x00000000418C8002L});
	public static final BitSet FOLLOW_term_in_sum296 = new BitSet(new long[]{0x0000002000000012L});
	public static final BitSet FOLLOW_Add_in_sum300 = new BitSet(new long[]{0x0000003130000850L});
	public static final BitSet FOLLOW_Subtract_in_sum305 = new BitSet(new long[]{0x0000003130000850L});
	public static final BitSet FOLLOW_term_in_sum309 = new BitSet(new long[]{0x0000002000000012L});
	public static final BitSet FOLLOW_unaryOperator_in_term319 = new BitSet(new long[]{0x0000000004002002L});
	public static final BitSet FOLLOW_Multiply_in_term323 = new BitSet(new long[]{0x0000003130000850L});
	public static final BitSet FOLLOW_Divide_in_term328 = new BitSet(new long[]{0x0000003130000850L});
	public static final BitSet FOLLOW_unaryOperator_in_term332 = new BitSet(new long[]{0x0000000004002002L});
	public static final BitSet FOLLOW_Add_in_unaryOperator344 = new BitSet(new long[]{0x0000001110000840L});
	public static final BitSet FOLLOW_Subtract_in_unaryOperator349 = new BitSet(new long[]{0x0000001110000840L});
	public static final BitSet FOLLOW_Not_in_unaryOperator354 = new BitSet(new long[]{0x0000001110000840L});
	public static final BitSet FOLLOW_exponent_in_unaryOperator359 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_factor_in_exponent365 = new BitSet(new long[]{0x0000000000010002L});
	public static final BitSet FOLLOW_Exp_in_exponent368 = new BitSet(new long[]{0x0000003130000850L});
	public static final BitSet FOLLOW_unaryOperator_in_exponent371 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_symbol_in_factor381 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_parenExpr_in_factor385 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OpenParen_in_parenExpr394 = new BitSet(new long[]{0x0000003130000850L});
	public static final BitSet FOLLOW_expr_in_parenExpr396 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_CloseParen_in_parenExpr398 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_symbol409 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_variable_in_symbol413 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_in_symbol417 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_variable_in_function423 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_OpenParen_in_function425 = new BitSet(new long[]{0x0000003130000A50L});
	public static final BitSet FOLLOW_expr_in_function428 = new BitSet(new long[]{0x0000000000000600L});
	public static final BitSet FOLLOW_Comma_in_function431 = new BitSet(new long[]{0x0000003130000850L});
	public static final BitSet FOLLOW_expr_in_function433 = new BitSet(new long[]{0x0000000000000600L});
	public static final BitSet FOLLOW_CloseParen_in_function439 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_namespacedVariable_in_variable465 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_normalVariable_in_variable469 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_BracketedVariable_in_namespacedVariable479 = new BitSet(new long[]{0x0000000000004002L});
	public static final BitSet FOLLOW_Dot_in_namespacedVariable482 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_BracketedVariable_in_namespacedVariable484 = new BitSet(new long[]{0x0000000000004002L});
	public static final BitSet FOLLOW_NoBracketsVariable_in_normalVariable505 = new BitSet(new long[]{0x0000000000000002L});
}
