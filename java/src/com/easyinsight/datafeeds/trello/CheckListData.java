package com.easyinsight.datafeeds.trello;

/**
* User: jamesboe
* Date: 3/24/14
* Time: 9:11 PM
*/
class CheckListData {
    private String checklistName;
    private String itemName;
    private String itemState;
    private String cardID;

    CheckListData(String checklistName, String itemName, String itemState, String cardID) {
        this.checklistName = checklistName;
        this.itemName = itemName;
        this.itemState = itemState;
        this.cardID = cardID;
    }

    public String getChecklistName() {
        return checklistName;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemState() {
        return itemState;
    }

    public String getCardID() {
        return cardID;
    }
}
