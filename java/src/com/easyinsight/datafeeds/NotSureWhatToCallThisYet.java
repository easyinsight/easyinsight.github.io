package com.easyinsight.datafeeds;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: 3/31/14
 * Time: 3:53 PM
 */
public class NotSureWhatToCallThisYet {

    private String name;
    private List<AnalysisItem> fields;

    public String getName() {
        return name;
    }

    public List<AnalysisItem> getFields() {
        return fields;
    }

    private Feed feed;

    public NotSureWhatToCallThisYet(Feed feed) {
        this.feed = feed;
        name = feed.getName();
        fields = feed.getFields();
    }

    private class Yargh {

        private Set<AnalysisItem> analysisItems = new HashSet<AnalysisItem>();
        private Collection<FilterDefinition> filters = new ArrayList<FilterDefinition>();
    }

    public DataSet yargh(InsightRequestMetadata insightRequestMetadata, Set<AnalysisItem> analysisItems, EIConnection conn,
                      Collection<FilterDefinition> reportFilters) {
        try {
            Map<DateKey, Yargh> nodeMap = new HashMap<DateKey, Yargh>();
            AnalysisDateDimension baseDate = insightRequestMetadata.getBaseDate();
            List<FilterDefinition> dateFilters = new ArrayList<FilterDefinition>();
            for (FilterDefinition reportFilter : reportFilters) {
                if (reportFilter.getField() != null && reportFilter.getField().qualifiedName().equals(baseDate.qualifiedName())) {
                    dateFilters.add(reportFilter);
                }
            }
            Set<AnalysisItem> otherItems = new HashSet<AnalysisItem>();
            List<CompositeFeedNode> compositeFeedNodes;
            if (feed instanceof CompositeFeed) {
                compositeFeedNodes = ((CompositeFeed) feed).getCompositeFeedNodes();
            } else {
                compositeFeedNodes = new ArrayList<CompositeFeedNode>();
            }


            for (AnalysisItem base : analysisItems) {

                boolean onThisLevel = false;
                if (base.getKey() instanceof DerivedKey) {

                    DerivedKey dKey = (DerivedKey) base.getKey();
                    for (CompositeFeedNode node : compositeFeedNodes) {
                        if (node.getDataFeedID() == dKey.getFeedID()) {
                            onThisLevel = true;
                        }
                    }
                } else if (base.getKey() instanceof NamedKey) {
                    if (!(feed.getDataSource() instanceof CompositeFeedDefinition)) {
                        onThisLevel = true;
                    }
                }

                if (onThisLevel) {

                    if (insightRequestMetadata.getReportItems().contains(base) && base.hasType(AnalysisItemTypes.MEASURE)) {
                        System.out.println("Base item = " + base.toDisplay() + " on " + getName());
                        AnalysisDateDimension itemDate = findDateDimension(base, baseDate.getDateLevel());
                        insightRequestMetadata.addAudit(base, "Date comparison used date of " + itemDate.toDisplay());
                        List<AnalysisItem> items = base.getAnalysisItems(insightRequestMetadata.getAllItems(), analysisItems, false, true, new HashSet<AnalysisItem>(),
                                insightRequestMetadata.getStructure());
                        for (AnalysisItem item : items) {
                            Key key = item.getKey();
                            UniqueKey uniqueKey = null;
                            if (compositeFeedNodes.size() > 0) {

                                if (key instanceof DerivedKey) {
                                    for (CompositeFeedNode node : compositeFeedNodes) {
                                        if (item.getKey().hasDataSource(node.getDataFeedID())) {
                                            uniqueKey = new UniqueKey(node.getDataFeedID(), UniqueKey.DERIVED);
                                            break;
                                        }
                                    }
                                } else if (key instanceof ReportKey) {
                                    for (CompositeFeedNode node : compositeFeedNodes) {
                                        if (item.getKey().hasReport(node.getReportID())) {
                                            uniqueKey = new UniqueKey(node.getReportID(), UniqueKey.REPORT);
                                            break;
                                        }
                                    }
                                }
                                if (uniqueKey == null) {
                                    throw new RuntimeException("Could not find the node for " + item.toDisplay() + " on " + getName());
                                }
                            } else {
                                uniqueKey = new UniqueKey(0, UniqueKey.DERIVED);
                            }

                            DateKey dateKey = new DateKey(uniqueKey, itemDate.qualifiedName(), itemDate);
                            //List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
                            Yargh queryStateNode = nodeMap.get(dateKey);
                            if (queryStateNode == null) {
                                queryStateNode = new Yargh();
                                nodeMap.put(dateKey, queryStateNode);
                                queryStateNode.analysisItems.add(itemDate);
                            }
                            queryStateNode.analysisItems.add(item);
                        }
                    } else {
                        otherItems.add(base);
                    }
                } else {
                    AnalysisDateDimension itemDate = findDateDimension(base, baseDate.getDateLevel());
                    Key key = base.getKey();
                    UniqueKey uniqueKey = null;
                    if (key instanceof DerivedKey) {
                        for (CompositeFeedNode node : compositeFeedNodes) {
                            if (base.getKey().hasDataSource(node.getDataFeedID())) {
                                uniqueKey = new UniqueKey(node.getDataFeedID(), UniqueKey.DERIVED);
                                break;
                            }
                        }
                    } else if (key instanceof ReportKey) {
                        for (CompositeFeedNode node : compositeFeedNodes) {
                            if (base.getKey().hasReport(node.getReportID())) {
                                uniqueKey = new UniqueKey(node.getReportID(), UniqueKey.REPORT);
                                break;
                            }
                        }
                    } else {
                        continue;
                    }
                    if (uniqueKey == null) {
                        throw new RuntimeException("Could not find the node for " + base.toDisplay() + " on " + getName());
                    }
                    insightRequestMetadata.addAudit(base, "Date comparison used date of " + itemDate.toDisplay());
                    DateKey dateKey = new DateKey(uniqueKey, itemDate.qualifiedName(), itemDate);
                    Yargh queryStateNode = nodeMap.get(dateKey);
                    if (queryStateNode == null) {
                        queryStateNode = new Yargh();
                        nodeMap.put(dateKey, queryStateNode);
                        queryStateNode.analysisItems.add(itemDate);
                    }
                    queryStateNode.analysisItems.add(base);
                }
            }

            DataSet dataSet = new DataSet();
            for (Map.Entry<DateKey, Yargh> entry : nodeMap.entrySet()) {
                AnalysisDateDimension dateDimension = entry.getKey().dateDimension;
                Yargh yargh = entry.getValue();
                yargh.analysisItems.addAll(otherItems);
                for (FilterDefinition filterDefinition : dateFilters) {
                    FilterDefinition clone = filterDefinition.clone();
                    clone.setField(dateDimension);
                    yargh.filters.add(clone);
                }

                DataSet childSet = feed.getAggregateDataSet(yargh.analysisItems, yargh.filters, insightRequestMetadata, getFields(), false, conn);
                for (IRow row : childSet.getRows()) {
                    dataSet.addRow(row);
                    Value value = row.getValue(dateDimension);
                    row.addValue(baseDate.createAggregateKey(), value);
                    dataSet.addRow(row);
                }
            }

            return dataSet;

            /*for (AnalysisItem item : otherItems) {
                   boolean found = false;
                   for (QueryStateNode node : nodeMap.values()) {
                       if (node.handles(item)) {
                           found = true;
                           node.addItem(item);
                       }
                   }
                   if (!found) {
                       // ARGH
                   }
               }

               new Yargh();*/

            /*DataSet dataSet = new DataSet();
               for (Map.Entry<DateKey, QueryStateNode> entry : nodeMap.entrySet()) {
                   AnalysisDateDimension dateDimension = entry.getKey().dateDimension;
                   QueryStateNode queryStateNode = entry.getValue();
                   for (FilterDefinition filterDefinition : dateFilters) {
                       FilterDefinition clone = filterDefinition.clone();
                       clone.setField(dateDimension);
                       queryStateNode.addFilter(clone);
                   }
                   DataSet childSet = queryStateNode.produceDataSet(insightRequestMetadata);
                   for (IRow row : childSet.getRows()) {
                       Value value = row.getValue(dateDimension);
                       row.addValue(baseDate.createAggregateKey(), value);
                       dataSet.addRow(row);
                   }
               }
               return dataSet;*/
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private static class DateKey {
        private UniqueKey uniqueKey;
        private String date;
        private AnalysisDateDimension dateDimension;

        private DateKey(UniqueKey uniqueKey, String date, AnalysisDateDimension dateDimension) {
            this.uniqueKey = uniqueKey;
            this.date = date;
            this.dateDimension = dateDimension;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DateKey dateKey = (DateKey) o;

            if (!date.equals(dateKey.date)) return false;
            if (!uniqueKey.equals(dateKey.uniqueKey)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = uniqueKey.hashCode();
            result = 31 * result + date.hashCode();
            return result;
        }
    }

    private AnalysisDateDimension findDateDimension(AnalysisItem item, int dateLevel) throws CloneNotSupportedException {
        if (item.getDefaultDate() != null && !"".equals(item.getDefaultDate())) {
            for (AnalysisItem field : getFields()) {
                if (field.hasType(AnalysisItemTypes.DATE_DIMENSION) && item.getDefaultDate().equals(field.toOriginalDisplayName())) {
                    return (AnalysisDateDimension) field;
                }
            }
            throw new RuntimeException("Could not find " + item.getDefaultDate() + " for " + item.toDisplay() + ".");
        }
        Key sourceKey = item.getKey();
        if (sourceKey instanceof DerivedKey) {
            DerivedKey derivedKey = (DerivedKey) sourceKey;
            long sourceID;
            Key parentKey = derivedKey.getParentKey();
            if (parentKey instanceof NamedKey) {
                sourceID = derivedKey.getFeedID();
            } else if (parentKey instanceof DerivedKey) {
                DerivedKey derivedKey1 = (DerivedKey) parentKey;
                sourceID = derivedKey1.getFeedID();
            } else {
                sourceID = 0;
            }
            for (AnalysisItem field : getFields()) {
                if (field.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                    if (field.getKey().hasDataSource(sourceID)) {
                        AnalysisDateDimension clone = (AnalysisDateDimension) field.clone();
                        clone.setDateLevel(dateLevel);
                        return clone;
                    }
                }
            }
        } else if (sourceKey instanceof ReportKey) {
            ReportKey reportKey = (ReportKey) sourceKey;
            for (AnalysisItem field : getFields()) {
                if (field.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                    if (field.getKey().hasReport(reportKey.getReportID())) {
                        AnalysisDateDimension clone = (AnalysisDateDimension) field.clone();
                        clone.setDateLevel(dateLevel);
                        return clone;
                    }
                }
            }
        } else if (sourceKey instanceof NamedKey) {
            for (AnalysisItem field : getFields()) {
                if (field.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                    AnalysisDateDimension clone = (AnalysisDateDimension) field.clone();
                    clone.setDateLevel(dateLevel);
                    return clone;
                }
            }
        }
        return null;
    }
}
