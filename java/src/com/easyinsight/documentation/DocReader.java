package com.easyinsight.documentation;

import com.easyinsight.cache.MemCachedManager;
import com.easyinsight.html.RedirectUtil;
import com.easyinsight.logging.LogClass;
import nu.xom.*;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.core.parser.markup.MarkupLanguage;
import org.eclipse.mylyn.wikitext.core.util.ServiceLocator;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: jamesboe
 * Date: 3/3/14
 * Time: 9:17 AM
 */
public class DocReader {

    public static final int APP = 1;
    public static final int WEBSITE = 2;


    public static String toHTML(String page, HttpServletRequest request, int site) throws Exception {
        if (page == null || "".equals(page)) {
            page = "Main_Page";
        }
        String cacheKey = site + page;
        String pageResults = (String) MemCachedManager.instance().get(cacheKey);
        if (pageResults == null) {
            Map<String, String> altToSrc = new HashMap<>();
            {
                URL url = new URL("https://wiki.easy-insight.com/wiki/index.php/" + URLDecoder.decode(page, "UTF-8"));
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

                BufferedReader in = null;
                try {
                    in = new BufferedReader(
                            new InputStreamReader(
                                    connection.getInputStream()));
                } catch (IOException e) {
                    in = new BufferedReader(
                            new InputStreamReader(
                                    connection.getErrorStream()));
                }

                StringBuilder result = new StringBuilder();

                String line;

                while ((line = in.readLine()) != null) {
                    result.append(line).append("\n");
                }
                in.close();
                //System.out.println(result);
                ByteArrayInputStream bais = new ByteArrayInputStream(result.toString().getBytes());
                Builder builder = new Builder();
                Document doc = builder.build(bais);
                Nodes nodes = doc.query("//img");
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    if (node instanceof Element) {
                        Element element = (Element) node;
                        String alt = element.getAttribute("alt").getValue().toLowerCase().trim();
                        String src = element.getAttribute("src").getValue();
                        altToSrc.put(alt, src);
                    }
                }

            }

            URL url = new URL("https://wiki.easy-insight.com/wiki/index.php?title=" + URLDecoder.decode(page, "UTF-8") + "&action=edit");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            BufferedReader in = null;
            try {
                in = new BufferedReader(
                        new InputStreamReader(
                                connection.getInputStream()));
            } catch (IOException e) {
                in = new BufferedReader(
                        new InputStreamReader(
                                connection.getErrorStream()));
            }

            StringBuilder result = new StringBuilder();

            String line;

            while ((line = in.readLine()) != null) {
                result.append(line).append("\n");
            }
            in.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(result.toString().getBytes());
            Builder builder = new Builder();
            Document doc = builder.build(bais);
            Nodes nodes = doc.query("//textarea/text()");
            if (nodes.size() == 0) {
                LogClass.error("Empty node on " + page);
                return "";
                // no text found in the documentation
            }
            Text node = (Text) nodes.get(0);
            String value = node.getValue();

            Map<String, String> fileMap = new HashMap<String, String>();
            Map<String, String> escapeMap = new HashMap<String, String>();
            // find links
            {
                Map<String, String> gs = new HashMap<String, String>();
                Pattern pattern = Pattern.compile("\\[(.*?)\\]");
                Matcher matcher = pattern.matcher(value);
                while (matcher.find()) {
                    String g = matcher.group();
                    if (g.charAt(1) != '[') {
                        String escaped = g.replace("[", "|").replace("]", "^");
                        gs.put(g, escaped);
                        escapeMap.put(escaped, g);
                    }
                }
                for (Map.Entry<String, String> entry : gs.entrySet()) {
                    value = value.replace(entry.getKey(), entry.getValue());
                }
            }
            {
                Pattern pattern = Pattern.compile("\\[\\[(.*?)\\]\\]");
                Matcher matcher = pattern.matcher(value);
                Map<String, String> gs = new HashMap<String, String>();
                while (matcher.find()) {
                    String g = matcher.group();
                    if (g.startsWith("[[File")) {
                        String substring = g.substring(2, g.length() - 2).split("\\:")[1].trim();
                        if (substring.contains("|")) {
                            String[] tokens = substring.split("\\|");
                            substring = tokens[0].trim();
                        }
                        String target = altToSrc.get(substring.toLowerCase());
                        if (target == null) {
                            target = altToSrc.get(substring.replace("_", " ").toLowerCase());
                            if (target == null) {
                                System.out.println("no luck at all...");
                            }
                        }
                        gs.put(g, "AAA" + substring + "AAA");
                        fileMap.put("AAA" + substring + "AAA", "<img src=\"https://wiki.easy-insight.com/" + target + "\" alt=\"" + substring + "\"/>");
                        //fileMap.put("AAA" + substring + "AAA", "");
                    } else {
                        String substring = g.substring(2, g.length() - 2);
                        if (substring.contains("|")) {
                            substring = substring.split("\\|")[0];
                        }

                        if (substring.contains("screencasts.jsp")) {
                            String link = "[" + RedirectUtil.getURL(request, "/app/screencasts.jsp") + " Screencasts ]";
                            gs.put(g, link);
                        } else if (substring.contains("docScreencasts.jsp")) {
                            String link = "[" + RedirectUtil.getURL(request, "/app/docScreencasts.jsp") + " Screencasts ]";
                            gs.put(g, link);
                        } else {
                            String link = substring.replace(" ", "_");
                            if (site == APP) {
                                String blah = "[" + RedirectUtil.getURL(request, "/app/docs/" + link) + " " + substring + "]";
                                gs.put(g, blah);
                            } else if (site == WEBSITE) {
                                String blah = "[" + RedirectUtil.getURL(request, "/app/websiteDocs/" + link) + " " + substring + "]";
                                gs.put(g, blah);
                            }
                        }
                    }

                    //System.out.println();
                }

                for (Map.Entry<String, String> entry : gs.entrySet()) {
                    value = value.replace(entry.getKey(), entry.getValue());
                }
                /*for (String g : gs) {
                    value = value.replace(g, "");
                }*/
                value = value.replace("<br />", "\n");
            }
            //System.out.println(value);
            String html = parseMediaWiki(value);
            for (Map.Entry<String, String> entry : escapeMap.entrySet()) {
                html = html.replace(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, String> entry : fileMap.entrySet()) {
                html = html.replace(entry.getKey(), entry.getValue());
            }
            pageResults = html;

            if ("Main_Page".equals(page)) {
                String target = "<ol>";
                int insertPoint = pageResults.indexOf(target) + target.length();
                String insertContent;
                if (site == DocReader.WEBSITE) {
                    insertContent = "<li><a href=\"/app/screencasts.jsp\">Screencasts</a></li>";
                } else if (site == DocReader.APP) {
                    insertContent = "<li><a href=\"/app/docsScreencast.jsp\">Screencasts</a></li>";
                } else {
                    insertContent = "";
                }
                pageResults = pageResults.substring(0, insertPoint) + insertContent + pageResults.substring(insertPoint);
            }

            MemCachedManager.instance().add(cacheKey, 500000, pageResults);
        }
        return pageResults;
    }

