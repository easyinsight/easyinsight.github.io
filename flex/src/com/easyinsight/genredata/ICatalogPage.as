package com.easyinsight.genredata
{
	
	[Event(name="returnToStore", type="com.easyinsight.listing.CatalogNavigationEvent")]
	import com.easyinsight.listing.AnalyzeSource;
	
	public interface ICatalogPage
	{
		function analyze():AnalyzeSource;
	}
}