package com.easyinsight.analysis;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Jul 17, 2008
 * Time: 7:44:42 PM
 */
@Entity
@Table(name="yahoo_map_definition")
@PrimaryKeyJoinColumn(name="graphic_definition_id")
public class YahooMapDefinition extends GraphicDefinition {

    @Column(name="yahoo_map_definition_id")
    private long yahooMapDefinitionID;

    public long getYahooMapDefinitionID() {
        return yahooMapDefinitionID;
    }

    public void setYahooMapDefinitionID(long yahooMapDefinitionID) {
        this.yahooMapDefinitionID = yahooMapDefinitionID;
    }

    protected WSAnalysisDefinition createWSDefinition() {
        WSYahooMapDefinition wsMapDefinition = new WSYahooMapDefinition();
        wsMapDefinition.setYahooMapDefinitionID(yahooMapDefinitionID);
        return wsMapDefinition;
    }

    public AnalysisDefinition clone() throws CloneNotSupportedException {
        YahooMapDefinition mapDefinition = (YahooMapDefinition) super.clone();
        mapDefinition.setYahooMapDefinitionID(0);
        return mapDefinition;
    }
}
