package com.easyinsight.datasources {

import com.easyinsight.customupload.GitHubDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.github.GithubCompositeSource")]
public class GithubCompositeSource extends CompositeServerDataSource {

    public var accessToken:String;
    public var refreshToken:String;

    public function GithubCompositeSource() {
        super();
        this.feedName = "GitHub";
    }

    override public function getFeedType():int {
        return DataSourceType.GITHUB;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        return pages;
    }

    override public function configClass():Class {
        return GitHubDataSourceCreation;
    }
}
}