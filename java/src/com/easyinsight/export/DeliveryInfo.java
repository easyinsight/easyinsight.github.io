package com.easyinsight.export;

import com.easyinsight.analysis.AnalysisStorage;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.datafeeds.FeedStorage;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Nodes;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 6/3/11
 * Time: 12:21 PM
 */
public class DeliveryInfo {

    public static final int REPORT = 1;
    public static final int SCORECARD = 2;
    public static final int DASHBOARD = 3;

    private String name;
    private long id;
    private long dataSourceID;
    private String label;
    private List<FilterDefinition> filters;
    private int type;
    private int index;
    private int format;
    private boolean sendIfNoData;
    private DeliveryExtension deliveryExtension;
    private long configurationID;



    public long getConfigurationID() {
        return configurationID;
    }

    public void setConfigurationID(long configurationID) {
        this.configurationID = configurationID;
    }

    public void fromXML(Element element, XMLImportMetadata xmlImportMetadata) {
        id = Long.parseLong(element.getAttribute("id").getValue());
        type = Integer.parseInt(element.getAttribute("type").getValue());
        format = Integer.parseInt(element.getAttribute("format").getValue());
        sendIfNoData = Boolean.parseBoolean(element.getAttribute("sendIfNoData").getValue());
        label = element.getAttribute("label").getValue();
        Nodes filterNodes = element.query("filters/filter");
        dataSourceID = Long.parseLong(element.getAttribute("dataSourceID").getValue());
        try {
            xmlImportMetadata.setDataSource(new FeedStorage().getFeedDefinitionData(dataSourceID));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (type == REPORT) {
            xmlImportMetadata.setAdditionalReportItems(new AnalysisStorage().getAnalysisDefinition(id).getAddedItems());
        }
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        for (int i = 0; i < filterNodes.size(); i++) {
            Element filterNode = (Element) filterNodes.get(i);
            filters.add(FilterDefinition.fromXML(filterNode, xmlImportMetadata));
        }
        this.filters = filters;
    }

    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = new Element("deliveryInfo");
        element.addAttribute(new Attribute("id", String.valueOf(id)));
        element.addAttribute(new Attribute("type", valueOf(type)));
        element.addAttribute(new Attribute("index", String.valueOf(index)));
        element.addAttribute(new Attribute("format", String.valueOf(format)));
        if (dataSourceID == 0) {
            return null;
        }
        element.addAttribute(new Attribute("dataSourceID", String.valueOf(dataSourceID)));
        if (label == null) {
            element.addAttribute(new Attribute("label", ""));
        } else {
            element.addAttribute(new Attribute("label", label));
        }
        element.addAttribute(new Attribute("sendIfNoData", String.valueOf(sendIfNoData)));
        Element filters = new Element("filters");
        element.appendChild(filters);
        if (this.filters != null) {
            for (FilterDefinition filterDefinition : this.filters) {
                filters.appendChild(filterDefinition.toXML(xmlMetadata));
            }
        }
        return element;
    }

    private static String valueOf(int type) {
        switch(type) {
            case DASHBOARD:
                return "dashboard";
            case REPORT:
                return "report";
            default:
                return "";
        }
    }

    public boolean isSendIfNoData() {
        return sendIfNoData;
    }

    public void setSendIfNoData(boolean sendIfNoData) {
        this.sendIfNoData = sendIfNoData;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public List<FilterDefinition> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDefinition> filters) {
        this.filters = filters;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DeliveryExtension getDeliveryExtension() {
        return deliveryExtension;
    }

    public void setDeliveryExtension(DeliveryExtension deliveryExtension) {
        this.deliveryExtension = deliveryExtension;
    }

    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("name", getName());
        jo.put("id", getId());
        jo.put("data_source_id", getDataSourceID());
        jo.put("label", getLabel());
        jo.put("type", toType(getType()));
        jo.put("index", getIndex());
        jo.put("format", ReportDelivery.reportFormatValue(getFormat()));
        jo.put("configuration_id", getConfigurationID());
        jo.put("send_if_no_data", isSendIfNoData());
        if(getDeliveryExtension() != null)
            jo.put("delivery_info", getDeliveryExtension());
        return jo;
    }

    public DeliveryInfo() {
    }

    public DeliveryInfo(net.minidev.json.JSONObject jsonObject, int index) {
        setIndex(index);
        setName(String.valueOf(jsonObject.get("name")));
        setId(Long.parseLong(String.valueOf(jsonObject.get("id"))));
        setType(parseType(String.valueOf(jsonObject.get("type"))));
        setFormat(ReportDelivery.formatStringToValue(String.valueOf(jsonObject.get("format"))));
        if(jsonObject.containsKey("configuration_id"))
            setConfigurationID(Long.parseLong(String.valueOf(jsonObject.get("configuration_id"))));
        setSendIfNoData(Boolean.valueOf(String.valueOf(jsonObject.get("send_if_no_data"))));
        if(jsonObject.containsKey("delivery_info"))
            setDeliveryExtension(DeliveryExtension.fromJSON(((net.minidev.json.JSONObject) jsonObject.get("delivery_info"))));

    }

    public static String toType(int type) {
        switch(type) {
            case DASHBOARD:
                return "dashboard";
            case REPORT:
                return "report";
            default:
                return "";
        }
    }

    private static int parseType(String type) {
        switch(type) {
            case "dashboard":
                return DASHBOARD;
            case "report":
                return REPORT;
            default:
                return 0;
        }
    }
}
