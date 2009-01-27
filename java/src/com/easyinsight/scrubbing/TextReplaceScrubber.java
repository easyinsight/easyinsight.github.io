package com.easyinsight.scrubbing;

import com.easyinsight.IRow;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.core.StringValue;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * User: James Boe
 * Date: Jul 30, 2008
 * Time: 12:53:08 PM
 */
public class TextReplaceScrubber implements IScrubber {

    private TextReplaceScrub scrub;
    private Pattern pattern;
    private String sourceText;

    public TextReplaceScrubber(TextReplaceScrub scrub) {
        this.scrub = scrub;
        if (scrub.isRegex()) {
            pattern = Pattern.compile(scrub.getSourceText());
        }
        if (scrub.isCaseSensitive()) {
            sourceText = scrub.getSourceText().toLowerCase();
        } else {
            sourceText = scrub.getSourceText();
        }
    }

    public void apply(IRow row) {
        for (Map.Entry<Key, Value> entry : new HashMap<Key, Value>(row.getValues()).entrySet()) {
            Value value = entry.getValue();
            if (value.type() == Value.STRING) {
                StringValue stringValue = (StringValue) value;
                String newValue;
                if (scrub.isRegex()) {
                    Matcher matcher = pattern.matcher(stringValue.getValue());
                    newValue = matcher.replaceAll(scrub.getReplaceText()).trim();
                } else {
                    newValue = stringValue.getValue().replace(sourceText, scrub.getReplaceText()).trim();
                }
                stringValue = new StringValue(newValue);
                row.getValues().put(entry.getKey(), stringValue);
            }
        }
    }
}
