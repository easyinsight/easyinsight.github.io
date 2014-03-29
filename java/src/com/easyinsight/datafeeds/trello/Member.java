package com.easyinsight.datafeeds.trello;

/**
* User: jamesboe
* Date: 3/24/14
* Time: 9:22 PM
*/
class Member {
    private String cardID;
    private String memberID;

    Member(String cardID, String memberID) {
        this.cardID = cardID;
        this.memberID = memberID;
    }

    public String getCardID() {
        return cardID;
    }

    public String getMemberID() {
        return memberID;
    }
}
