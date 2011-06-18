package com.easyinsight.exchange;

import com.easyinsight.core.EIDescriptor;

import java.util.Date;

/**
 * User: jamesboe
 * Date: Sep 14, 2009
 * Time: 7:08:30 PM
 */
public class ExchangeItem {
    private String name;
    private long id;
    private int installs;
    private Date dateAdded;
    private String description;
    private String author;
    private com.easyinsight.core.EIDescriptor descriptor;
    private long solutionID;
    private String solutionName;
    private boolean recommended;

    public ExchangeItem() {
    }

    public ExchangeItem(String name, long id, int installs, Date dateAdded, String description,
                        String author, EIDescriptor descriptor, long solutionID, String solutionName, boolean recommended) {
        this.name = name;
        this.id = id;
        this.installs = installs;
        this.dateAdded = dateAdded;
        this.description = description;
        this.author = author;
        this.descriptor = descriptor;
        this.solutionID = solutionID;
        this.solutionName = solutionName;
        this.recommended = recommended;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public EIDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(EIDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public long getSolutionID() {
        return solutionID;
    }

    public void setSolutionID(long solutionID) {
        this.solutionID = solutionID;
    }

    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getInstalls() {
        return installs;
    }

    public void setInstalls(int installs) {
        this.installs = installs;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
}
