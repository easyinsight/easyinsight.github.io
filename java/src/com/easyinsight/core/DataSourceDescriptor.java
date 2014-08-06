package com.easyinsight.core;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.tag.Tag;
import com.easyinsight.userupload.CustomFolder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * User: James Boe
 * Date: Mar 30, 2009
 * Time: 2:31:58 PM
 */
public class DataSourceDescriptor extends EIDescriptor {

    private String description;
    private int dataSourceType;
    private long size;
    private Date creationDate;
    private Date lastDataTime;
    private long groupSourceID;
    private byte[] logoImage;
    private int dataSourceBehavior;
    private List<EIDescriptor> children = new ArrayList<EIDescriptor>();
    private List<CustomFolder> customFolders = new ArrayList<CustomFolder>();
    private List<Tag> tags;
    private int rowCount;
    private boolean inRequestedGroup;
    private Collection<EIDescriptor> prebuilts;

    public boolean isInRequestedGroup() {
        return inRequestedGroup;
    }

    public void setInRequestedGroup(boolean inRequestedGroup) {
        this.inRequestedGroup = inRequestedGroup;
    }

    public Collection<EIDescriptor> getPrebuilts() {
        return prebuilts;
    }

    public void setPrebuilts(Collection<EIDescriptor> prebuilts) {
        this.prebuilts = prebuilts;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public List<CustomFolder> getCustomFolders() {
        return customFolders;
    }

    public void setCustomFolders(List<CustomFolder> customFolders) {
        this.customFolders = customFolders;
    }

    public int getDataSourceBehavior() {
        return dataSourceBehavior;
    }

    public void setDataSourceBehavior(int dataSourceBehavior) {
        this.dataSourceBehavior = dataSourceBehavior;
    }

    public byte[] getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(byte[] logoImage) {
        this.logoImage = logoImage;
    }

    public List<EIDescriptor> getChildren() {
        return children;
    }

    public void setChildren(List<EIDescriptor> children) {
        this.children = children;
    }

    public long getGroupSourceID() {
        return groupSourceID;
    }

    public void setGroupSourceID(long groupSourceID) {
        this.groupSourceID = groupSourceID;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastDataTime() {
        return lastDataTime;
    }

    public void setLastDataTime(Date lastDataTime) {
        this.lastDataTime = lastDataTime;
    }

    public int getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(int dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getType() {
        return EIDescriptor.DATA_SOURCE;
    }

    public DataSourceDescriptor() {
    }

    public DataSourceDescriptor(String name, long id, int dataSourceType, boolean accountVisible, int dataSourceBehavior) {
        super(name, id, accountVisible);
        this.dataSourceType = dataSourceType;
        this.dataSourceBehavior = dataSourceBehavior;
    }

    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        DateFormat dateFormat = ExportService.getDateFormatForAccount(AnalysisDateDimension.MINUTE_LEVEL, null, md.dateFormat);
        JSONObject jo = super.toJSON(md);
        JSONArray ja = new JSONArray();
        if(getTags() != null)
            for(Tag t : getTags()) {
                JSONObject to = new JSONObject();
                to.put("name", t.getName());
                to.put("id", t.getId());
                ja.put(to);
            }
        jo.put("tags", ja);
        if (lastDataTime != null) {
            jo.put("last_refresh_time", dateFormat.format(lastDataTime));
        } else {
            jo.put("last_refresh_time", "");
        }
        return jo;
    }
}
