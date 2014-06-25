package com.easyinsight.datafeeds.sample;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: 6/18/14
 * Time: 5:00 PM
 */
public class OpportunityData {
    private String id;
    private String salesRep;
    private double dealSize;

    private Date dateCreated;
    private Date dateWon;
    private String stage = "Created";
    private List<StageHistory> history = new ArrayList<>();
    private String customer;
    private String product;
    private int dealChance;

    private static String[] stages = { "Lead", "Qualified", "Working", "Negotiation", "Closed - Won", "Closed - Lost" };

    private static final String[] customerNames = { "Bross Design", "Crestone Engineering", "Uncompahgre Systems",
            "Sneffels Technology", "Sunlight Architecture", "Pyramid Research", "Little Bear Consulting", "Torreys Inc" };

    private static final String[] productNames = { "Widget1", "Widget2", "Widget3", "Widget4", "Widget5"};

    public OpportunityData(String id, Date dateCreated, String salesRep, double dealSize, int dealChance) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.salesRep = salesRep;
        this.dealSize = dealSize;
        this.dealChance = dealChance;
        customer = customerNames[((int) (Math.random() * customerNames.length))];
        product = productNames[((int) (Math.random() * productNames.length))];
    }

    public String getCustomer() {
        return customer;
    }

    public String getProduct() {
        return product;
    }

    public Date getDateWon() {
        return dateWon;
    }

    public double getDealSize() {
        return dealSize;
    }

    public String getSalesRep() {
        return salesRep;
    }

    public String getId() {
        return id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getStage() {
        return stage;
    }

    public List<StageHistory> getHistory() {
        return history;
    }

    public void generateHistory() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateCreated);
        Date now = new Date();

        // scale the stages based on dollars

        int interval;
        int minInterval;

        if (dealChance == 0 || dealChance == 1 || dealChance == 2) {
            interval = 30;
            minInterval = 10;
        } else if (dealChance == 3 || dealChance == 4) {
            interval = 40;
            minInterval = 20;
        } else {
            interval = 50;
            minInterval = 40;
        }

        for (int i = 0; i < (stages.length - 1); i++) {
            int days = (int) (Math.random() * interval) + minInterval;
            cal.add(Calendar.DAY_OF_YEAR, days);
            if (cal.getTime().after(now)) {
                break;
            }
            if (i == (stages.length - 2)) {
                int winOdds = (int) (Math.random() * 10);
                if (winOdds > 3) {
                    history.add(new StageHistory(stages[i], cal.getTime()));
                    stage = stages[i];
                } else {
                    history.add(new StageHistory(stages[i + 1], cal.getTime()));
                    stage = stages[i + 1];
                }
            } else {
                history.add(new StageHistory(stages[i], cal.getTime()));
                stage = stages[i];
            }
        }
        if ("Closed - Won".equals(stage)) {
            dateWon = cal.getTime();
        }
    }

    public class StageHistory {
        public String stage;
        public Date date;

        public StageHistory(String stage, Date date) {
            this.stage = stage;
            this.date = date;
        }
    }
}
