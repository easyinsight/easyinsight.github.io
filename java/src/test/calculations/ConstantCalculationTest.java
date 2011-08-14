package test.calculations;

import junit.framework.TestCase;
import com.easyinsight.calculations.generated.CalculationsLexer;
import com.easyinsight.calculations.generated.CalculationsParser;
import com.easyinsight.calculations.*;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 10, 2008
 * Time: 10:40:19 PM
 */
public class ConstantCalculationTest extends TestCase {

    /*public void testCreateTree() {
        assertEquals(23.0, evalString("23"));
    }

    public void testAdd() {
        assertEquals(23.0, evalString("20 + 3"));
    }

    public void testCalc() {
        assertEquals(46.0, evalString("23 + 27 - (6 + 2) * 4 ^ (-1/2)"));
    }

    public void testFunction() {
        assertEquals(Math.log(3.0), evalString("ln(3)"));
    }

    public void testLargeFunction() {
            assertEquals(12345.0, evalString("nconcat(1, 2, 3, 4 + (5 - 6) + 1, 5)"));
    }*/

    public void testBlah() {
        assertTrue(true);
    }


    private double evalString(String s) {
        ICalculationTreeVisitor visitor = null;
        CalculationsParser.expr_return ret;
        CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(s));
        CommonTokenStream tokes = new CommonTokenStream();
        tokes.setTokenSource(lexer);
        CalculationsParser parser = new CalculationsParser(tokes);
        parser.setTreeAdaptor(new NodeFactory());
        try {
            ret = parser.expr();
            CalculationTreeNode c = (CalculationTreeNode) ret.getTree();
            //visitor = new ResolverVisitor(null, new FunctionFactory());
            c.accept(visitor);
            visitor = new EvaluationVisitor();
            c.accept(visitor);

        } catch (RecognitionException e) {
            e.printStackTrace();
        }
        return visitor.getResult().toDouble();
    }
}
