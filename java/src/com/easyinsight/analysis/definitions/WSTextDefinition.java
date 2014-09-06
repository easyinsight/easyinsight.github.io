package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;
import com.easyinsight.intention.Intention;
import com.easyinsight.intention.IntentionSuggestion;
import com.easyinsight.intention.ReportPropertiesIntention;
import com.easyinsight.pipeline.IComponent;
import com.easyinsight.pipeline.ListSummaryComponent;
import com.easyinsight.util.HTMLPolicy;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.core.util.ServiceLocator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringWriter;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: James Boe
 * Date: Jan 11, 2008
 * Time: 10:19:11 AM
 */
public class WSTextDefinition extends WSAnalysisDefinition {
    private List<AnalysisItem> columns;
    private long textReportID;
    private int fontColor;
    private String text;
    private transient String originalText;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    public long getTextReportID() {
        return textReportID;
    }

    public void setTextReportID(long textReportID) {
        this.textReportID = textReportID;
    }

    public List<AnalysisItem> getColumns() {
        return columns;
    }

    public void setColumns(List<AnalysisItem> columns) {
        this.columns = columns;
    }

    public String getDataFeedType() {
        return "Text";
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        for (AnalysisItem item : columns) {
            columnList.add(item);
        }
        columnList.addAll(getLimitFields());
        return columnList;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        Collections.sort(getColumns(), new Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                return new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition());
            }
        });
        addItems("", getColumns(), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        setColumns(items("", structure));
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        fontColor = (int) findNumberProperty(properties, "fontColor", 0);
    }

    public String jsonProperties() {

        JSONObject p = new JSONObject();
        try {
            List<ReportProperty> properties = createProperties();
            populateProperties(properties);
            for (ReportProperty property : properties) {
                if (property instanceof ReportNumericProperty)
                    p.put(property.getPropertyName(), ((ReportNumericProperty) property).getValue());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return p.toString();
    }

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentFilters) throws JSONException {
        JSONObject jo = super.toJSON(htmlReportMetadata, parentFilters);
        jo.put("key", getUrlKey());
        jo.put("url", "/app/htmlExport");
        jo.put("type", "text_report");
        return jo;
    }

    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("fontColor", fontColor));
        return properties;
    }

    public Map<String, DerivedAnalysisDimension> beforeRun() {
        Map<String, DerivedAnalysisDimension> map = new HashMap<>();

        if (getText() != null && !"".equals(getText())) {

            originalText = getText();

            List<AnalysisItem> tmpColumns = new ArrayList<>();
            if (getAddedItems() == null) {
                setAddedItems(new ArrayList<>());
            }

            Pattern pattern = Pattern.compile("\\{(.*?)\\}");
            Matcher matcher = pattern.matcher(getText());
            Map<String, String> aliasMap = new HashMap<>();
            int i = 0;
            while (matcher.find()) {
                String g = matcher.group();
                String substring = g.substring(1, g.length() - 1).trim();
                DerivedAnalysisDimension dim = new DerivedAnalysisDimension();
                if (substring.startsWith("[") && substring.endsWith("]")) {
                    dim.setDerivationCode("format(" + substring + ")");
                } else {
                    dim.setDerivationCode(substring);
                }
                System.out.println(substring);
                String alias = "tmp" + (i++);
                dim.setKey(new NamedKey(alias));
                dim.setDisplayName(substring);
                map.put(alias, dim);
                aliasMap.put(g, alias);
                getAddedItems().add(dim);
                tmpColumns.add(dim);
            }
            for (Map.Entry<String, String> entry : aliasMap.entrySet()) {
                setText(getText().replace(entry.getKey(), entry.getValue()));
            }
            setColumns(tmpColumns);
        }
        return map;
    }

    public String createText(Map<String, DerivedAnalysisDimension> map, DataSet dataSet) {
        if (dataSet.getRows().size() == 0 || getText() == null) {
            return "";
        }
        System.out.println(getText());
        StringWriter writer = new StringWriter();
        HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer);
        MarkupParser parser = new MarkupParser(ServiceLocator.getInstance().getMarkupLanguage("MediaWiki"), builder);
        parser.parse(getText());
        //String html = parser.parseToHtml(wikiText);
        String html = writer.toString();
        IRow row = dataSet.getRow(0);
        for (Map.Entry<String, DerivedAnalysisDimension> entry : map.entrySet()) {
            String string = row.getValue(entry.getValue().createAggregateKey()).toString();
            html = html.replace(entry.getKey(), string);
        }
        html = html.substring(169);
        html = html.substring(0, html.length() - 14);
        html = HTMLPolicy.getPolicyFactory().sanitize(html);
        setText(originalText);
        return html;
    }

    @Override
    public String toExportHTML(EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean email) {
        try {
            return DataService.getText(this, insightRequestMetadata, conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
