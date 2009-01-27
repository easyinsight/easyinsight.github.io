package com.easyinsight.goals
{
  
  import flash.display.DisplayObject;
  
  import ilog.orgchart.OrgChartItem;
  
  import mx.containers.Canvas;
  import mx.controls.listClasses.IListItemRenderer;
  import mx.core.UIComponent;

  /**
   * This class is a base class for custom item renderers.
   * It provides API to ease level of details management and measurement.
   */ 
  public class OrgChartItemRendererBase extends Canvas implements IListItemRenderer
  {
    
    public function OrgChartItemRendererBase() {
      includeInLayout = false;
    }
        
    private var lodList:Array = [];
    
    /**
     * Configures the level of details for the children components.     
     * By default this class put each direct child in a different level.
     * Subclasses can make calls to <code>setLevelsOfDetails</code> for a 
     * another management (for example share some components between levels).  
     */  
    protected function configureLevelOfDetails():void {
      for (var i:int=0; i<numChildren; i++) {
        setLevelsOfDetails(getChildAt(i), [i]);
      }
    }
    
    /**
     * @private
     */ 
    override protected function createChildren():void {
      super.createChildren();
      configureLevelOfDetails();
    }     
    
    /**
     * Sets the levels of detals where the specified object is visible.
     * <p>The scales corresponding to each level of details are set by the 
     * <code>OrgChart.lodLevels</code> property.
     * @param child The child object to display in the specified levels.
     * @param levels The levels where the object is visible.
     */   
    protected function setLevelsOfDetails(child:DisplayObject, levels:Array /*of int*/):void {
      for each (var level:int in levels) {
        var list:Array = lodList[level];
        if (list == null) {
          list = [];
          lodList[level] = list;          
        }
        list.push(child);
      }
    } 
    
    /**
     * Removes all the components registered with the <code>setLevelsOfDetails</code> method.
     */  
    protected function clearLevelOfDetails():void {
      lodList = [];
    }
    
    /**
     * Shows the children objects that are visible in the current level of detail of the OrgChart.
     * <p>This methods must be called in the <code>updateDisplayList</code> of subclasses. 
     */  
    protected function applyLevelOfDetails():void {
      var item:OrgChartItem = data as OrgChartItem;
      var obj:DisplayObject;
      if (item != null) {
        for each (var level:Array in lodList) {
          for each (obj in level) {
            obj.visible = false;
          }
        }
        var currentLevel:int = getCurrentLOD();
        for each (obj in lodList[currentLevel]) {
          obj.visible = true;
        }
      }     
    }    
    
    /**
     * Returns the current level of detail.
     */  
    protected function getCurrentLOD():int {
      var _data:OrgChartItem = data as OrgChartItem;
      if (_data) {
        return computeLod(_data.orgChart.scale, _data.orgChart.lodLevels);
      } else {
        return 0; 
      }
      
    }
    
    /**
     * @private
     */  
    private static function computeLod(scale:Number, levels:Array):int {
      if (levels == null && levels.length == 0) {
        throw new Error();
      }  
      var res:int = -1;    
      while (res+1 != levels.length && 
             scale > levels[res+1]) {
        res++;
      }
      return res + 1;      
    }
    
    /**
     * @private
     */  
    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {      
      super.updateDisplayList(unscaledWidth, unscaledHeight);       
      applyLevelOfDetails();        
      setActualSize(unscaledWidth, unscaledHeight);
    }
           
         
  }
}