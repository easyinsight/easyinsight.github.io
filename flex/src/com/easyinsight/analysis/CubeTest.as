package com.easyinsight.analysis
{
	import flexunit.framework.TestCase;
	
	import mx.collections.ArrayCollection;

	public class CubeTest extends TestCase
	{
		public function CubeTest(methodName:String=null)
		{
			super(methodName);
		}
		
		public function testCube():void {
			var cube:Cube = new Cube();
			var crosstabDefinition:CrosstabDefinition = new CrosstabDefinition();
			var customerDimension:AnalysisDimension = new AnalysisDimension();
			customerDimension.key = "Customer";
			var productDimension:AnalysisDimension = new AnalysisDimension();
			productDimension.key = "Product";
			var revenue:AnalysisMeasure = new AnalysisMeasure();
			revenue.key = "Revenue";
			crosstabDefinition.rows = [ customerDimension ];
			crosstabDefinition.columns = [ productDimension ];
			crosstabDefinition.measures = [ revenue ];
			var data:ArrayCollection = new ArrayCollection([
				{ Customer: "A", Product: "1", Revenue: 100 },
				{ Customer: "A", Product: "2", Revenue: 200 },
				{ Customer: "B", Product: "1", Revenue: 300 },
				{ Customer: "B", Product: "2", Revenue: 400 },
				{ Customer: "C", Product: "1", Revenue: 500 },
				{ Customer: "C", Product: "2", Revenue: 600 } 
			]);
			cube.populate(crosstabDefinition, data, null);
		}
	}
}