package com.easyinsight.calculations;

import org.antlr.runtime.Token;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.StringValue;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jul 28, 2010
 * Time: 6:46:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringNode extends CalculationTreeNode {


    public StringNode(Token payload) {
        super(payload);
        str = payload.getText();
    }

    public StringValue getString() {
            return new StringValue(str);        
    }

    @Override
    public void accept(ICalculationTreeVisitor visitor) {
        visitor.visit(this);
    }

    private String str;
}
