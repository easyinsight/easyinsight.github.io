package com.easyinsight.calculations;

import com.easyinsight.core.Value;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;

/**
 * User: jamesboe
 * Date: 4/1/12
 * Time: 11:12 AM
 */
public class GetFunction extends Function {
    public Value evaluate() {
        String url = minusQuotes(0);
        HttpClient client = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        try {
            client.executeMethod(getMethod);
        } catch (IOException e) {
            throw new FunctionException(e.getMessage());
        }
        return null;
    }

    public int getParameterCount() {
        return 1;
    }
}
