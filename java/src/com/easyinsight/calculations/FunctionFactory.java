package com.easyinsight.calculations;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 9:06:50 PM
 */
public class FunctionFactory {
    public IFunction createFunction(String s) {
        if(s.equals("ln")) {
            return new NaturalLog(); 
        }
        else if(s.equals("nconcat")) {
            return new NConcat();
        }
        else {
            return null;
        }
    }
}
