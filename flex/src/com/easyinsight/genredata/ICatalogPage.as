package com.easyinsight.genredata
{
import com.easyinsight.framework.ModuleAnalyzeSource;
	
	[Event(name="returnToStore", type="com.easyinsight.listing.CatalogNavigationEvent")]
	
	public interface ICatalogPage
	{
		function analyze():ModuleAnalyzeSource;
	}
}