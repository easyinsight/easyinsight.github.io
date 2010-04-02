package com.easyinsight.reportpackage;

import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.datafeeds.FeedConsumer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: Dec 8, 2009
 * Time: 11:08:36 PM
 */
public class ReportPackage implements Cloneable {
    private String name;
    private long reportPackageID;
    private List<InsightDescriptor> reports;
    private List<FeedConsumer> administrators = new ArrayList<FeedConsumer>();
    private List<FeedConsumer> consumers = new ArrayList<FeedConsumer>();
    private boolean marketplaceVisible;
    private boolean connectionVisible;
    private boolean publiclyVisible;
    private String description;
    private boolean singleDataSource;
    private String authorName;
    private Date dateCreated;
    private long dataSourceID;
    private boolean temporaryPackage;
    private String urlKey;

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public List<FeedConsumer> getAdministrators() {
        return administrators;
    }

    public void setAdministrators(List<FeedConsumer> administrators) {
        this.administrators = administrators;
    }

    public List<FeedConsumer> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<FeedConsumer> consumers) {
        this.consumers = consumers;
    }

    public boolean isTemporaryPackage() {
        return temporaryPackage;
    }

    public void setTemporaryPackage(boolean temporaryPackage) {
        this.temporaryPackage = temporaryPackage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSingleDataSource() {
        return singleDataSource;
    }

    public void setSingleDataSource(boolean singleDataSource) {
        this.singleDataSource = singleDataSource;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    @Override
    public ReportPackage clone() throws CloneNotSupportedException {
        ReportPackage reportPackage =  (ReportPackage) super.clone();
        reportPackage.setReportPackageID(0);
        reportPackage.setUrlKey(null);
        return reportPackage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getReportPackageID() {
        return reportPackageID;
    }

    public void setReportPackageID(long reportPackageID) {
        this.reportPackageID = reportPackageID;
    }

    public List<InsightDescriptor> getReports() {
        return reports;
    }

    public void setReports(List<InsightDescriptor> reports) {
        this.reports = reports;
    }

    public boolean isMarketplaceVisible() {
        return marketplaceVisible;
    }

    public void setMarketplaceVisible(boolean marketplaceVisible) {
        this.marketplaceVisible = marketplaceVisible;
    }

    public boolean isConnectionVisible() {
        return connectionVisible;
    }

    public void setConnectionVisible(boolean connectionVisible) {
        this.connectionVisible = connectionVisible;
    }

    public boolean isPubliclyVisible() {
        return publiclyVisible;
    }

    public void setPubliclyVisible(boolean publiclyVisible) {
        this.publiclyVisible = publiclyVisible;
    }
}
