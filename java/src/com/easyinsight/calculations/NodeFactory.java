package com.easyinsight.calculations;

import org.antlr.runtime.tree.*;
import org.antlr.runtime.Token;

import com.easyinsight.calculations.generated.*;

public class NodeFactory extends CommonTreeAdaptor {

    public NodeFactory() {
        super();
    }

    public Object create(Token payload) {
		Object retVal;

        if(payload == null) {
            return super.create(payload);    
        }

        switch(payload.getType())
		{
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
