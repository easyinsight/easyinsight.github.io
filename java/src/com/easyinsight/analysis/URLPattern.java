package com.easyinsight.analysis;

import com.easyinsight.core.Key;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * User: abaldwin
 * Date: Sep 8, 2009
 * Time: 10:38:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class URLPattern {

    private static final String patternStr = "\\[[A-Za-z0-9][A-Za-z0-9\\t :<>,.;/?\"\\\'{}=+-]*\\]";

    private static final Pattern keyPattern = Pattern.compile(patternStr);

    public static List<String> getKeys(String pattern) {
        List<String> results = new LinkedList<String>();
        Matcher m = keyPattern.matcher(pattern);
        while(m.find()) {
            String s = m.group();
            s = s.substring(1, s.length() - 1).trim();
            if(!results.contains(s))
                results.add(s);
        }
        return results;
    }

    public static String getURL(String pattern, IRow row, Map<String, String> dataSourceProperties) {
        Map<String, Key> values = new HashMap<String, Key>();
        for(Key k : row.getKeys()) {
            values.put(k.toKeyString(), k);
        }
        Matcher m = keyPattern.matcher(pattern);
        String[] fragments = pattern.split(patternStr);
        int i = 0;
        StringBuilder sb;
        if(i < fragments.length)
             sb = new StringBuilder(fragments[i++]);
        else
            sb = new StringBuilder();
        while(m.find()) {
            String t = m.group();
            String s = t.substring(1, t.length() - 1).trim();
            if(values.get(s) == null) {
                String dsProp = dataSourceProperties.get(s);
                if (dsProp == null)
                    sb.append(t);
                else
                    sb.append(dsProp);
            }
            sb.append(row.getValue(values.get(s)).toString());
            if(i < fragments.length)
                sb.append(fragments[i++]);
        }
        String urlString = sb.toString();
        if (!urlString.startsWith("http")) {
            urlString = "http://" + urlString;
        }
        return urlString;
    }

}
