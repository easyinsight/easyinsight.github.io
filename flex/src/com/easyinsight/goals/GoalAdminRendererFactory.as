package com.easyinsight.goals
{
	import mx.core.IFactory;

	public class GoalAdminRendererFactory implements IFactory
	{
		public function GoalAdminRendererFactory()
		{
		}

		public function newInstance():*
		{
			return new GoalAdminRenderer2();
		}
		
	}
}