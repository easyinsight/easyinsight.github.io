package test;

import junit.framework.TestCase;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.TreeAdaptor;
import org.antlr.runtime.tree.CommonTreeAdaptor;

/**
 * User: James Boe
 * Date: Feb 9, 2008
 * Time: 1:40:17 PM
 */
public class LexerTest extends TestCase {
    public void testSomething() {
        try {
            ANTLRStringStream fs = new ANTLRStringStream("1+2+3");
            ConditionLexer lex = new ConditionLexer(fs);
            TokenRewriteStream tokens = new TokenRewriteStream(lex);
            ConditionParser grammar = new ConditionParser(tokens);
            grammar.setTreeAdaptor(adaptor);
            ConditionParser.expr_return ret = grammar.expr();
            CommonTree tree = (CommonTree) ret.getTree();
            printTree(tree, 0);
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
    }

    public void testAlanCode() {
        try {
            ANTLRStringStream fs = new ANTLRStringStream("a b * c(d - 30,5)");
            HmmLexer lex = new HmmLexer(fs);
            TokenRewriteStream tokens = new TokenRewriteStream(lex);
            HmmParser grammar = new HmmParser(tokens);
            grammar.setTreeAdaptor(adaptor);
            HmmParser.expr_return ret = grammar.expr();
            CommonTree tree = (CommonTree) ret.getTree();
            printTree(tree, 0);
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
    }

    public void printTree(CommonTree t, int indent) {
        if (t != null) {
            StringBuffer sb = new StringBuffer(indent);
            for (int i = 0; i < indent; i++)
                sb = sb.append("   ");
            for (int i = 0; i < t.getChildCount(); i++) {
                System.out.println(sb.toString() + t.getChild(i).toString());
                printTree((CommonTree) t.getChild(i), indent + 1);
            }
        }
    }

    static final TreeAdaptor adaptor = new CommonTreeAdaptor() {
        public Object create(Token payload) {
            
            if (payload != null) {
                switch (payload.getType()) {
                    case ConditionParser.PLUS:
                    case ConditionParser.MINUS:
                    case ConditionParser.MULT:
                    case ConditionParser.DIV:
                    case ConditionParser.NUMBER:
                        
                }
                System.out.println(payload.getType() + " - " + payload.getText());
            }
            return new CommonTree(payload);
        }
    };

    private static abstract class ASTNode extends CommonTree {
        public ASTNode(Token token) {
            super(token);
        }


    }

    private static class Number {
        private double number;
    }

    private static class Symbol {
        // symbols will need to resolve parantheses as well
    }

    private static class Comparison {

    }

    private static class Operator {
        
    }

    private static class Add extends Operator {
        // we need the lh and rh values here
        // 
    }

    private static class Subtract extends Operator {

    }

    private static class Multiply extends Operator {

    }

    private static class Divide extends Operator {

    }

    // we're gonna resolve into what exactly...
    // symbol
    // conditional
    // operator
    // left paren, right paren
    // number
}
