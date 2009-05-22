package com.easyinsight.analysis;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Mar 31, 2009
 * Time: 10:06:02 AM
 */
@Entity
@Table(name="treemap_report")
public class TreeMapDefinitionState extends AnalysisDefinitionState {

    @Column(name="color_scheme")
    private int colorScheme;

    public int getColorScheme() {
        return colorScheme;
    }

    public void setColorScheme(int colorScheme) {
        this.colorScheme = colorScheme;
    }

    public WSAnalysisDefinition createWSDefinition() {
        WSTreeMapDefinition wsTreeMapDefinition = new WSTreeMapDefinition();
        wsTreeMapDefinition.setColorScheme(colorScheme);
        return wsTreeMapDefinition;
    }
}