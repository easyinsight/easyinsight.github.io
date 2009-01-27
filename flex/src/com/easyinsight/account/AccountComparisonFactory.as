package com.easyinsight.account
{
	import mx.core.IFactory;

	public class AccountComparisonFactory implements IFactory
	{
		private var key:String;
		
		public function AccountComparisonFactory(key:String)
		{
			this.key = key;
		}

		public function newInstance():*
		{
			return new AccountComparisonImage(key);
		}
		
	}
}