package com.easyinsight.datafeeds.trello;

/**
* User: jamesboe
* Date: 3/24/14
* Time: 9:17 PM
*/
class LabelObject {
    private String cardID;
    private String label;
    private String color;

    LabelObject(String cardID, String label, String color) {
        this.cardID = cardID;
        this.label = label;
        this.color = color;
    }

    public String getCardID() {
        return cardID;
    }

    public String getLabel() {
        return label;
    }

    public String getColor() {
        return color;
    }
}
