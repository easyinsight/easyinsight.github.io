package com.easyinsight.analysis;

/**
 * User: jamesboe
 * Date: 8/21/14
 * Time: 2:27 PM
 */
public class TextDataResults extends DataResults {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public EmbeddedResults toEmbeddedResults() {
        EmbeddedTextDataResults textDataResults = new EmbeddedTextDataResults();
        textDataResults.setText(text);
        return textDataResults;
    }
}
