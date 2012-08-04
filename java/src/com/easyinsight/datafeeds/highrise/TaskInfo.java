package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.IRow;
import com.easyinsight.core.DateValue;
import com.easyinsight.dataset.DataSet;

import java.util.Date;

/**
* User: jamesboe
* Date: 8/2/12
* Time: 10:16 AM
*/
class TaskInfo {
    private String taskID;
    private String category;
    private String body;
    private String owner;
    private String author;
    private String caseID;
    private String companyID;
    private String dealID;
    private Date createdAt;
    private Date dueAt;
    private Date doneAt;
    private String contactID;

    TaskInfo(String taskID, String category, String body, String owner, String author, String caseID, String companyID, String dealID, Date createdAt, Date dueAt, Date doneAt,
             String contactID) {
        this.taskID = taskID;
        this.category = category;
        this.body = body;
        this.owner = owner;
        this.author = author;
        this.caseID = caseID;
        this.companyID = companyID;
        this.dealID = dealID;
        this.createdAt = createdAt;
        this.dueAt = dueAt;
        this.doneAt = doneAt;
        this.contactID = contactID;
    }

    public String getTaskID() {
        return taskID;
    }

    public String getCategory() {
        return category;
    }

    public String getBody() {
        return body;
    }

    public String getOwner() {
        return owner;
    }

    public String getAuthor() {
        return author;
    }

    public String getCaseID() {
        return caseID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public String getDealID() {
        return dealID;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getDueAt() {
        return dueAt;
    }

    public Date getDoneAt() {
        return doneAt;
    }

    public String getContactID() {
        return contactID;
    }

    public void addToDataSet(DataSet dataSet) {
        IRow row = dataSet.createRow();
        row.addValue(HighRiseTaskSource.TASK_ID, taskID);
        row.addValue(HighRiseTaskSource.CATEGORY, category);
        row.addValue(HighRiseTaskSource.COUNT, 1);
        row.addValue(HighRiseTaskSource.BODY, body);
        row.addValue(HighRiseTaskSource.OWNER, owner);
        row.addValue(HighRiseTaskSource.AUTHOR, author);
        row.addValue(HighRiseTaskSource.CASE_ID, caseID);
        row.addValue(HighRiseTaskSource.DEAL_ID, dealID);
        row.addValue(HighRiseTaskSource.COMPANY_ID, companyID);
        row.addValue(HighRiseTaskSource.CREATED_AT, new DateValue(createdAt));
        row.addValue(HighRiseTaskSource.DUE_AT, new DateValue(dueAt));
        row.addValue(HighRiseTaskSource.DONE_AT, new DateValue(doneAt));
        row.addValue(HighRiseTaskSource.CONTACT_ID, contactID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskInfo taskInfo = (TaskInfo) o;

        return !(taskID != null ? !taskID.equals(taskInfo.taskID) : taskInfo.taskID != null);

    }

    @Override
    public int hashCode() {
        return taskID != null ? taskID.hashCode() : 0;
    }
}
