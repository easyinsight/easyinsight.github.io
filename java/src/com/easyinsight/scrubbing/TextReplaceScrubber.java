package com.easyinsight.scrubbing;

import com.easyinsight.analysis.IRow;
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
        } else {
            pattern = Pattern.compile(createWildcardPattern(scrub.getSourceText()));
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
                Matcher matcher = pattern.matcher(stringValue.getValue());
                if (matcher.matches()) {
                    row.getValues().put(entry.getKey(), new StringValue(scrub.getReplaceText()));   
                }
                
            }
        }
    }

    private String createWildcardPattern(String wildcard) {
        StringBuffer s = new StringBuffer(wildcard.length());
        s.append('^');
        for (int i = 0, is = wildcard.length(); i < is; i++) {
            char c = wildcard.charAt(i);
            switch(c) {
                case '*':
                    s.append(".*");
                    break;
                case '?':
                    s.append(".");
                    break;
                    // escape special regexp-characters
                case '(': case ')': case '[': case ']': case '$':
                case '^': case '.': case '{': case '}': case '|':
                case '\\':
                    s.append("\\");
                    s.append(c);
                    break;
                default:
                    s.append(c);
                    break;
            }
        }
        s.append('$');
        return s.toString();
    }
}
