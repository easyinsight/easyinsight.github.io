package com.easyinsight.salesautomation;

/**
 * User: jamesboe
 * Date: 1/5/11
 * Time: 2:50 PM
 */
public class ParagraphEmailBlock extends EmailBlock {

    private String paragraph;
    private String[] links;

    public ParagraphEmailBlock(String paragraph, String... links) {
        this.paragraph = paragraph;
        this.links = links;
    }

    @Override
    public String toHTML() {
        String html = "<p>" + paragraph + "</p>";
        int i = 0;
        int index;
        do {
            String pattern = "{" + i + "}";
            index = html.indexOf(pattern);
            if (index != -1) {
                String link = links[i];
                String composedLink = "<a href=\"" + link + "\">" + link + "</a>";
                html = html.replace(pattern, composedLink);
            }
            i++;
        } while (index != -1);
        return html;
    }

    @Override
    public String toText() {
        String text = paragraph;
        int i = 0;
        int index;
        do {
            String pattern = "{" + i + "}";
            index = text.indexOf(pattern);
            if (index != -1) {
                String link = links[i];
                text = text.replace(pattern, link);
            }
            i++;
        } while (index != -1);
        return text;
    }
}
