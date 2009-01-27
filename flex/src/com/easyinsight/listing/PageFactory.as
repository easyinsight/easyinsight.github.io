package com.easyinsight.listing
{
	import com.easyinsight.intro.CompanyInfoPanel;
	import com.easyinsight.intro.FAQPanel;
	import com.easyinsight.intro.IntroPanel;
	
	public class PageFactory
	{
		private static var introPanel:IntroPanel = new IntroPanel();
		private static var blogPanel:BlogPanel = new BlogPanel();
		private static var faqPanel:FAQPanel = new FAQPanel();
		private static var companyPanel:CompanyInfoPanel = new CompanyInfoPanel();
		
		public function PageFactory()
		{
		}

		public static function getIntroPanel():IntroPanel {
			return introPanel;
		}
		
		public static function getBlogPanel():BlogPanel {
			return blogPanel;
		}
		
		public static function getFAQPanel():FAQPanel {
			return faqPanel;			
		}
		
		public static function getCompanyInfoPanel():CompanyInfoPanel {
			return companyPanel;
		}
	}
}