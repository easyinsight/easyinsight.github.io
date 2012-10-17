package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSDiagramDefinition;
import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ExportService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 7/13/12
 * Time: 9:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class DiagramChartServlet extends HtmlServlet {
    @Override
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSAnalysisDefinition report) throws Exception {
        TrendDataResults results = DataService.getTrendDataResults((WSDiagramDefinition) report, insightRequestMetadata, conn);
        List<TrendOutcome> outcomes = results.getTrendOutcomes();
        JSONObject outDiagram = new JSONObject();
        if(outcomes.size() > 0) {

            if (!(report instanceof WSDiagramDefinition)) {
                throw new RuntimeException();
            }

            WSDiagramDefinition diagram = (WSDiagramDefinition) report;

            JSONObject nodes = new JSONObject();
            Map<AnalysisItem, String> lookup = new HashMap<AnalysisItem, String>();
            for (TrendOutcome o : outcomes) {
                AnalysisItem i = o.getMeasure();

                Link defaultLink = null;
                for (Link l : i.getLinks()) {
                    if (l.isDefaultLink())
                        defaultLink = l;
                }

                if (!(i.getReportFieldExtension() instanceof DiagramReportFieldExtension)) {
                    throw new RuntimeException();
                }
                JSONObject node = new JSONObject();
                DiagramReportFieldExtension nodeValues = (DiagramReportFieldExtension) i.getReportFieldExtension();
                node.put("x", nodeValues.getX());
                node.put("y", nodeValues.getY());

                node.put("name", i.getDisplayName());
                if (nodeValues.getDate() != null) {
                    node.put("type", "trend");
                    if (o.getHistorical() != null) {
                        double d = (((o.getNow().toDouble() / o.getHistorical().toDouble()) - 1.0) * 100.0);
                        FormattingConfiguration c = new FormattingConfiguration();
                        c.setFormattingType(FormattingConfiguration.PERCENTAGE);
                        node.put("change", c.createFormatter().format(d));
                        node.put("trendIcon", ExportService.getIconImage(o));
                    }
                } else {
                    node.put("type", "node");
                }


                if (defaultLink != null) {
                    node.put("drillthrough", defaultLink.getLinkID());
                }

                node.put("image", nodeValues.getIconImage());
                node.put("value", i.getFormattingConfiguration().createFormatter().format(o.getNow().toDouble()));

                lookup.put(i, String.valueOf(i.getAnalysisItemID()));
                nodes.put(String.valueOf(i.getAnalysisItemID()), node);
            }

            outDiagram.put("nodes", nodes);

            JSONArray links = new JSONArray();

            for (DiagramLink diagramLink : diagram.getLinks()) {
                JSONObject link = new JSONObject();
                link.put("from", lookup.get(diagramLink.getStartItem()));
                link.put("to", lookup.get(diagramLink.getEndItem()));
                if (diagramLink.getLabel() != null)
                    link.put("label", diagramLink.getLabel());
                links.put(link);
            }

            outDiagram.put("links", links);
        }
        response.setContentType("application/json");
        response.getOutputStream().write(outDiagram.toString().getBytes());
        response.getOutputStream().flush();

    }

}
