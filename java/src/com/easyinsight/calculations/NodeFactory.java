package com.easyinsight.calculations;

import com.easyinsight.calculations.*;
import com.easyinsight.calculations.generated.CalculationsLexer;
import com.sun.org.apache.xpath.internal.operations.NotEquals;
import com.sun.tools.corba.se.idl.constExpr.LessThan;
import org.antlr.runtime.tree.*;
import org.antlr.runtime.Token;

public class NodeFactory extends CommonTreeAdaptor {

    public NodeFactory() {
        super();
    }

    public Object create(Token payload) {
        Object retVal;

        if (payload == null) {
            return super.create(payload);
        }

        switch (payload.getType()) {

            // boolean logic
            case CalculationsLexer.And:
                retVal = new AndNode(payload);
                break;
            case CalculationsLexer.Or:
                retVal = new OrNode(payload);
                break;
            case CalculationsLexer.Not:
                retVal = new NotNode(payload);
                break;
            // comparison logic
            case CalculationsLexer.Equals:
                retVal = new EqualsNode(payload);
                break;
            case CalculationsLexer.NotEquals:
                retVal = new NotEqualsNode(payload);
                break;
            case CalculationsLexer.GreaterThan:
                retVal = new GreaterThanNode(payload);
                break;
            case CalculationsLexer.LessThan:
                retVal = new LessThanNode(payload);
                break;
            case CalculationsLexer.GreaterThanEqualTo:
                retVal = new GreaterThanEqualToNode(payload);
                break;
            case CalculationsLexer.LessThanEqualTo:
                retVal = new LessThanEqualToNode(payload);
                break;


            // math
            case CalculationsLexer.Add:
                retVal = new AddNode(payload);
                break;
            case CalculationsLexer.Subtract:
                retVal = new SubtractNode(payload);
                break;
            case CalculationsLexer.Multiply:
                retVal = new MultiplyNode(payload);
                break;
            case CalculationsLexer.Divide:
                retVal = new DivideNode(payload);
                break;
            case CalculationsLexer.Variable:
                retVal = new VariableNode(payload);
                break;
            case CalculationsLexer.Decimal:
                retVal = new NumberNode(payload);
                break;
            case CalculationsLexer.String:
                retVal = new StringNode(payload);
                break;
            case CalculationsLexer.FuncEval:
                retVal = new FunctionNode(payload);
                break;
            case CalculationsLexer.Exp:
                retVal = new ExponentNode(payload);
                break;
            default:
                retVal = super.create(payload);
        }
        return retVal;
    }
}
