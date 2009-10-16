package com.easyinsight.genredata
{
import com.easyinsight.listing.AnalyzeSource;

[Event(name="returnToStore", type="com.easyinsight.listing.CatalogNavigationEvent")]
	
	public interface ICatalogPage
	{
		function analyze():AnalyzeSource;
	}
}