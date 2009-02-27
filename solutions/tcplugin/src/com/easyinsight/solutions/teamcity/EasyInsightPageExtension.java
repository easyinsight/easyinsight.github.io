package com.easyinsight.solutions.teamcity;

import jetbrains.buildServer.web.openapi.SimplePageExtension;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PlaceId;

/**
 * User: James Boe
 * Date: Feb 26, 2009
 * Time: 7:07:20 PM
 */
public class EasyInsightPageExtension extends SimplePageExtension {
    public EasyInsightPageExtension(PagePlaces pagePlaces) {
        super(pagePlaces);
    }

    public EasyInsightPageExtension(PagePlaces pagePlaces, PlaceId placeId, @org.jetbrains.annotations.NonNls String s, @org.jetbrains.annotations.NonNls String s1) {
        super(pagePlaces, placeId, s, s1);
    }
}
