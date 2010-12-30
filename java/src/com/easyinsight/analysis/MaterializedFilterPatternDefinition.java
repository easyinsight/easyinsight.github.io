package com.easyinsight.analysis;

import com.easyinsight.core.Value;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

/**
 * User: jamesboe
 * Date: Aug 7, 2009
 * Time: 11:04:43 AM
 */
public class MaterializedFilterPatternDefinition extends MaterializedFilterDefinition {

    private boolean caseSensitive;
    private Pattern pattern;

    public MaterializedFilterPatternDefinition(AnalysisItem key, String pattern, boolean caseSensitive, boolean regex) {
        super(key);
        if (pattern == null) {
            pattern = "";
        }
        if (!"".equals(pattern)) {
            if (regex) {
                try {
                    this.pattern = Pattern.compile(caseSensitive ? pattern : pattern.toLowerCase());
                } catch (PatternSyntaxException pse) {
                    throw new ReportException(new GenericReportFault(pse.getMessage()));
                }
            } else {
                this.pattern = Pattern.compile(caseSensitive ? createWildcardPattern(pattern) : createWildcardPattern(pattern.toLowerCase()));
            }
        }
        this.caseSensitive = caseSensitive;

    }

    public boolean allows(Value value) {
        if (pattern == null) {
            return true;
        }
        String string = value.toString();
        if (!caseSensitive) {
            string = string.toLowerCase();
        }
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
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