    public static final String NAME_TEXTILE = "Textile";
    public static final String NAME_TRACWIKI = "TracWiki";
    public static final String NAME_MEDIAWIKI = "MediaWiki";
    public static final String NAME_CONFLUENCE = "Confluence";
    public static final String NAME_TWIKI = "TWiki";

    public static String parseTextile(String wikiText) {

        return parseByLanguage(NAME_TEXTILE, wikiText);
    }

    public static String parseTracWiki(String wikiText) {

        return parseByLanguage(NAME_TRACWIKI, wikiText);
    }

    public static String parseMediaWiki(String wikiText) {

        return parseByLanguage(NAME_MEDIAWIKI, wikiText);
    }

    public static String parseConfluence(String wikiText) {

        return parseByLanguage(NAME_CONFLUENCE, wikiText);
    }

    public static String parseTWiki(String wikiText) {

        return parseByLanguage(NAME_TWIKI, wikiText);
    }

    public static String parseByLanguage(String name, String wikiText) {

        return parseByLanguage(ServiceLocator.getInstance().getMarkupLanguage(name), wikiText);
    }

    public static String parseByLanguage(MarkupLanguage language, String wikiText) {

        StringWriter writer = new StringWriter();
        HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer);
        MarkupParser parser = new MarkupParser(language, builder);
        parser.parse(wikiText);
        //String html = parser.parseToHtml(wikiText);
        String html = writer.toString();
        html = html.substring(169);
        html = html.substring(0, html.length() - 14);
        return html;
    }

    /**
     * MarkupLanguage API prefers we retrieve the MarkupLanguge by name from
     * the ServiceLocator; since there are no name constants, we obtain the names
     * from this method or alternately use the hard-coded names from this utility class,
     * which were pulled directly from a prior call to this very method.
     */
    public static Set<String> getLanguageNames() {

        Set<String> languages = new TreeSet<String>();
        for (MarkupLanguage s : ServiceLocator.getInstance().getAllMarkupLanguages()) {
            languages.add(s.getName());
        }

        return languages;
    }
}
