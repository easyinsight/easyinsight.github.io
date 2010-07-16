package com.easyinsight.analysis;

import java.util.List;

/**
 * User: jamesboe
 * Date: Jul 14, 2010
 * Time: 11:49:58 PM
 */
public class FunctionExplanation {
    private String signature;
    private String description;
    private List<String> parameters;

    public FunctionExplanation() {
    }

    public FunctionExplanation(String signature, String description, List<String> parameters) {
        this.signature = signature;
        this.description = description;
        this.parameters = parameters;
    }

    public FunctionExplanation(String signature, String description) {
        this.signature = signature;
        this.description = description;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }
}
