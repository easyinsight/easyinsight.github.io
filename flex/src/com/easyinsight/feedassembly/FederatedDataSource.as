/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 3/5/11
 * Time: 6:43 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.feedassembly {
import com.easyinsight.administration.feed.FeedDefinitionData;

import com.easyinsight.analysis.AnalysisItem;

import com.easyinsight.datasources.DataSourceType;

import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.composite.FederatedDataSource")]
public class FederatedDataSource extends FeedDefinitionData {

    public var sources:ArrayCollection;
    public var analysisItem:AnalysisItem;

    public function FederatedDataSource() {
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        var editor:FederatedEditor = new FederatedEditor();
        editor.federatedSource = this;
        editor.dataSources = this.sources;
        editor.label = "Federated Data Sources";
        pages.addItem(editor);
        return pages;
    }

    override public function getFeedType():int {
        return DataSourceType.FEDERATED;
    }
}
}
