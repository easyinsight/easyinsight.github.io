///////////////////////////////////////////////////////////////////////////////
//
//  Copyright (C) 2007-2008 ILOG, S.A.
//  All Rights Reserved.
//  The following is ILOG Source Code.  No modifications may be made to the
//  ILOG Source Code.  Any usage of the ILOG Source Code is subject to
//  the terms and conditions of the ILOG End User License Agreement
//  applicable to this ILOG software product.
//
///////////////////////////////////////////////////////////////////////////////

package ilog.orgchart
{
  import flash.display.Bitmap;
  import flash.display.BitmapData;
  import flash.display.DisplayObject;
  import flash.display.Graphics;
  import flash.display.Shape;
  import flash.events.Event;
  import flash.events.IEventDispatcher;
  import flash.events.KeyboardEvent;
  import flash.events.MouseEvent;
  import flash.geom.Matrix;
  import flash.geom.Point;
  import flash.geom.Rectangle;
  import flash.ui.Keyboard;
  import flash.utils.getDefinitionByName;

  import ilog.utils.AnimatedZoomReticle;
  import ilog.utils.AssetsUtil;
  import ilog.utils.CSSUtil;
  import ilog.utils.DataUtil;
  import ilog.utils.GraphicsUtil;
  import ilog.utils.ItemRendererCache;

  import mx.collections.HierarchicalCollectionView;
  import mx.collections.HierarchicalData;
  import mx.collections.ICollectionView;
  import mx.collections.IHierarchicalCollectionView;
  import mx.collections.IHierarchicalData;
  import mx.collections.IViewCursor;
  import mx.collections.errors.ItemPendingError;
  import mx.controls.listClasses.IListItemRenderer;
  import mx.core.ClassFactory;
  import mx.core.EventPriority;
  import mx.core.IFactory;
  import mx.core.IFlexDisplayObject;
  import mx.core.IInvalidating;
  import mx.core.UIComponent;
  import mx.core.UITextField;
  import mx.effects.Tween;
  import mx.effects.easing.Exponential;
  import mx.events.CollectionEvent;
  import mx.events.CollectionEventKind;
  import mx.events.FlexEvent;
  import mx.events.PropertyChangeEvent;
  import mx.graphics.IStroke;
  import mx.graphics.Stroke;
  import mx.managers.IFocusManagerComponent;
  import mx.styles.CSSStyleDeclaration;
  import mx.utils.UIDUtil;


  //----------------------------------------------------------------------------
  //
  // Style
  //
  //----------------------------------------------------------------------------

  /**
   * The duration in milliseconds of the view animations.
   * @default 1000
   */
  [Style(name="animationDuration", type="Number", format="Time", inherit="no")]


  /**
   *  Easing function to control view animations.
   *  @default "mx.effects.easing.Exponential.easeOut"
   */
  [Style(name="easingFunction", type="Function", inherit="no")]

  /**
   * The duration in milliseconds of the fade transition between local item or view mode changes.
   * @default 1300
   */
  [Style(name="fadeDuration", type="Number", format="Time", inherit="no")]

  /**
   * The background color used during the fade animation.
   * @default #FFFFFF
   */
  [Style(name="fadeBackgroundColor", type="uint", format="Color", inherit="no")]


  /**
   *  The color of a link.
   *
   *  @default 0x329CC3
   */
  [Style(name="linkColor", type="uint", format="Color", inherit="no")]

  /**
   * The radius of rounded link corners.<br/>
   * A radius of <code>0</code> means that the corners are not rounded.<br/>
   * Rounded corners are not dashed for assistants.
   *
   * @default 0
   */
  [Style(name="linkRadius", type="int", format="Number", inherit="no")]

  /**
   *  The border width of a link.
   *
   *  @default 3
   */
  [Style(name="linkThickness", type="int", format="Number", inherit="no")]

  /**
   * The view position when a data provider is set. Valid values are <code>"fitToContents"</code> or
   * <code>"centeredOnRoot"</code>.
   * @default "centeredOnRoot"
   */
  [Style(name="initialPosition", type="String", enumeration="fitToContents,centeredOnRoot", inherit="no")]

  /**
   *  The <code>styleName</code> applied to the item renderers.
   * @default null
   */
  [Style(name="itemStyleName", type="Object", inherit="no")]

  /**
   * The x-axis padding used in the layout algorithm.
   * @default 100
   */
  [Style(name="layoutXPadding", type="Number",  inherit="no")]

  /**
   * The y-axis padding used in the layout algorithm.
   * @default 20
   */
  [Style(name="layoutYPadding", type="Number",  inherit="no")]

  /**
   * Pan cursor.
   */
  [Style(name="panCursor", type="Bitmap", format="", inherit="no")]

  /**
   * Indicates that a zoom reticle will appear when zooming with the mouse wheel.
   * @default true
   */
  [Style(name="showZoomReticle", type="Boolean",  enumeration="true,false", inherit="no")]


  //----------------------------------------------------------------------------
  //
  // Events
  //
  //----------------------------------------------------------------------------

  /**
   * Dispatched when the user clicks an item in the chart.
   *
   * @eventType ilog.orgchart.OrgChartEvent.ITEM_CLICK
   */
  [Event(name="itemClick", type="ilog.orgchart.OrgChartEvent")]

  /**
   * Dispatched when the user double-clicks an item in the chart.
   *
   * @eventType ilog.orgchart.OrgChartEvent.ITEM_DOUBLE_CLICK
   */
  [Event(name="itemDoubleClick", type="ilog.orgchart.OrgChartEvent")]

  /**
   * Dispatched when the <code>selectedItems</code> property
   * changes as a result of user interaction.
   *
   *  @eventType ilog.orgchart.OrgChartEvent.CHANGE
   */
  [Event(name="change", type="ilog.orgchart.OrgChartEvent")]

  /**
   *  Dispatched when the user rolls the mouse pointer over
   *  an item in the organization chart.
   *
   *  @eventType ilog.orgchart.OrgChartEvent.ITEM_ROLL_OVER
   */
  [Event(name="itemRollOver", type="ilog.orgchart.OrgChartEvent")]

  /**
   *  Dispatched when the user rolls the mouse pointer out
   *  of an item in the organization chart.
   *
   *  @eventType ilog.orgchart.OrgChartEvent.ITEM_ROLL_OUT
   */
  [Event(name="itemRollOut", type="ilog.orgchart.OrgChartEvent")]

  //----------------------------------------------------------------------------
  //
  // Defaults
  //
  //----------------------------------------------------------------------------

  [IconFile("OrgChart.png")]

  [DefaultProperty("dataProvider")]



  /**
   *  The organization chart control allows you to view hierarchical data arranged
   *  as a tree view.
   *
   *  @mxml
   *  <p>
   *  The <code>&lt;ilog:OrgChart&gt;</code> tag inherits all the tag attributes of its superclass and
   *  adds the following tag attributes:
   *  </p>
   *  <pre>
   *  &lt;ilog:OrgChart
   *    <b>Properties</b>
   *    allowMultipleSelection="false|true"
   *    allowNavigation="false|true"
   *    allowSelection="false|true"
   *    dataProvider="null"
   *    fields="<i>Instance of OrgChartFields</i>"
   *    itemRenderer="<i>Instance of IFactory</i>"
   *    localItem="null"
   *    lodLevels="[]"
   *    lowerLevelLimit="1"
   *    maxZoomLevel="-1"
   *    scale="<i>Current scale</i>"
   *    selectedItems="[]"
   *    upperLevelLimit="1"
   *    viewMode="global"
   *
   *    <b>Events</b>
   *    change="<i>No default</i>"
   *    itemDoubleClick="<i>No default</i>"
   *    itemRollOut="<i>No default</i>"
   *    itemRollOver="<i>No default</i>"
   *    itemClick="<i>No default</i>"
   *  /&gt;
   *  </pre>
   *  @ilog.reviewed Whole file Shirley 2 Jan 08
   *
   *  @includeExample examples/OrgChartExample.mxml -noswf
   */
  public class OrgChart extends UIComponent implements IFocusManagerComponent
  {

    private static var classConstructed:Boolean = initStyles();

    private static function initStyles():Boolean {
      var styleDeclaration:CSSStyleDeclaration = CSSUtil.createSelector("OrgChart");
      styleDeclaration.defaultFactory = function():void {
        this.linkColor = 0x329CC3;
        this.linkRadius = 0;
        this.linkThickness = 3.0;
        this.itemStyleName = null;
        this.layoutXPadding = 100.0;
        this.layoutYPadding = 20.0;
        this.panCursor = AssetsUtil.PAN_CURSOR;
        this.showZoomReticle = true;
        this.initialPosition = ROOT_INIT_POSITION;
        this.animationDuration = 1000;
        this.fadeDuration = 1300;
        this.fadeBackgroundColor = 0xFFFFFF;
        this.easingFunction = Exponential.easeOut;
      }
      return true;
    };

    private var _itemRenderer:IFactory = new ClassFactory(OrgChartItemRenderer);
    private var _itemRendererCache:ItemRendererCache;

    private var _hasRoot:Boolean = false;
    private var _model:Object;
    private var _virtualRootParents:Array = []; //list of items
    private var _localItem:Object = null;
    private var _root:Object = null;
    private var _rootModel:IHierarchicalData;
    private var _collection:IHierarchicalCollectionView;
    private var _dataProviderChanged:Boolean = false;
    private var _iterator:IViewCursor;

    private var _highlightedItem:Object = null;
    private var _itemRenderers:Array = []; // item uid -> item renderer
    private var _layoutInfo:Array = []; // item uid -> layout info for this item
    private var _upperLevelLimit:int = 1;
    private var _lowerLevelLimit:int = 1;
    private var _depthLimit:int = 0; // the depth from the virtual root in local mode
    private var _minZoomLevel:Number;
    private var _maxZoomLevel:Number;
    private var _minZoomLevelSet:Boolean = false;
    private var _maxZoomLevelSet:Boolean = false;


    private var _openNodes:Object;

    /**
     * Constant: The initial view position shows the entire diagram.
     * Set the constant on the <code>initialPosition</code> style property.
     */
    public static const FIT_INIT_POSITION:String = "fitToContents";


    /**
     * Constant: The initial view position is centered on the root.
     * Set the constant on the <code>initialPosition</code> style property.
     */
    public static const ROOT_INIT_POSITION:String = "centeredOnRoot";

    /**
     * @private
     */
    internal static const I_GLOBAL_VIEW_MODE:int = 0;

    /**
     * @private
     */
    internal static const I_LOCAL_VIEW_MODE:int = 1;

    /**
     * Constant: the local view mode.
     * @see #viewMode
     */
    public static const GLOBAL_VIEW_MODE:String = "global";

    /**
     * Constant: the global view mode.
     * @see #viewMode
     */
    public static const LOCAL_VIEW_MODE:String = "local";

    private var _viewMode:int = I_GLOBAL_VIEW_MODE;

   /**
    * Constant: the name of the standard layout mode.
    * <p>
    *   <img src="layouttree.png" />
    * </p>
    *
    * @includeExample examples/LayoutStdExample.mxml -noswf
    */
    public static const STANDARD:String = "standard";

   /**
    * Constant: the name of the both-hanging layout mode.
    * <p><img src="layoutboth.png" /></p>
    *
    * @includeExample examples/LayoutBothExample.mxml -noswf
    */
    public static const BOTH_HANGING:String = "bothHanging";

   /**
    * Constant: the name of the left-hanging layout mode.
    * <p><img src="layoutleft.png" /></p>
    *
    * @includeExample examples/LayoutLeftExample.mxml -noswf
    */
    public static const LEFT_HANGING:String = "leftHanging";

   /**
    * Constant: the name of the right-hanging layout mode.
    * <p><img src="layoutright.png" /></p>
    *
    * @includeExample examples/LayoutRightExample.mxml -noswf
    */
    public static const RIGHT_HANGING:String = "rightHanging";

    /**
     * The mouse shield to catch events in the background.
     */
    private var _mouseShield:Shape;

    private var _container:UIComponent;
    private var _linkContainer:UIComponent;

    /**
     * The bounds of the content of the container.
     */
    private var _contentBounds:Rectangle;

    private var _fitNeeded:Boolean;
    private var _modelChanged:Boolean;
    private var _layoutInvalidated:Boolean;
    private var _graphicObjectInvalidated:Boolean;
    private var _visibilityInvalidated:Boolean;
    private var _localItemChanged:Boolean;
    private var _viewModeChanged:Boolean;
    private var _initViewPositionNeeded:Boolean;
    private var _transitionState:Boolean;
    private var _clearItemRenderers:Boolean;
    private var _showSiblings:Boolean = true;

    /**
     * Creates a new <code>OrgChart</code> instance.
     */
    public function OrgChart() {

      registerEventHandlers();
    }

    /**
     * @private
     */
    override protected function commitProperties():void {
      var ir:IListItemRenderer;
      super.commitProperties();

      if (_busyCusorSet) {
        cursorManager.removeBusyCursor();
        _busyCusorSet = false;
      }

      if (_itemRendererCache == null) {
        _itemRendererCache = new ItemRendererCache(_itemRenderer, _container, 20, true,
                                                   createOrgChartItemImpl, registerItemEventHandlers,
                                                   itemToUID);
      }

      // Apply data provider change.
      if (_dataProviderChanged) {

        if (_tween) {
          _tween.endTween();
        }

        _initViewPositionNeeded = true;
        _dataProviderChanged = false;

        if (_rootModel) {

          if (_collection != null) {
            _collection.removeEventListener(CollectionEvent.COLLECTION_CHANGE,
                                          collectionChanged,
                                          false);
          }
          if (_newCol) {
            _collection = _newCol;
            _newCol = null;
          } else {
            _collection = new HierarchicalCollectionView(_rootModel);
          }
          _iterator = _collection.createCursor();

          _collection.addEventListener(CollectionEvent.COLLECTION_CHANGE,
                                          collectionChanged,
                                          false,
                                          EventPriority.DEFAULT_HANDLER, true);
          _model = _rootModel.getRoot();
        }

        if (_localItem == null) {
           _root = topRoot;
          _localItem = _root;
          _virtualRootParents = [];
          _depthLimit = _lowerLevelLimit + 1; //+1 is local node
        }

        _localItemChanged = true //force applying the view mode
        _visibilityInvalidated = true;
        _graphicObjectInvalidated = true;
        _layoutInvalidated = true;

        invalidateDisplayList();
      }

      var shouldFade:Boolean = false;

      //Apply view mode change
      if (_viewModeChanged && _rootModel != null) {
        shouldFade = true;
        if (_virtualRootParents == null) {
          installViewMode(I_GLOBAL_VIEW_MODE);
        } else {
          installViewMode(_viewMode);
        }
        if (_viewMode == I_GLOBAL_VIEW_MODE) {
          _root = topRoot;
        } else {
          _localItemChanged = true;
          if (_localItem == null) {
            _localItem = topRoot;
          }
        }
        _graphicObjectInvalidated = true;
        _layoutInvalidated = true;
        _visibilityInvalidated = true;
        _viewModeChanged = false;
      }

      // Apply virtual root change.
      if (_localItemChanged && _rootModel != null) {

        if (_tween) {
          _tween.endTween();
        }

        if (model != null) {
          var res:Object = {parents:[], found:false};
          var saveMode:int = _viewMode;
          // search in non filtered data tree.

          installViewMode(I_GLOBAL_VIEW_MODE);

          if (_localItem != null) {
            findItemRecursively(topRoot, model, _localItem, res);
            if (res.found) {
              _virtualRootParents = res.parents;
              var realUpperLimit:int = Math.min(_upperLevelLimit, res.parents.length);
              if (_viewMode == I_LOCAL_VIEW_MODE) {
                _root = realUpperLimit == 0 ? _localItem : _virtualRootParents[realUpperLimit-1];
              } else {
                _root = topRoot;
              }
              _depthLimit = realUpperLimit + _lowerLevelLimit;
            }
            installViewMode(saveMode);
          }
          if (_viewMode == I_LOCAL_VIEW_MODE) {
            _visibilityInvalidated = true;
            _graphicObjectInvalidated = true;
            shouldFade = true;
          }
          _localItemChanged = false;
        }
      }

      if (shouldFade) {
        startFading();
      }

      // The item renderers are invalidated: remove them
      if (_graphicObjectInvalidated) {
        _itemRendererCache.recycleAllItemRenderers();
        _visibilityInvalidated = true;
        _graphicObjectInvalidated = false;
        invalidateDisplayList();
      }

      // Compute layout
      if (_layoutInvalidated && _rootModel != null) {

        if (_tween) {
          _tween.endTween();
        }

        _contentBounds = layout();
        _layoutInvalidated = false;
        _visibilityInvalidated = true;
        invalidateDisplayList();
      }

    }

    /**
     * @private
     */
    public function set itemRenderer(value:IFactory):void {
      _itemRenderer = value;
      if (_measuringItemRenderer != null) {
        _container.removeChild(_measuringItemRenderer as DisplayObject);
        _measuringItemRenderer = null;
      }

      if (_itemRendererCache != null) {
        _itemRendererCache.setFactory(_itemRenderer);
      } else {
        //cache will be created with latest factory in commitProperties method
        _clearItemRenderers = true;
      }
      invalidateLayout(true);
      invalidateProperties();
      invalidateDisplayList();
    }

    /**
     * The custom item renderer for the control. You can specify a drop-in, inline, or custom item renderer.
     * The default item renderer is an <code>OrgChartItemRenderer</code> object. Note that another item renderer
     * that displays more information is available: <code>OrgChartDetailedItemRenderer</code>.
     *
     * @see OrgChartItemRenderer
     * @see OrgChartDetailedItemRenderer
     * @includeExample examples/DetailedItemRendererExample.mxml -noswf
     */
    public function get itemRenderer():IFactory {
      return _itemRenderer;
    }

    /**
     * @private
     */
    override public function styleChanged(styleProp:String):void {
      super.styleChanged(styleProp);

      if (styleProp == null) {
        invalidateLayout(true);
        _visibilityInvalidated = true;
        invalidateDisplayList();
      }

      if (styleProp == "linkThickness" || styleProp == "linkColor") {
       _visibilityInvalidated = true;
       invalidateDisplayList();
      }

      if (styleProp == "itemStyleName") {
        invalidateLayout(true);
        invalidateProperties();
      }

      if (styleProp == "panCursor") {
        _cursor = null;
        _cursorID = 0;
        var c:Object = cursor;
      }
    }

    /**
     * @private
     */
    public function set lowerLevelLimit(value:int):void {
      if (_lowerLevelLimit != value) {
        _lowerLevelLimit = value;
        if (_viewMode == I_LOCAL_VIEW_MODE) {
          _viewModeChanged = true;
          invalidateProperties();
        }
      }
    }

    [Bindable]
    [Inspectable (defaultValue="1", type="Number")]
    /**
     * The level limit that determines the number of hierarchical levels displayed below the
     * local item level in the organization chart hierarchy.
     * @default 1
     * @see #viewMode
     * @see #upperLevelLimit
     */
    public function get lowerLevelLimit():int {
      return _lowerLevelLimit;
    }

    /**
     * @private
     */
    public function set upperLevelLimit(value:int):void {
      if (_upperLevelLimit != value) {
        _upperLevelLimit = value;
        if (_viewMode == I_LOCAL_VIEW_MODE) {
          _viewModeChanged = true;
          invalidateProperties();
        }
      }
    }

    [Bindable]
    [Inspectable (defaultValue="1", type="Number")]
    /**
     * The level limit that determines the number of hierarchical levels displayed above the
     * local item level in the organization chart hierarchy.
     * @default 1
     * @see #viewMode
     * @see #lowerLevelLimit
     */
    public function get upperLevelLimit():int {
      return _upperLevelLimit;
    }

    //--------------------------------------------------------------------------
    //
    // Events
    //
    //--------------------------------------------------------------------------

    private var _globalPoint:Point = new Point();
    private var _startPoint:Point = new Point();
    private var _zoomFactor:Number = 1.3;
    private var _allowNavigation:Boolean = true;
    private var _allowLocalNavigation:Boolean = true;
    private var _allowSelection:Boolean = true;
    private var _dragged:Boolean = false;
    private var _translation:Number;
    private var _gStartPoint:Point;
    private var _cursorID:Number = 0;
    private var _cursor:Class = null;
    private var _cursorWidth:Number = 0;
    private var _cursorHeight:Number = 0;

    private function installPanningCursor():void {
      if(cursor != null && _cursorID == 0)
        _cursorID = cursorManager.setCursor(cursor, 2, - _cursorWidth / 2, - _cursorHeight / 2);
    }

    private function removeCustomCursor():void {
       if(_cursorID != 0) {
        cursorManager.removeCursor(_cursorID);
        _cursorID = 0;
      }
    }

    private function get cursor():Class {
      if(_cursor == null) {
        _cursor = getStyle("panCursor");
        if(_cursor != null) {
          var c:* = new _cursor();
          if(c is Bitmap) {
           var b:Bitmap = Bitmap(c);
           _cursorWidth = b.width;
           _cursorHeight = b.height;
          }
        }
      }
      return _cursor;
    }

    /**
     * @private
     */
    private function ignoreEvent(e:Event):Boolean {
      return !enabled;
    }

    /**
     * @private
     */
    public function set allowNavigation(value:Boolean):void {
      _allowNavigation = value;
    }

    [Inspectable(category="General", enumeration="false,true", defaultValue="true")]
    /**
     *  Indicates whether you can pan and zoom the view.
     *  @default true
     */
    public function get allowNavigation():Boolean {
      return _allowNavigation;
    }

    /**
     * @private
     */
    public function set allowLocalNavigation(value:Boolean):void {
      _allowLocalNavigation = value;
    }

    [Inspectable(category="General", enumeration="false,true", defaultValue="true")]
    /**
     *  Indicates whether you can navigate from one item to another in local mode using the local icon.
     *  @default true
     */
    public function get allowLocalNavigation():Boolean {
      return _allowLocalNavigation;
    }

    /**
     * @private
     */
    private function registerEventHandlers():void {
      addEventListener(MouseEvent.CLICK, mouseClickHandler);
      addEventListener(MouseEvent.DOUBLE_CLICK, mouseDoubleClickHandler);
      addEventListener(MouseEvent.MOUSE_WHEEL, mouseWheelHandler);
      addEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
      addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
      addEventListener(KeyboardEvent.KEY_DOWN, keyEventHandler);

    }

    private function zoom(z:Number, x:Number, y:Number, animate:Boolean=false):Boolean {

      if (_rootModel == null) {
        return false;
      }

      var oldScale:Number = _container.transform.matrix.a;
      var ok:Boolean = zoomInImpl(_container, z, x, y, _minZoomLevel, _maxZoomLevel, animate);
      var newScale:Number = _container.transform.matrix.a;

      var oldLevel:int = OrgUtil.getLodLevel(oldScale, _lodLevels);
      var newLevel:int = OrgUtil.getLodLevel(newScale, _lodLevels);

      if (_lodLevels != null && _lodLevels.length != 0 &&
          oldLevel != newLevel) {
        _visibleItemChanged = true;
      }

      _visibilityInvalidated = true;
      invalidateDisplayList();
      return ok;
    }


    /**
    * Sets the highlighted item.
    * <p>Only one item can be highlighted at the same time.</p>
    * @param item The item to highlight.
    */
    private function setHighlightedItem(item:Object):void {

      var ir:IListItemRenderer;

      if (item == _highlightedItem) {
        return;
      }

      if (_highlightedItem != null) {
        ir  = getItemRenderer(_highlightedItem, false);
      }

      _highlightedItem = item;
      invalidateItemRendererDisplayList(ir);

      ir = getItemRenderer(item, false);
      invalidateItemRendererDisplayList(ir);

    }

    private var _tempKeyEvent:Event;

    /**
     * Move the focus from the highlighted item to another one
     * depending on the direction.
     * @param direction The direction to go to look for an item.
     */
    private function moveFocus(direction:String, skipChildren:Boolean=false):void {

      if (!_allowSelection) {
        return;
      }

      var ir:IListItemRenderer
      var nextItem:Object;
      var selection:Array = selectedItems;

      if (selection.length == 0) {
        selectItem(_rootModel.getRoot());
      } else {
        var item:Object = selection[selection.length-1];
        var parent:Object = getParentItem(item);

        if (!parent && direction == "up") {
          return ; //root
        }

        var layout:String = STANDARD;
        var siblings:Array = [];
        var assistants:Array = [];
        var isAssistant:Boolean;
        var index:int;

        if (parent != null) {

          layout = DataUtil.getFieldValue(_rootModel.getData(parent), fields.layoutField, STANDARD, fields.layoutFunction) as String;

          if (layout != STANDARD &&
            layout != LEFT_HANGING &&
            layout != RIGHT_HANGING &&
            layout != BOTH_HANGING) {
            layout = STANDARD;
          }

          if (hasChildren(parent, false, true)) {
            var children:ICollectionView = getFilteredChildren(parent);
            var ic:int = 0;
            var ia:int = 0;
            for (var it:IViewCursor = children.createCursor(); !it.afterLast;) {
              var child:Object = it.current;
              var ass:Boolean = Boolean(DataUtil.getFieldValue(_rootModel.getData(child), _fields.assistantField, false, _fields.assistantFunction));

              if (ass) {
                assistants.push(it.current);
                ia++;
              } else {
                siblings.push(it.current);
                ic++;
              }

              if (child == item) {
                index = ass ? ia-1 : ic-1;
                isAssistant = ass;
              }
              try {
                it.moveNext();
              } catch (e:ItemPendingError) {
                pendingErrorHandler();
                break;
              }
            }
          }
        }

        switch(direction) {
          case "up":
            nextItem = findItemAbove(parent, item, assistants, siblings, index, layout, isAssistant);
            break;
          case "right":
            nextItem =  findItemAtRight(parent, item, assistants, siblings, index, layout, isAssistant);
            break;
          case "left":
            nextItem =  findItemAtLeft(parent, item, assistants, siblings, index, layout, isAssistant);
            break;
          case "down":
            nextItem = findItemBelow(parent, item, assistants, siblings, index, layout, isAssistant, skipChildren);
            break;

        }
      }


      if (nextItem != null) {
        selectItem(nextItem);
        dispatchEvent(new OrgChartEvent(OrgChartEvent.CHANGE, nextItem, getItemRenderer(item, false), _tempKeyEvent));
        dispatchEvent(new FlexEvent(FlexEvent.VALUE_COMMIT));
        if (_allowNavigation) {
          _ensureItemVisibilityNeeded = true;
          _ensureVisibilityItem = nextItem;
        }
        invalidateDisplayList();
      }

    }


    private var _savedHighlightedItem:Object;

    private final function workaroundNoop():void {
    }


    /**
     * Finds the item below the highlighted object.
     * @return The item or <code>null</code> if none is found.
     */
    private function findItemBelow(parent:Object, item:Object, assSiblings:Array, siblings:Array, index:int, layout:String, isAssistant:Boolean=false, skipChildren:Boolean=false):Object {
      var nextItem:Object = null;

      if (!skipChildren && hasChildren(item, false, true)) {
        var children:Array = [];
        var assistants:Array = [];
        var isAssistant:Boolean;
        _savedHighlightedItem = null;
        var childrenCol:ICollectionView = getFilteredChildren(item);

        for (var it:IViewCursor = childrenCol.createCursor(); !it.afterLast;) {
          var child:Object = it.current;
          isAssistant = Boolean(DataUtil.getFieldValue(_rootModel.getData(child), _fields.assistantField, false, _fields.assistantFunction));
          if (isAssistant) {
            assistants.push(it.current);
          } else {
            children.push(it.current);
          }
          try {
            it.moveNext();
          } catch (e:ItemPendingError) {
            pendingErrorHandler();
            break;
          }
        }

        if (assistants.length > 0) {
          nextItem = assistants[0];
        } else if (children.length > 0) {
          nextItem = children[0];
        }
      } else {
        if (parent != null) {
          if (isAssistant) {
            if (index != assSiblings.length-1 && index != assSiblings.length-2) {
              nextItem = assSiblings[index+2];
            } else {
              if (siblings.length > 0) {
                nextItem = siblings[0];
              }
            }
            _savedHighlightedItem = null;
            return nextItem;
          }
          switch (layout) {
            case RIGHT_HANGING:
            case LEFT_HANGING:
              if (index < siblings.length-1) {
                nextItem =  siblings[index+1]
                _savedHighlightedItem = null;
              } else {
                findItemOnUpperLevels(parent, item);
              }
              workaroundNoop();
              break;
            case BOTH_HANGING:
              if (index != siblings.length-1 && index != siblings.length-2) {
                nextItem = siblings[index+2];
                _savedHighlightedItem = null;
              } else {
                findItemOnUpperLevels(parent, item);
              }
              workaroundNoop();
              break;
            default:
              findItemOnUpperLevels(parent, item);
          }
        }
      }

      return nextItem;
    }

    private function findItemOnUpperLevels(parent:Object, item:Object):void {
      if (parent != _root) {
        if (_savedHighlightedItem == null) {
          _savedHighlightedItem = item;
        }
        selectItem(parent);
        moveFocus("down", true);
      } else {
        selectItem(_savedHighlightedItem);
        _savedHighlightedItem = null;
      }
    }

    /**
     * Finds the item above the highlighted object.
     * @return The item or <code>null</code> if none is found.
     */
    private function findItemAbove(parent:Object, item:Object, assSiblings:Array, siblings:Array, index:int, layout:String, isAssistant:Boolean=false):Object {

      var nextItem:Object = null;
      _savedHighlightedItem = null;

      if (item != _root && parent) {
        if (isAssistant) {
          if (index == 0 || index == 1) {
            nextItem = parent;
          } else {
            nextItem = assSiblings[index-2];
          }
          return nextItem;
        }

        switch (layout) {
          case RIGHT_HANGING:
          if (index == 0) {
              if (assSiblings.length > 0) {
                if (assSiblings.length % 2 == 0 || assSiblings.length == 1) {
                  nextItem = assSiblings[assSiblings.length-1];
                } else {
                  nextItem = assSiblings[assSiblings.length-2];
                }
              } else {
                nextItem = parent;
              }
            } else {
              nextItem = siblings[index-1];
            }
            workaroundNoop();
            break;

          case LEFT_HANGING:
            if (index == 0) {
              if (assSiblings.length > 0) {
                if (assSiblings.length % 2 == 0) {
                  nextItem = assSiblings[assSiblings.length-2];
                } else {
                  nextItem = assSiblings[assSiblings.length-1];
                }
              } else {
                nextItem = parent;
              }
            } else {
              nextItem = siblings[index-1];
            }
            workaroundNoop();
            break;

          case BOTH_HANGING:
            if (index == 0 || index == 1) {
              if (assSiblings.length > 0) {
                if (index == 0) {
                  nextItem = assSiblings[assSiblings.length % 2 == 0 ? assSiblings.length-2 : assSiblings.length-1];
                } else {
                  if (assSiblings.length % 2 == 0 || assSiblings.length == 1) {
                    nextItem = assSiblings[assSiblings.length-1];
                  } else {
                    nextItem = assSiblings[assSiblings.length-2];
                  }
                }
              } else {
                nextItem = parent;
              }
            } else {
              nextItem = siblings[index-2];
            }
            workaroundNoop();
            break;
          default:
            if (assSiblings.length > 0) {
                nextItem = assSiblings[assSiblings.length-1];
              } else {
                nextItem = parent;
              }
            workaroundNoop();
        }
      }
      return nextItem;

    }

    /**
     * Finds the item to the right of the highlighted object.
     * @return The item or <code>null</code> if none is found.
     */
    private function findItemAtRight(parent:Object, item:Object, assSiblings:Array, siblings:Array, index:int, layout:String, isAssistant:Boolean=false):Object {

      var nextItem:Object = null;
      _savedHighlightedItem = null;
      if (isAssistant) {
        nextItem = index%2 == 0 ? assSiblings[index+1] : null;
      } else {
        if (parent != null) {
          switch (layout) {
            case RIGHT_HANGING:
            case LEFT_HANGING:
              nextItem = null;
              break;
            case BOTH_HANGING:
              nextItem = index%2 == 0 ? siblings[index+1] : null;
              break;
            default:
              nextItem = index < siblings.length-1 ? siblings[index+1] : null;
          }
        }
      }
      return nextItem;
    }

    /**
     * Finds the item to the left of the highlighted object.
     * @return The item or <code>null</code> if none is found.
     */
    private function findItemAtLeft(parent:Object, item:Object, assSiblings:Array, siblings:Array, index:int, layout:String, isAssistant:Boolean=false):Object {

      var nextItem:Object = null;
      _savedHighlightedItem = null;

      if (isAssistant) {
        nextItem = index%2 == 1 ? assSiblings[index-1] : null;
      } else {
        if (parent !=null) {
          switch (layout) {
            case RIGHT_HANGING:
            case LEFT_HANGING:
              nextItem = null;
              break;
            case BOTH_HANGING:
              nextItem = index%2 == 1 ? siblings[index-1] : null;
              break;
            default:
              nextItem = index > 0 ? siblings[index-1] : null;
          }
        }
      }

      return nextItem;
    }

    /**
     * @private
     */
    private function keyEventHandler(event:KeyboardEvent):void {
      var off:int = 100;
      var oldScale:Number;
      var newScale:Number;

      if (!enabled) {
        return;
      }

      _tempKeyEvent = event;

      switch (event.keyCode) {
        case Keyboard.LEFT:
          if (event.ctrlKey) {
            moveFocus("left");
          } else {
            translateBy(_container, off, 0, true);
          }
          workaroundNoop();
          break;
        case Keyboard.UP:
          if (event.ctrlKey) {
            moveFocus("up");
          } else {
            translateBy(_container, 0, off, true);
          }
          workaroundNoop();
          break;
        case Keyboard.RIGHT:
          if (event.ctrlKey) {
            moveFocus("right");
          } else {
            translateBy(_container, -off, 0, true);
          }
          workaroundNoop();
          break;
        case Keyboard.DOWN:
          if (event.ctrlKey) {
            moveFocus("down");
          } else {
            translateBy(_container, 0, -off, true);
          }
          workaroundNoop();
          break;
        case 187:
        case Keyboard.NUMPAD_ADD:
          if (_allowNavigation) {
            zoom(_zoomFactor, unscaledWidth/2, unscaledHeight/2, true);
          }
          workaroundNoop();
          break;
        case 189:
        case Keyboard.NUMPAD_SUBTRACT:
          if (_allowNavigation) {
            zoom(1/_zoomFactor, unscaledWidth/2, unscaledHeight/2, true);
          }
          workaroundNoop();
          break;
        case Keyboard.SPACE:
          if (_highlightedItem != null) {
            dispatchChangeEvent(_highlightedItem, getItemRenderer(_highlightedItem, false), event);
          }
          workaroundNoop();
          break;
        case Keyboard.ENTER:
          if (_viewMode == I_LOCAL_VIEW_MODE) {
            localItem = _highlightedItem;
          }
          workaroundNoop();
          break;
      }
      _visibilityInvalidated = true;
      invalidateDisplayList();
      _tempKeyEvent = null;
    }

    /**
     * @private
     */
    private function registerItemEventHandlers(item:IEventDispatcher):void {
      //item.addEventListener(MouseEvent.ROLL_OVER, rollOverHandler);
      //item.addEventListener(MouseEvent.ROLL_OUT, rollOutHandler);
    }

    /**
     * Returns the item from the item provider, if it is wrapped in an <code>OrgChartItem</code>.
     */
    private static function getWrappedData(data:Object):Object {
      if (data is OrgChartItem) {
        return OrgChartItem(data).item;
      } else {
        return data;
      }
    }

    /**
     * @private
     */
    private function mouseClickHandler(event:MouseEvent):void {

      if (ignoreEvent(event) || _dragged && _translation > 2.0 ||
          event.target == _linkContainer) {
        return;
      }


      if (allowSelection && event.target == this) {
        if (selectedItems.length != 0) {
          clearSelection();
          dispatchEvent(new OrgChartEvent(OrgChartEvent.CHANGE, null, null, event));
        }
        return;
      }

      if (event.target != this._container) {

        var obj:DisplayObject = DisplayObject(event.target);
        while(obj.parent != this._container) {

          if (obj == this) {
            return;
          }

          obj = obj.parent;
        }

        var ir:IListItemRenderer = obj as IListItemRenderer;
        var data:Object = getWrappedData(ir.data);

        if (allowSelection) {

          if (obj == null) {
            clearSelection();
          } else {
            dispatchChangeEvent(data, ir, event);
          }
        }

        dispatchEvent(new OrgChartEvent(OrgChartEvent.ITEM_CLICK, data, ir, event));

      }


    }

    private function dispatchChangeEvent(item:Object, itemRenderer:IListItemRenderer, event:Event):void {

      var sel:Boolean = isItemSelected(item);
      var changed:Boolean = false;
      var ctrlKey:Boolean = false;;

      if (event is MouseEvent) {
        ctrlKey = MouseEvent(event).ctrlKey;
      } else if (event is KeyboardEvent) {
        ctrlKey = KeyboardEvent(event).ctrlKey;
      }

      if (allowMultipleSelection) {
        if (ctrlKey) {
          //toggle selection
          toggleItemSelection(item);
          _ensureVisibilityItem = item;
          _ensureItemVisibilityNeeded = true;
          changed = true;
        } else {
          if (!sel) {
            //if the item is not already selected
            //clear the selection and add this item
            selectItem(item);
            _ensureVisibilityItem = item;
            _ensureItemVisibilityNeeded = true;
            changed = true;
          }
        }
      } else {
        if (ctrlKey) {
          if (sel) {
            //if the object is selected
            //deselect it.
            clearSelection();
            changed = true;
          } else {
            //clear the current selection and
            //select the item
            selectItem(item);
            _ensureVisibilityItem = item;
            _ensureItemVisibilityNeeded = true;
            invalidateDisplayList();
            changed = true;
          }
        } else {
          if (!sel) {
           //clear the current selection and
            //select the item
            selectItem(item);
            _ensureVisibilityItem = item;
            _ensureItemVisibilityNeeded = true;
            invalidateDisplayList();
            changed = true;
          }
        }
      }

      if (changed) {
        dispatchEvent(new OrgChartEvent(OrgChartEvent.CHANGE, item, itemRenderer, event));
      }

    }

    /**
     * @private
     */
    private function mouseDoubleClickHandler(event:MouseEvent):void {
      if (ignoreEvent(event) || event.target == _linkContainer) {
        return;
      }
      if (event.target != this._container && event.target != this) {
        var obj:DisplayObject = DisplayObject(event.target);
        while(obj.parent != this._container) {
          if (obj == this) {
            return;
          }
          obj = obj.parent;
        }
        var ir:IListItemRenderer = obj as IListItemRenderer;
        dispatchEvent(new OrgChartEvent(OrgChartEvent.ITEM_DOUBLE_CLICK,
                      getWrappedData(ir.data), ir, event));
      }
    }

    /**
     * @private
     */
    private function mouseDownHandler(event:MouseEvent):void {
      if(ignoreEvent(event)) {
        return;
      }
      if(_allowNavigation) {
        // in case the mouse is released outside of the orgchart (with capture
        // to be called before anyone else)
        //systemManager.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler, true);
        // but the user can also release it outside of the window
        systemManager.stage.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);

        _dragged = false;
        _translation = 0.0;
        _globalPoint.x = event.stageX;
        _globalPoint.y = event.stageY;
        _startPoint = globalToLocal(_globalPoint);
        _gStartPoint = new Point(_startPoint.x, _startPoint.y);

        installPanningCursor();

        // we start dragging, register the move event, do that on the systemManager
        // with capture flag to be sure to get all events
        systemManager.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler, true);
        // in case the mouse goes out of the window
        systemManager.stage.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveStageHandler);

        //we reset the cursor if the mouse goes outside of the window.
        systemManager.stage.addEventListener(MouseEvent.MOUSE_OUT, mouseOutStageHandler);
        systemManager.stage.addEventListener(MouseEvent.MOUSE_OVER, mouseOverStageHandler);
      }
    }

    /**
     * @private
     */
    private function mouseOutStageHandler(e:MouseEvent):void {
      removeCustomCursor();
    }

    /**
     * @private
     */
    private function mouseOverStageHandler(e:MouseEvent):void {
      if(_dragged) {
        installPanningCursor()
      }
    }

    /**
     * @private
     * This handler is dynamically installed on a mouse down event.
     */
    private function mouseMoveHandler(event:MouseEvent):void  {

      if(ignoreEvent(event)) {
        return;
      }

      if(_allowNavigation && _startPoint != null && event.buttonDown) { // that is : dragging

        _globalPoint.x = event.stageX;
        _globalPoint.y = event.stageY;
        _dragged = true;
        var lp:Point = globalToLocal(_globalPoint);
        var dx:Number = lp.x - _startPoint.x;
        var dy:Number = lp.y - _startPoint.y;
        translateBy(_container, dx, dy);
        _visibilityInvalidated = true;
        invalidateDisplayList();
        _translation = Math.max(Math.abs(_gStartPoint.x - lp.x), Math.abs(_gStartPoint.y - lp.y));
        _startPoint.x = lp.x;
        _startPoint.y = lp.y;

      }
    }

    /**
     * @private
     */
    private function mouseUpHandler(event:Event):void  {
      if(ignoreEvent(event)) {
       return;
      }

      removeCustomCursor();

      // Remove temporarly listeners.
      systemManager.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler, true);
      systemManager.stage.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);

      systemManager.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler, true);
      systemManager.stage.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveStageHandler);

      systemManager.stage.removeEventListener(MouseEvent.MOUSE_OUT, mouseOutStageHandler);
      systemManager.stage.removeEventListener(MouseEvent.MOUSE_OVER, mouseOverStageHandler);

      _startPoint = null;
      _gStartPoint = null;

    }

    /**
     * @private
     */
    private function mouseMoveStageHandler(event:MouseEvent):void {
      //Prevent triggering of the listener two times if the cursor is in the window.
      if (event.target == stage) {
        mouseMoveHandler(event);
      }
    }

    /**
     * @private
     */
    private function mouseWheelHandler(event:MouseEvent):void {

      if(ignoreEvent(event)) {
       return;
      }

      if(_allowNavigation && !event.ctrlKey) {

        var amount:int = event.delta;
        _globalPoint.x = event.stageX;
        _globalPoint.y = event.stageY;
        var lp:Point = globalToLocal(_globalPoint);
        var z:Number = amount > 0 ? _zoomFactor : 1./_zoomFactor;
        var oldMatrix:Matrix = _container.transform.matrix;
        zoom(z, lp.x, lp.y, false);
        var newMatrix:Matrix = _container.transform.matrix;

        //test the matrices because the zoom level may not have changed
        //if we're at the limits
        if (getStyle("showZoomReticle") == true && oldMatrix.a != newMatrix.a) {
          var localPoint:Point = globalToLocal(_globalPoint);
          _zoomAnim.play(localPoint.x, localPoint.y, getStyle("linkColor"), amount < 0);
        }

        //prevent triggering the scrollbars
        event.stopPropagation();

        invalidateDisplayList();

      }
    }

    /**
     * Returns the item renderers bounding box in the <code>OrgChart</code> coordinate system.
     */
    private function getTransformedItemRendererBounds(itemRenderer:IListItemRenderer):Rectangle {
      var info:Node = getItemInfo(getWrappedData(itemRenderer.data));
      var m:Matrix = _container.transform.matrix;
      var tBounds:Rectangle = transformRectangle(m, info.bounds);
      return tBounds;
    }

    /**
     * @private
     */
    private function rollOverHandler(event:MouseEvent):void {
      if (ignoreEvent(event) || event.target == _linkContainer) {
        return;
      }

      var obj:IListItemRenderer = IListItemRenderer(event.currentTarget);

      setHighlightedItem(getWrappedData(obj.data));

      dispatchEvent(new OrgChartEvent(OrgChartEvent.ITEM_ROLL_OVER,
                    getWrappedData(obj.data), obj, event));
    }

    /**
     * @private
     */
    private function rollOutHandler(event:MouseEvent):void {
      if (ignoreEvent(event) || event.target == _linkContainer) {
        return;
      }

      var obj:IListItemRenderer = IListItemRenderer(event.currentTarget);

      dispatchEvent(new OrgChartEvent(OrgChartEvent.ITEM_ROLL_OUT, _highlightedItem, obj, event));
      setHighlightedItem(null);

    }

    //--------------------------------------------------------------------------
    //
    //  selected items
    //
    //--------------------------------------------------------------------------

    /**
     * Selects the specified item.
     */
    private function selectItem(item:Object):void{
      selectedItems = [item];
    }

    /**
     * Toggles the selection state of an item.
     */
    private function toggleItemSelection(item:Object):void {
      var sItems:Array =  selectedItems; //copy
      var index:int = sItems.indexOf(item);

      if (index != -1) {
        sItems.splice(index, 1);
      } else {
        sItems.push(item);
      }
      selectedItems = sItems;

    }

    /**
     *  Determines whether the item renderer for a data provider item
     *  is selected.
     *
     *  @param item The data provider item.
     *  @return <code>true</code> if the item is selected.
     */
    public function isItemSelected(item:Object):Boolean {
      if (!item) {
        return false;
      }
      return _selectedItems[itemToUID(item)] != undefined;
    }

    /**
     *  Determines if the item renderer for a data provider item
     *  is highlighted (is rolled over with the mouse pointer or
     *  is under the caret with keyboard navigation).
     *
     *  @param item The data provider item
     *  @return <code>true</code> if the item is highlighted
     */
    public function isItemHighlighted(item:Object):Boolean {
      if (!item || _highlightedItem == null) {
        return false;
      } else {

        return getWrappedData(item) == _highlightedItem;
      }
    }

    /**
     * @private
     */
    private var _selectedItems:Array = []; // of selected items.

    /**
     * @private
     */
    private var _oldSelectedItems:Array;

    /**
     * @private
     */
    private var _selectionChanged:Boolean;

    //to refresh components bounds on this property
    //on a user interaction that changes the selection
    [Bindable("change")]
    [Bindable("valueCommit")]
    [Inspectable(environment="none")]

    /**
     * The list of selected items.
     *
     * @default []
     */
    public function get selectedItems():Array {
      var tmp:Array = [];
      for each (var item:Object in _selectedItems) {
        tmp.push(item);
      }
      return tmp;
    }

    /**
     * @private
     */
    public function set selectedItems(items:Array):void {

      _oldSelectedItems = _selectedItems;
      _selectedItems = [];

      if (items == null) {
        return;
      }

      for each (var i:Object in items) {
        _selectedItems[itemToUID(i)] = i;
      }

      //graphic feedback refresh
      var ir:IListItemRenderer;
      for each (i in _oldSelectedItems) {
        ir = getItemRenderer(i, false);
        invalidateItemRendererDisplayList(ir);
      }
      for each (i in _selectedItems) {
        ir = getItemRenderer(i, false);
        invalidateItemRendererDisplayList(ir);
      }

      dispatchEvent(new FlexEvent(FlexEvent.VALUE_COMMIT));

    }

    /**
     * Removes all selected items from the selection. This is equivalent
     * to setting <code>null</code> for the <code>selectedItems</code> property.
     */
    public function clearSelection():void {
      selectedItems = [];
    }

    /**
     * Forces a refresh of an item to reflect the visual changes.
     */
    private function invalidateItemRendererDisplayList(item:IListItemRenderer):void {
      if (item == null) {
        return;
      }
      if (item is IFlexDisplayObject) {
        if (item is IInvalidating) {
          IInvalidating(item).invalidateDisplayList();
          IInvalidating(item).validateNow();
        }
      } else if (item is UITextField) {
        UITextField(item).validateNow();
      }
    }

    /**
     * Invalidates all the item renderers displayed.
     */
    private function invalidateDisplayedItems():void {
      for each (var i:IListItemRenderer in _itemRendererCache.itemRenderers) {
        invalidateItemRendererDisplayList(i);
      }
    }

    /**
     * @private
     */
    private var _visibleItemChanged:Boolean = false;

    /**
     * Invalidates the item renderers that are within the view bounds.
     */
    private function invalidateVisibleItems():void {
      for each (var i:IListItemRenderer in _itemRendererCache.itemRenderers) {
        if (i.visible) {
          invalidateItemRendererDisplayList(i);
        }
      }
    }

    /**
     * @private
     */
    public function set allowSelection(value:Boolean):void {
      _allowSelection = value;
    }

    [Inspectable(category="General", enumeration="false,true", defaultValue="true")]
    /**
     * Indicates whether the user can select an item.
     * @default true
     */
    public function get allowSelection():Boolean {
      return _allowSelection;
    }

    /**
     *  Storage for the <code>allowMultipleSelection</code> property.
     */
    private var _allowMultipleSelection:Boolean = false;

    [Inspectable(category="General", enumeration="false,true", defaultValue="false")]

    /**
     *  Indicates whether you can allow more than one item to be
     *  selected at the same time.
     *  If <code>true</code>, users can select multiple items using a control key.
     *  @default false
     */
    public function get allowMultipleSelection():Boolean
    {
        return _allowMultipleSelection;
    }

    /**
     *  @private
     */
    public function set allowMultipleSelection(value:Boolean):void
    {
        _allowMultipleSelection = value;
    }

    //--------------------------------------------------------------------------
    //
    // Graphic component creation
    //
    //--------------------------------------------------------------------------

    /**
     * @private
     */
    private function invalidateItemRenderers():void {
      _graphicObjectInvalidated = true;
      invalidateProperties();
    }

    /**
     * Removes all the item renderers.
     */
    private function clearItemRenderers():void {
      _itemRendererCache.clearItemRenderers();
      _measuringItemRenderer = null;
    }

    /**
    * @private
     * Returns a unique identifier for a data item.
     * This method is used to generate a unique key for associative arrays.
     */
    internal static function itemToUID(item:Object):String {
      if (!item) {
        return "null";
      }

      item = getWrappedData(item);

      return UIDUtil.getUID(item);
    }

    private function recycleItemRenderer(itemRenderer:IListItemRenderer):void {
      _itemRendererCache.recycleItemRenderer(itemRenderer);
    }

    private function getItemRenderer(data:Object, create:Boolean=true, visible:Boolean=true):IListItemRenderer {
      return _itemRendererCache.getItemRenderer(getWrappedData(data), create, visible);
    }

    private function createOrgChartItemImpl(item:Object):Object {
      if (!(item is OrgChartItem)) {
        item = createOrgChartItem(item);
      }
      return item;
    }

    /**
     * Creates the data item that will be set by an item renderer.
     * @param item The data provider item.
     * @return The <code>OrgChartItem</code> instance to be set by the item renderer.
     */
    protected function createOrgChartItem(item:Object):OrgChartItem {
      return new OrgChartItem(this, item, _rootModel.getData(item));
    }

    //--------------------------------------------------------------------------
    //
    // Layout
    //
    //--------------------------------------------------------------------------

    private var _measuringItemRenderer:IListItemRenderer;

    /**
     * Measures the bounds of an item renderer that displays the specified item.
     */
    private function getItemRendererBounds(item:Object):Rectangle {

      var itemInfo:Node = getItemInfo(item);
      if (itemInfo != null) {
        var parent:Node = itemInfo.parent;
        itemInfo.parent = null;
        var r2:Rectangle = itemInfo.bounds;
        itemInfo.parent = parent;
        return r2;
      }

      if (_measuringItemRenderer == null) {
        _measuringItemRenderer = _itemRenderer.newInstance();
        _container.addChild(DisplayObject(_measuringItemRenderer));
        _measuringItemRenderer.visible = false;
      }

      var itemStyleName:Object = getStyle("itemStyleName");
      if (_measuringItemRenderer.styleName != itemStyleName) {
        _measuringItemRenderer.styleName = itemStyleName;
      }

      _measuringItemRenderer.data = createOrgChartItem(item);
      IInvalidating(_measuringItemRenderer).invalidateProperties();
      _measuringItemRenderer.validateProperties();

      IInvalidating(_measuringItemRenderer).invalidateSize();
      _measuringItemRenderer.validateSize(true);

      var w:Number = isNaN(_measuringItemRenderer.explicitWidth) ?
                       _measuringItemRenderer.measuredWidth :
                       _measuringItemRenderer.explicitWidth;
      var h:Number = isNaN(_measuringItemRenderer.explicitHeight) ?
                       _measuringItemRenderer.measuredHeight :
                       _measuringItemRenderer.explicitHeight;

      var r:Rectangle = new Rectangle(0, 0, w, h);

      return r;
    }

    /**
     * Returns the metadata associated with this item, especially layout and
     * visibility information.
     */
    private function getItemInfo(item:Object):Node {
      var info:Node = _layoutInfo[itemToUID(getWrappedData(item))];
      if (!info) {
        return null;
      }
      return info
    }

    /**
     * Sets the metadata associated with this item.
     */
    private function setItemInfo(item:Object, itemInfo:Node):void {
      if (itemInfo == null) {
        delete _layoutInfo[itemToUID(item)];
      } else {
        _layoutInfo[itemToUID(item)] = itemInfo;
      }
    }



    /**
     * Computes the layout of the organization chart and returns the bounding box
     * of the chart. This method associates layout metadata with items so that they
     * can be moved in the next pass.
     */
    private function layout():Rectangle {

      var root:Node = createLayoutHierarchy(_root);

      //Same height for all nodes.
      var nodeHeight:Number =  getItemRendererBounds(_root).height;
      //To measure the root, we kept an invalid bounds (position is incorrect).
      //So clear it and ket the layout set the right one.
      var rootNode:Node = getItemInfo(_root);
      if (rootNode != null) {
        rootNode.invalidateBoundsCache();
      }

      var xPadding:Number = getStyle("layoutXPadding");
      var yPadding:Number = getStyle("layoutYPadding");

      var layout:Layout = new Layout(nodeHeight, xPadding/2, yPadding/2);

      layout.performLayout(root);

      return root.bbox;
    }

    /**
     * Creates the data structure to lay out.
     */
    private function createLayoutHierarchy(node:Object):Node {

      var _hasChildren:Boolean = hasChildren(node, false, true);
      var maxWidth:Number = 0;
      var maxAssWidth:Number = 0;
      var cWidth:Number = 0;
      var childrenCount:int = 0;

      var layoutNode:Node = getItemInfo(node);

      if (layoutNode == null) {
        var dim:Rectangle = getItemRendererBounds(node);
        layoutNode = new Node(node, dim.width, dim.height);
        setItemInfo(node, layoutNode);
      } else {
        //reset layout
        layoutNode.invalidate();
      }

      //layout can change between 2 layouts
      var layout:String = DataUtil.getFieldValue(_rootModel.getData(node), _fields.layoutField, STANDARD, fields.layoutFunction) as String;

      if (layout != STANDARD &&
          layout != LEFT_HANGING &&
          layout != RIGHT_HANGING &&
          layout != BOTH_HANGING) {
        layout = STANDARD;
      }

      layoutNode.layout = layout;

      if (_hasChildren) {
        var i:IViewCursor;
        var child:Object;
        var children:ICollectionView = getFilteredChildren(node);
        var isAssistant:Boolean;
        var childNode:Node;
        for (i = children.createCursor(); !i.afterLast; ) {
          child = i.current;
          cWidth = getItemRendererBounds(child).width;

          childNode = createLayoutHierarchy(child);

          isAssistant = Boolean(DataUtil.getFieldValue(_rootModel.getData(child), _fields.assistantField, false, _fields.assistantFunction));

          if (isAssistant) {
            if (maxAssWidth < cWidth) {
              maxAssWidth = cWidth;
            }
            layoutNode.addAssistant(childNode);
          } else {
            if (maxWidth < cWidth) {
              maxWidth = cWidth;
            }
            layoutNode.addChild(childNode);
          }
          try {
            i.moveNext();
          } catch (e:ItemPendingError) {
            pendingErrorHandler();
            break;
          }
        }
       }
       for each (var subNode:Node in layoutNode.children) {
         subNode.width = maxWidth;
       }
       for each (subNode in layoutNode.assistants) {
         subNode.width = maxAssWidth;
       }

      return layoutNode;
    }

    /**
     * Invalidates the layout.
     */
    private function invalidateLayout(clearCache:Boolean=false):void {
      if (clearCache) {
        _layoutInfo = [];
        _contentBounds = null;
      }
      _layoutInvalidated = true;
      invalidateProperties();
    }

    //--------------------------------------------------------------------------
    //
    // Link Drawing
    //
    //--------------------------------------------------------------------------

    /**
     * Draws visible links between item renderers.
     */
    private function drawLinks(viewBounds:Rectangle):void {
      _linkContainer.graphics.clear();
      drawLinksRecursively(getItemInfo(_root), viewBounds);
    }

    /**
     * @private
     */
    private function drawLinksRecursively(node:Node, viewBounds:Rectangle):void {

      if (!node.visible) {
        //the whole subtree is invisible so no need to draw the links
        return;
      }

      drawAssistantsLinks(node, viewBounds);

      if (node.layout == STANDARD) {
        drawLinksStandard(node,viewBounds);
      } else {
        drawHangingLayouts(node, viewBounds);
      }

    }

    private function drawAssistantsLinks(node:Node, viewBounds:Rectangle):void {

      var gy:Number;
      var nx:Number;

      var _hasAssistants:Boolean = node.assistantsCount > 0;
      var _hasChildren:Boolean = node.childrenCount > 0;

      var ngx:Number = node.bounds.x + node.bounds.width/2;
      var ngy:Number = node.bounds.y + node.bounds.height;

      var radius:Number = getStyle("linkRadius");
      var showRoundCorner:Boolean = radius != 0;

      if (_hasAssistants) {

        var n:int = 0;
        for each (var child:Node in node.assistants) {
          nx = n%2 == 0 ? child.bounds.right : child.bounds.left;
          gy = centerY(child);
          if (showRoundCorner && !_hasChildren && node.assistantsCount%2 == 1 && n == node.assistantsCount-1) {
            drawLine(ngx-radius, gy, nx, gy, viewBounds, true); //from the center link to the item renderer
          } else {
            drawLine(ngx, gy, nx, gy, viewBounds, true); //from the center link to the item renderer
          }
          drawLinksRecursively(child, viewBounds);
          n++;
        }
        var last:Node = node.assistants[node.assistantsCount-1];
        var dotted:Boolean = node.childrenCount == 0;
        if (showRoundCorner && !_hasChildren && node.assistantsCount%2 == 1 ) {
          drawLine(ngx, node.bounds.bottom , ngx, last.bounds.y + last.bounds.height/2 - radius, viewBounds, dotted); //from the parent center to the last assistant y.
          drawRoundCorner(ngx, gy, radius, 3, viewBounds);
        } else {
          drawLine(ngx, node.bounds.bottom , ngx, last.bounds.y + last.bounds.height/2, viewBounds, dotted); //from the parent center to the last assistant y.
        }

      }
    }


    /**
     * @private
     */
    private function drawHangingLayouts(node:Node, viewBounds:Rectangle):void {

      var gy:Number;
      var nx:Number;
      var radius:Number = getStyle("linkRadius");
      var showRoundCorner:Boolean = radius != 0;

      var _hasChildren:Boolean = node.childrenCount > 0;

      if (_hasChildren) {

        var ngx:Number = node.bounds.x + node.bounds.width/2;
        var ngy:Number;
        if (node.assistantsCount > 0) {
          var ass:Node = node.assistants[node.assistantsCount-1];
          ngy = ass.bounds.y + ass.bounds.height/2;
        } else {
          ngy = node.bounds.y + node.bounds.height;
        }

        var n:int = 0;


        var direction:int;
        for each (var child:Node in node.children) {

          switch (node.layout) {
            case LEFT_HANGING:
              nx = child.bounds.right;
              direction = -1;
              break;
            case RIGHT_HANGING:
              nx = child.bounds.left;
              direction = 1;
              break;
            case BOTH_HANGING:
              nx = n%2 == 0 ? child.bounds.right : child.bounds.left;
              direction = -1;
              break;
          }

          gy = centerY(child);
          if (showRoundCorner && n == node.childrenCount-1 ) {
            drawLine(ngx + direction*radius, gy, nx, gy, viewBounds);
          } else {
            drawLine(ngx, gy, nx, gy, viewBounds);
          }
          drawLinksRecursively(child, viewBounds);
          n++;
        }

        var last:Node = node.children[node.childrenCount-1];
        if ( showRoundCorner &&
            (node.layout != BOTH_HANGING ||
             node.childrenCount % 2 != 0)) {

          direction = node.layout == RIGHT_HANGING ? 1 : -1;
          var lcy:Number = last.bounds.y + last.bounds.height/2;
          drawLine(ngx, ngy , ngx, lcy - radius, viewBounds);
          drawRoundCorner(ngx, lcy, radius, direction == 1 ? 2 : 3, viewBounds);
        } else {
          drawLine(ngx, ngy , ngx, last.bounds.y + last.bounds.height/2, viewBounds);
        }

      }

    }

    /**
     * @private
     */
    private function drawLinksStandard(node:Node, viewBounds:Rectangle):void {

      var radius:Number = getStyle("linkRadius");
      var showRoundCorner:Boolean = radius != 0;


      var first:Node;
      var last:Node;

      var xPadding:Number = getStyle("layoutXPadding");
      var yPadding:Number = getStyle("layoutYPadding");

      var gx:Number;
      var gy:Number;

      var _hasChildren:Boolean = node.childrenCount > 0;

      if (_hasChildren) {

        first = node.children[0];

        for (var i:int=0; i<node.childrenCount; i++) {

          var child:Node = node.children[i];

          gx = centerX(child);
          gy = child.bounds.y - yPadding/2;

          if (showRoundCorner && node.childrenCount > 1 && (i==0 || i==node.childrenCount-1)) {
            gy += radius;
          }

          drawLine(gx, gy, gx, child.bounds.y, viewBounds);

          drawLinksRecursively(child, viewBounds);
        }

        last = node.children[node.childrenCount-1];

        var ngx:Number = centerX(node);

        var ngy:Number;

        if (node.assistantsCount > 0) {
          var ass:Node = node.assistants[node.assistantsCount-1];
          ngy = ass.bounds.y + ass.bounds.height/2;
          gy = ass.bounds.bottom + yPadding/2;
        } else {
          ngy = node.bounds.y + node.bounds.height;
          gy = node.bounds.y + node.bounds.height + yPadding/2;
        }

        gy = last.bounds.y - yPadding/2;

        drawLine(ngx, ngy, ngx, gy, viewBounds); //vertical between parent to the horizontal line
        if (showRoundCorner && node.childrenCount > 1) {
          var cx:Number = centerX(first);
          var cxl:Number = centerX(last);
          drawLine(cx+radius, gy, cxl-radius, gy, viewBounds);
          //first child
          drawRoundCorner(cx, gy, radius, 0, viewBounds);
          //last child
          drawRoundCorner(cxl, gy, radius, 1, viewBounds);
        } else {
          drawLine(centerX(first), gy, centerX(last), gy, viewBounds); //horizontal line from the first child to the last line.
        }
      }

    }

    /**
     * @private
     */
    private function centerX (node:Node):Number {
      return node.bounds.x + node.bounds.width/2;
    }

    /**
     * @private
     */
    private function centerY (node:Object):Number {
      return node.bounds.y + node.bounds.height/2;
    }


    private static var ROUND_CORNER_LIMIT:int = 4;

    private function drawRoundCorner(x:Number, y:Number, radius:Number, corner:int, viewBounds:Rectangle, color:uint=0x000000):void {

      var g:Graphics = _linkContainer.graphics;

      //corner
      //0 top left
      //1 top right
      //2 bottom left
      //3 bottom right

      if (viewBounds != null) {

        var x1:Number;
        var y1:Number;

        switch (corner) {
          case 0:
            x1 = x;
            y1 = y;
            break;
          case 1:
            x1 = x - radius;
            y1 = y;
            break;
          case 2:
            x1 = x1;
            y1 = y1 - radius;
            break;
          case 3:
            x1 = x -radius;
            y1 = y - radius;
        }

        //clip
        var r:Rectangle = new Rectangle(x1, y1, radius, radius);
        if (!viewBounds.intersects(r)) {
          return;
        }

      }

      var c:uint = color == 0x000000 ? getStyle("linkColor") : color;
      g.lineStyle(getStyle("linkThickness"), c, 1.0, true);

      var tRadius:Number = radius * scale;

      if (tRadius < ROUND_CORNER_LIMIT) {

        switch (corner) {
          case 0:
            g.moveTo(x, y + radius);
            g.lineTo(x+radius, y);
            break;
          case 1:
            g.moveTo(x - radius, y);
            g.lineTo(x, y+radius);

            break;
          case 2:
            g.moveTo(x + radius, y);
            g.lineTo(x, y - radius);

            break;
          case 3:
            g.moveTo(x, y - radius);
            g.lineTo(x-radius, y);
        }
      } else {

        switch (corner) {
          case 0:
            g.moveTo(x, y + radius);
            g.curveTo(x, y, x+radius, y);
            break;
          case 1:
            g.moveTo(x - radius, y);
            g.curveTo(x, y, x, y+radius);

            break;
          case 2:
            g.moveTo(x + radius, y);
            g.curveTo(x, y, x, y - radius);

            break;
          case 3:
            g.moveTo(x, y - radius);
            g.curveTo(x, y, x-radius, y);
        }
      }
    }

    /**
     * Draws a line from the point (x1, y1) to (x2, y2). This line is clipped with
     * the specified view bounds. Only vertical or horizontal lines are managed.
     */
    private function drawLine(x1:Number, y1:Number, x2:Number, y2:Number, viewBounds:Rectangle,dashed:Boolean=false, color:uint=0x000000):void {
      //Only vertical or horizontal lines are handled.
      var r:Object;
      if (x1>x2 || y1>y2) {
        var t:Number = x2;
        x2 = x1;
        x1 = t;
        t = y2;
        y2 = y1;
        y1 = t;
      }
      var s:Object = {x1:x1, x2:x2, y1:y1, y2:y2};
      if (viewBounds != null) {
        if (x1 == x2) {
          r = clipLink(y1, y2, x1, viewBounds.y, viewBounds.y+viewBounds.height,
                       viewBounds.x, viewBounds.x+viewBounds.width);
          y1 =  r.x1;
          y2 =  r.x2;
        } else {
          r = clipLink(x1, x2, y1, viewBounds.x, viewBounds.x+viewBounds.width,
                       viewBounds.y, viewBounds.y+viewBounds.height);
          x1 =  r.x1;
          x2 =  r.x2;
        }

        if (r.x1 == -1) {
          return;
        }
      }

      var c:uint = color == 0x000000 ? getStyle("linkColor") : color;

      if (dashed) {
        var stroke:IStroke = new Stroke(c, getStyle("linkThickness"));
        drawDashedLine(_linkContainer.graphics, stroke, s.x1, s.y1, s.x2, s.y2, x1, y1, x2, y2);
      } else {
        var g:Graphics = _linkContainer.graphics;
        g.lineStyle(getStyle("linkThickness"), c, 1.0, true);
        g.moveTo(x1, y1);
        g.lineTo(x2, y2);
      }
    }

    private static const LINE_PATTERN:Array = [5, 5];


    /**
     * Draws a clipped dashed line.
     * @param target The target graphics
     * @param strkoe The stroke to use.
     * @param rx0 Non clipped x of the first point.
     * @param ry0 Non clipped y of the first point.
     * @param rx1 Non clipped x of the second point.
     * @param ry1 Non clipped y of the second point.
     * @param x0 Clipped x of the first point.
     * @param y0 Clipped y of the first point.
     * @param x1 Cipped x of the second point.
     * @param y1 Clipped y of the second point.
     */
    private static function drawDashedLine(target:Graphics, stroke:IStroke,
                                           rx0:Number, ry0:Number, rx1:Number, ry1:Number,
                                           x0:Number, y0:Number, x1:Number, y1:Number):void {

      var patternLength:int = 0;

      for each (var i:int in LINE_PATTERN) {
        patternLength += i;
      }

      if (x0 == x1) {
        y0 -= Math.abs(y0 - ry0) % patternLength;
      } else {
        x0 -= Math.abs(x0 - rx0) % patternLength;
      }

      GraphicsUtil.drawDashedLine(target, stroke, x0, y0, x1, y1, LINE_PATTERN);
    }

    /**
     * @private
     */
    private static function clipLink(x1:Number, x2:Number, y:Number, vx1:Number,
                                     vx2:Number, vy1:Number, vy2:Number):Object {


      if (y < vy1 || y > vy2) {
        //out of the view bounds
        return {x1:-1, x2:-1};
      }

      if (x1 > x2) {
        //be sure x1 <= x2
        var t:Number;
        t = x1;
        x1 = x2;
        x2 = t;
      }

      if (x2 < vx1 || x1 > vx2) {
        //out of the view bounds
        return {x1:-1, x2:-1};
      }

      //clip
      if (x1 < vx1) {
        x1 = Math.max(vx1, x1);
      }
      if (x2 > vx2) {
        x2 = Math.min(x2, vx2);
      }
      return {x1:x1, x2:x2};

    }

    /**
     * @private
     */
    private function drawDebugRect(x:Number, y:Number, width:Number, height:Number, color:uint = 0x000000):void {
      var g:Graphics = _linkContainer.graphics;
      g.lineStyle(1, color);
      g.drawRect(x, y, width, height);
    }

    /**
     * The scale of the container.
     */
    public function get scale():Number {
      return _container.transform.matrix.a;
    }

    /**
    * @private
    */
    public function set scale(value:Number):void {
      var m:Matrix = _container.transform.matrix;
      zoomBy(value/m.a, false);
    }

    //--------------------------------------------------------------------------
    //
    //  LOD
    //
    //--------------------------------------------------------------------------

    /**
     * @private
     */
    private var _lodLevels:Array = [0.4, 0.7]; // of scales

    /**
     * @private
     */
    public function set lodLevels(value:Array):void {
      if (value == null || !(value is Array)) {
        _lodLevels = null;
      } else {
        _lodLevels = value.concat(); //copy
      }
    }

    [Inspectable(defaultValue="[0.4, 0.7]", type="Array")]
    /**
     * The level of detail levels. A level of detail is a view scale interval.
     * When the view scale goes from one level to another the item renderers
     * are invalidated. This enables them to render differently, with more or less detail, and without
     * the need to do another item layout. This property must be set if a custom item renderer
     * is specified on this component. This property is an array of <code>Numbers</code>
     * in ascending number order.
     */
    public function get lodLevels():Array {
      return _lodLevels.concat(); //copy
    }

    //--------------------------------------------------------------------------
    //
    // Model
    //
    //--------------------------------------------------------------------------

    /**
     * @private
     */
    private function collectionChanged(event:CollectionEvent):void {
      var invalidateCache:Boolean = true;
      var item:Object;
      var ir:IListItemRenderer;

      //we're in the middle of some changes: don't process the event;
      if (_transitionState) {
        return;
      }

      switch (event.kind) {
        case CollectionEventKind.UPDATE:
          //an update occurred. It invalidates the layout and the item renderer but
          //not the other item renderers.
          //So we remove the item renderer info of the updated items from the cache.
          for each (var propEvent:PropertyChangeEvent in event.items) {
            item = propEvent.source;
            ir = getItemRenderer(item, false);
            if (ir != null) {
              ir.data = createOrgChartItem(item); //force update
            }
            setItemInfo(item, null); //for measurement
          }
          //and keep the cache
          invalidateCache = false;
          break;

        case CollectionEventKind.REFRESH:
        case CollectionEventKind.RESET:
          clearSelection();
          invalidateCache = true;
          break;

        case CollectionEventKind.ADD:
          invalidateCache = false;
          //we suppose that there's not too many items
          for each (item in event.items) {
            _collection.openNode(item);
          }
          break;

        case CollectionEventKind.REMOVE:

          var selection:Array = selectedItems;

          for each (item in event.items) {
            if (item == _localItem) {
              _localItem = _root;
            }
            if (isItemSelected(item)) {
              selection.splice(selection.indexOf(item), 1);
            }
            if (isItemHighlighted(item)) {
              setHighlightedItem(null);
            }
            setItemInfo(item, null); //for measurement
          }
          if (selection.length != selectedItems.length) {
            selectedItems = selection;
          }
          invalidateCache = false;

          break;

        case CollectionEventKind.MOVE:
          invalidateCache = false;
          break;

        case CollectionEventKind.REPLACE:

          selection = selectedItems;
          for each (propEvent in event.items) {
            item = propEvent.oldValue;
            if (item != null) {
              if (isItemSelected(item)) {
                selection.splice(selection.indexOf(item), 1);
              }
              if (isItemHighlighted(item)) {
                setHighlightedItem(null);
              }
              setItemInfo(item, null); //for measurement
            }
            item = propEvent.newValue;
            if (item != null) {
              if (!isOpen(item)) {
                _collection.openNode(item);
              }
              setItemInfo(item, null); //for measurement
            }
          }
          if (selection.length != selectedItems.length) {
            selectedItems = selection;
          }
          invalidateCache = false;
          break;

      }

      invalidateLayout(invalidateCache);
      invalidateItemRenderers();
      invalidateDisplayList();
      dispatchEvent(event);
    }

    /**
     * Returns the root of the model displayed by this renderer.
     * @private
     */
    private function get topRoot():Object {
      if (_rootModel == null) {
        return null;
      } else {
        return _rootModel.getRoot();
      }
    }

    /**
     * @private
     */
    public function set showSiblings(value:Boolean):void {
      _showSiblings = value;
      _localItemChanged = true;
      invalidateLayout();
      invalidateProperties();
    }

    [Inspectable(category="General", enumeration="false,true", defaultValue="true")]
    /**
     *  Indicates whether to display the siblings of the <code>localItem</code> in local view mode.
     *  @default true
     *  @see #viewMode
     *  @see #localItem
     */
    public function get showSiblings():Boolean {
      return _showSiblings;
    }

    [Inspectable(category="Data", defaultValue="null")]
    [Bindable]
    /**
     * The item of interest when the control is in local mode. By default, the virtual root is the root
     * of the data set as <code>dataProvider</code>. If you want to drill down to a particular
     * object in the model, without changing the <code>dataProvider</code>, set the
     * <code>virtualRoot</code> to that data.
     *
     * @see #dataProvider
     */
    public function get localItem():Object {
      return _localItem;
    }

    /**
     * @private
     */
    public function set localItem(value:Object):void {
      if (value == _localItem) {
        return;
      }
      if (value == null) {
        _localItem = topRoot;
      } else {
        _localItem = value;
      }
      _localItemChanged = true;
      invalidateLayout();
      invalidateProperties();
    }

    [Inspectable(type="String", enumeration="global,local", defaultValue="global")]
    /**
     * The view mode of this control.
     * This control has two visualization modes:
     * <ul>
     *   <li>Global: The entire tree is displayed. In this mode navigation
     *               is free. The constant used is <code>GLOBAL_VIEW_MODE</code>.</li>
     *   <li>Local: The view is centered on an item of interest. The tree is
     *              filtered to display <code>upperLevelLimit</code> levels of parents,
     *              <code>lowerLevelLimit</code> levels of children, the coworkers, and
     *              the person. The constant used is <code>LOCAL_VIEW_MODE</code>.</li>
     * </ul>
     * @see #GLOBAL_VIEW_MODE
     * @see #LOCAL_VIEW_MODE
     * @see #upperLevelLimit
     * @see #lowerLevelLimit
     * @see #localItem
     */
    public function get viewMode():String{
      return _viewMode == I_GLOBAL_VIEW_MODE ? GLOBAL_VIEW_MODE : LOCAL_VIEW_MODE;
    }

    /**
     * @private
     */
    public function set viewMode(value:String):void {
      if (value == viewMode) {
        return;
      }
      // default is global view mode.
      if (value != LOCAL_VIEW_MODE) {
        _viewMode = I_GLOBAL_VIEW_MODE;
      } else {
        _viewMode = I_LOCAL_VIEW_MODE;
      }
      _viewModeChanged = true;
      invalidateProperties();

    }

    /**
     *  @private
     *  Returns the known parent of a child item. This method returns a value
     *  only if the item was or is currently visible. Top-level items have a
     *  parent with the value <code>null</code>.
     *
     *  @param The item for which to get the parent.
     *
     *  @return The parent of the item.
     */
    private function getParentItem(item:Object):* {
      if (item == null) {
        return null;
      }
      return _collection.getParentItem(item);
    }

    /**
     *  @private
     *  Returns the stack of parents from a child item.
     */
    private function getParentStack(item:Object):Array {
      var stack:Array = [];
      if (item == null) {
          return stack;
      }
      var parent:* = getParentItem(item);
      while (parent) {
        stack.push(parent);
        parent = getParentItem(parent);
      }
      return stack;
    }

    private function isOpen(item:Object):Boolean {
      if (!_collection.openNodes) {
        return false;
      } else {
        return _collection.openNodes[itemToUID(item)] != undefined;
      }
    }

    private function expandAll():void {


      //store in an array to not break the iterators and open/close several
      //times an item
      var toOpen:Array = [];
      // Move to the first item.

      toOpen =  toOpen.concat(expandChildrenOf(_rootModel.getRoot()));

      _transitionState = true;
      _collection.openNodes = toOpen;
      _transitionState = false;

    }


    private function expandChildrenOf(item:Object):Array {
      var res:Array = [item];

      // If it is not a branch item there is nothing to do.
      if (isBranch(item)) {

        var childItems:ICollectionView;
        if (item != null &&
          _rootModel.canHaveChildren(item) &&
          _rootModel.hasChildren(item)) {

           childItems = getChildren(item);
        }
        if (childItems) {

          var childItem:Object;
          for (var i:IViewCursor = childItems.createCursor(); !i.afterLast; ) {
            childItem = i.current;

            if (isBranch(childItem)) {
              res = res.concat(expandChildrenOf(childItem));
            }
            try {
              i.moveNext();
            } catch (e:ItemPendingError) {
              pendingErrorHandler();
              break;
            }
          }
        }
      }
      return res;
    }

    /**
     *  @private
     *  Returns <code>true</code>, if the specified item is a branch item.
     *  @param item The item to inspect.
     *  @return <code>true</code> if it is a branch.
     *
     */
    private function isBranch(item:Object):Boolean {
        if (_rootModel && item != null) {
            return _rootModel.canHaveChildren(item);
        }
        return false;
    }

    /**
     *  @private
     *  Returns <code>true</code> if the specified item has children
     *  @param item The item to inspect.
     *  @return <code>true</code> if the item has children.
     */
    private function hasChildren(item:Object, traverseAll:Boolean = false, filtered:Boolean=false):Boolean {
      if (_rootModel && item != null) {
        var res:Boolean = true;

        if (!traverseAll) {
          res = isOpen(item);
        }

        if (res) {
          res = _rootModel.canHaveChildren(item) &&
                _rootModel.hasChildren(item);
        }

        if (res && filtered) {
          var col:ICollectionView = getFilteredChildren(item);
          var cursor:IViewCursor = col.createCursor();
          res = !cursor.afterLast;
        }
        return res;
      }
      return false;
    }

    /**
     *  @private
     */
    private function getChildren(item:Object):ICollectionView {
      //get the collection of children
      return _collection.getChildren(item);
    }

    private function getFilteredChildren(item:Object):ICollectionView {
      //get the collection of children
      var col:ICollectionView = _collection.getChildren(item);
      if (_viewMode == I_LOCAL_VIEW_MODE) {
        col = new FilteredCollectionView(col);
        col.filterFunction = localViewCollectionFilter;
      }
      return col;
    }

    private var _busyCusorSet:Boolean = false;
    private function pendingErrorHandler():void {
      if (!_busyCusorSet) {
        cursorManager.setBusyCursor();
        _busyCusorSet = true;
      }
    }



    /**
    * @private
    * Filters the children in local view mode in addition to opened/closed nodes.
    */
    private function localViewCollectionFilter(item:Object):Boolean {
      //local item is supposed to be not null

      var localParent:Object = getParentItem(_localItem);
      if (localParent == null) {
        return true;
      }

      if (item == _localItem) {
        return true;
      }

      var parents:Array = getParentStack(item);

      if (!_showSiblings && localParent != _localItem && localParent == parents[0]) {
        return false;
      }

      var index:int;
      //test if it's a child or sibling of the local item
      if (parents.indexOf(localParent) == -1) {
        index = _virtualRootParents.indexOf(item);
        //true if it's a parent of the local item
        return index != -1;
      }

      return true;
    }

    [Inspectable(category="Data", defaultValue="null")]

    /**
     *  An object that contains the data to be displayed.
     *  When you assign a value to this property, the <code>OrgChart</code> class handles
     *  the source data object as follows:
     *
     *  <ul><li>A String containing valid XML text is converted to an <code>IHierarchicalCollectionView</code>.</li>
     *  <li>An XMLNode is converted to an <code>IHierarchicalCollectionView</code>.</li>
     *  <li>An XMLList is converted to an <code>IHierarchicalCollectionView</code>.</li>
     *  <li>Any object that implements the <code>IHierarchicalCollectionView</code> interface is cast to
     *  an <code>IHierarchicalCollectionView</code>.</li>
     *  <li>An array is converted to an <code>IHierarchicalCollectionView</code>.</li>
     *  <li>Any other type of object is wrapped in an array with the object as its sole
     *  entry.</li></ul>
     *
     *  @default null
     *  @includeExample examples/DataProviderExample.mxml -noswf
     */
    public function get dataProvider():Object {
      return _collection;
    }

    private var _newCol:IHierarchicalCollectionView;

    /**
     *  @private
     */
    public function set dataProvider(value:Object):void {
      if (value is IHierarchicalData) {
        _rootModel = value as IHierarchicalData;
      } else if (value is IHierarchicalCollectionView) {
        _newCol = IHierarchicalCollectionView(value);
        _rootModel = _newCol.source;
      } else if (value is XML) {
        _rootModel = new HierarchicalData(value);
      } else if (value is String) {
        _rootModel = new HierarchicalData(new XML(value));
      } else if (value is Object) {
        _rootModel = new HierarchicalData(value);
      } else {
        _rootModel = null;
      }

      //Flag for processing in commitProps.
      _dataProviderChanged = true;
      _localItem = null;
      invalidateLayout(true);
      clearSelection();
      _highlightedItem = null;
      invalidateProperties();
    }

    private function get model():Object {
      return _model;
    }

   /**
    * @private
    * Applies the view mode change by opening/closing nodes.
    */
    private function installViewMode(mode:int):void {
      //the changes in the orgchart are managed in the
      //commit properties: no need to process them in
      //the collection change handler.
      _transitionState = true;
      if (mode == I_GLOBAL_VIEW_MODE) {
        expandAll();
      } else {
        var toOpen:Array = [_rootModel.getRoot()];
        openParentsOfLocalItem(toOpen);
        openChildrenOfLocalItem(toOpen);
        _collection.openNodes = toOpen;
      }
      _openNodes = _collection.openNodes;
      _transitionState = false;

    }

    /**
     * @private
     * Open the parent hierarchy to make the local item visible.
     */
    private function openParentsOfLocalItem(toOpen:Array):void {
      var parents:Array = getParentStack(_localItem);
      var children:ICollectionView;
      var child:Object;
      var parent:Object;

      _transitionState = true;
      for (var i:int = parents.length-1; i>=0; i--) {
        children = getChildren(parents[i]);
        for (var it:IViewCursor = children.createCursor(); !it.afterLast; ) {
          child = it.current;
          parent = i == 0 ? _localItem : parents[i-1];
          if (parent == child) {
            toOpen.push(child);
          }
          try {
            it.moveNext();
          } catch (e:ItemPendingError) {
            pendingErrorHandler();
            break;
          }
        }
      }
      _transitionState = false;
    }

    /**
     * Opens the children of the local item.
     */
    private function openChildrenOfLocalItem(toOpen:Array):void {
      openCloseItem(_localItem, 0, lowerLevelLimit, toOpen);
    }


    private function openCloseItem(item:Object, depth:int, maxDepth:int, toOpen:Array):void {

      var children:ICollectionView;
      var child:Object;

      if (depth < maxDepth && hasChildren(item, true, false) ) {

        toOpen.push(item);

        children = getChildren(item);

        for (var it:IViewCursor = children.createCursor(); !it.afterLast; ) {
          child = it.current;
          openCloseItem(it.current, depth+1, maxDepth, toOpen);
          try {
            it.moveNext();
          } catch (e:ItemPendingError) {
            pendingErrorHandler();
            break;
          }
        }
      }
    }

    /**
     * Looks for the specified item in the model. Works in both modes.
     * @param root The current item.
     * @param model The model traversed.
     * @param item The item to look for.
     * @param res The resulting object.
     */
    private function findItemRecursively(root:Object, model:Object, item:Object, res:Object):void {
      if (root == item) {
        res.found = true;
        return;
      }

      if (hasChildren(root, true, false)) {
        var children:ICollectionView = getChildren(root);
        for (var i:IViewCursor = children.createCursor(); !i.afterLast && !res.found; ) {
          var child:Object = i.current;
          try {
            i.moveNext();
          } catch (e:ItemPendingError) {
            pendingErrorHandler();
            break;
          }
          findItemRecursively(child, model, item, res);
          if (res.found) {
            res.parents.push(root);
          }
        }
      }
    }


    //--------------------------------------------------------------------------
    //
    // Data Fields
    //
    //--------------------------------------------------------------------------

    /**
     * @private
     */
     private var _fields:OrgChartFields = new OrgChartFields();

     /**
      * @private
      */
     public function set fields(value:OrgChartFields):void {
       if (_fields != value) {
         _fields.removeEventListener(Event.CHANGE, fieldsChangeHandler);
         if (value == null) {
           _fields = new OrgChartFields();
         } else {
           _fields = value;
         }
         _fields.addEventListener(Event.CHANGE, fieldsChangeHandler);
       }
     }

     /**
      * The data fields.
      */
     public function get fields():OrgChartFields {
       return _fields;
     }

     /**
      * @private
      */
     private function fieldsChangeHandler(event:Event):void {
       _clearItemRenderers = true;
       invalidateLayout(true);
       invalidateProperties();
       invalidateDisplayList();
     }

     /**
      * @private
      */
     override protected function resourcesChanged():void {
       _clearItemRenderers = true;
       invalidateLayout(true);
       invalidateProperties();
       invalidateDisplayList();
     }

    //--------------------------------------------------------------------------
    //
    // View management
    //
    //--------------------------------------------------------------------------

    /**
     * Zooms the chart by the specified factor.
     * @param factor The zoom factor. A factor lower than 1 zooms out and greater that 1 zooms in.
     * @param animate Indicates whether the transition is animated.
     * @return <code>true</code>, if the zoom succeeded (that is, if the zoom level has not reached its limit).
     */
    public function zoomBy(factor:Number, animate:Boolean):Boolean {
      return zoom(factor, unscaledWidth/2, unscaledHeight/2, animate);
    }

    private var _fitParams:Object = null;

    /**
     * Displays the entire content in the view.
     * @param animate Indicates whether the view transition is animated.
     */
    public function fitToContents(animate:Boolean=false):void {
      _fitNeeded = true;
      _fitParams = {
        item: null,
        animate: animate
      };
      invalidateDisplayList();
    }

    /**
     * Displays the subtree of the item.
     * @param root The root of the subtree.
     * @param animate Indicates whether the view transition is animated.
     */
    public function fitToSubTree(root:Object, animate:Boolean=false):void {
      _fitNeeded = true;
      _fitParams = {
        item: root,
        animate: animate
      };
      invalidateDisplayList();
    }

   private var _centerParams:Object = null;

    /**
     * Centers the view on the specified item.
     *
     * The <code>OrgChart</code> class must be validated before calling this method.
     * @param item The item.
     * @param centerX Centers the view on the the x-axis.
     * @param centerY Centers the view on the the y-axis.
     * @param animate Indicates whether the view transition is animated.
     */
    public function centerOn(item:Object, centerX:Boolean=true, centerY:Boolean=true, animate:Boolean=false):void {
      if (!centerX && !centerY || _rootModel == null) {
        return;
      }
      _centerParams = { item:item,
                        centerOnX:centerX,
                        centerOnY:centerY,
                        animate: animate };

      _visibleItemChanged = true;
      invalidateDisplayList();
    }

    private function doCenterOnItem():void {

      var itemInfo:Node = getItemInfo(_centerParams.item);

      if (itemInfo == null) {
        return;
      }

      var m:Matrix = _container.transform.matrix;

      //scaling matrix
      var m2:Matrix = new Matrix(m.a, 0, 0, m.d, 0, 0);

      //top left corner of the item renderer
      var p:Point = m2.transformPoint(new Point(itemInfo.bounds.x, itemInfo.bounds.y));

      //bottom right corner of the view
      var p2:Point = new Point(unscaledWidth, unscaledHeight);
      p.x -= p2.x/2;
      p.y -= p2.y/2;

      var r:Rectangle = itemInfo.bounds;
      //bottom right corner of the item renderer
      p2 = m2.transformPoint(new Point(r.width, r.height));
      p.x += p2.x/2;
      p.y += p2.y/2;

      translateTo(_container, _centerParams.centerOnX ? -p.x : m.tx, _centerParams.centerOnY ? -p.y : m.ty, _centerParams.animate);

    }

    /**
     * Returns the rectangle transformed by the specified matrix.
     */
    private static function transformRectangle(m:Matrix, r:Rectangle):Rectangle {
      var r2:Rectangle = new Rectangle();
      r2.topLeft = m.transformPoint(r.topLeft);
      r2.bottomRight = m.transformPoint(r.bottomRight);
      return r2;
    }

    private function translateBy(c:DisplayObject, dx:Number, dy:Number, animate:Boolean=false):void {

      var transMatrix:Matrix = new Matrix(1, 0, 0, 1, dx, dy);
      var tmpMatrix:Matrix = c.transform.matrix;
      tmpMatrix.concat(transMatrix);

      if (_contentBounds != null) {

        var b:Rectangle = getItemRendererBounds(_root);

        var vb:Rectangle = computeViewBounds(tmpMatrix);
        if (vb.x + vb.width - b.width < _contentBounds.x ||
            vb.x + b.width > _contentBounds.x + _contentBounds.width) {
          dx = 0;
        }

        if (vb.y + vb.height - b.height < _contentBounds.y ||
            vb.y + b.height > _contentBounds.y + _contentBounds.height) {
          dy = 0;
        }
      }

      if (dx == 0 && dy == 0) {
        return;
      }

      transMatrix = new Matrix(1, 0, 0, 1, dx, dy);

      tmpMatrix = c.transform.matrix;
      tmpMatrix.concat(transMatrix);
      if (_tween != null) {
        _tween.endTween();
      }

      var duration:Number = getStyle("animationDuration");
      if (animate && duration > 0) {
        _startMatrix = c.transform.matrix;
        _endMatrix = tmpMatrix;
        _tween = new Tween(this, 0.0, 1.0, duration);
        _tween.easingFunction = getStyle("easingFunction");
      } else {
        c.transform.matrix = tmpMatrix;
      }

    }

    private function translateTo(c:DisplayObject, tx:Number, ty:Number, animate:Boolean=false):void {
      var tmpMatrix:Matrix = c.transform.matrix;
      tmpMatrix = new Matrix(tmpMatrix.a, 0, 0, tmpMatrix.d, tx, ty);
      if (_tween != null) {
        _tween.endTween();
      }
      var duration:Number = getStyle("animationDuration");
      if (animate && duration > 0) {
        _startMatrix = c.transform.matrix;
        _endMatrix = tmpMatrix;
        _tween = new Tween(this, 0.0, 1.0, duration);
        _tween.easingFunction = getStyle("easingFunction");
      } else {
        c.transform.matrix = tmpMatrix;
      }
    }

    private function zoomInImpl(c:DisplayObject, z:Number, x:Number = NaN, y:Number = NaN, minScale:Number=0, maxScale:Number=100, animate:Boolean=false):Boolean {
      var tx:Number;
      var ty:Number;

      var ok:Boolean = true;

      if(isNaN(x)) {
        x = c.x + c.width / 2;
      }
      if(isNaN(y)) {
        y = c.y + c.height / 2;
      }

      //check scale
      var fScale:Number = c.transform.matrix.a * z;
      if (fScale >= maxScale) {
        z = maxScale / c.transform.matrix.a;
        ok = false;
      } else if (fScale <= minScale) {
        if (!_minZoomLevelSet) {
          var viewBounds:Rectangle = computeViewBounds();
          if (!viewBounds.x != _contentBounds.x) {
            fitToContents(true);
          }
          return false;
        }
        z = minScale / c.transform.matrix.a;
      }

      tx = x - z * x;
      ty = y - z * y;

      var transMatrix:Matrix = new Matrix(z, 0, 0, z, tx, ty);
      var tmpMatrix:Matrix = c.transform.matrix;
      tmpMatrix.concat(transMatrix);

      //check position

      var vb:Rectangle = computeViewBounds(tmpMatrix);

      var dx:Number = 0;
      var dy:Number = 0;

      var b:Rectangle = getItemRendererBounds(_root);

      if (vb.x + vb.width - b.width < _contentBounds.x) {
        dx = vb.x + vb.width - b.width - _contentBounds.x;
      } else if (vb.x + b.width > _contentBounds.x + _contentBounds.width) {
        dx =  vb.x + b.width - _contentBounds.x - _contentBounds.width;
      }

      if (vb.y + vb.height - b.height < _contentBounds.y) {
        dy = vb.y + vb.height - b.height - _contentBounds.y
      } else if (vb.y + b.height > _contentBounds.y + _contentBounds.height) {
        dy =  vb.y + b.height - _contentBounds.y - _contentBounds.height
      }

      if (dx != 0 || dy != 0) {
        var m:Matrix = new Matrix(1, 0, 0, 1, dx, dy);
        m.concat(tmpMatrix);
        tmpMatrix = m;
      }

      if (_tween != null) {
        _tween.endTween();
      }

      var duration:Number = getStyle("animationDuration");
      if (animate && duration > 0) {
        _startMatrix = c.transform.matrix;
        _endMatrix = tmpMatrix;
        _tween = new Tween(this, 0.0, 1.0, duration);
        _tween.easingFunction = getStyle("easingFunction");
      } else {
        c.transform.matrix = tmpMatrix;
      }

      return ok;
    }

    private  static function interpolateMatrix(f:Matrix, t:Matrix, n:Number):Matrix {
      var a:Number = (1 - n) * f.a  + n * t.a;
      var b:Number = (1 - n) * f.b  + n * t.b;
      var c:Number = (1 - n) * f.c  + n * t.c;
      var d:Number = (1 - n) * f.d  + n * t.d;
      var tx:Number = (1 - n) * f.tx + n * t.tx;
      var ty:Number = (1 - n) * f.ty + n * t.ty;
      var m:Matrix = new Matrix(a, b, c, d, tx, ty);
      return m;
    }

    private static function computeMatrix(rect:Rectangle, trect:Rectangle, m:Matrix=null):Matrix {
      if (m == null)
        m = new Matrix();
      var sx:Number = trect.width/rect.width;
      var sy:Number = trect.height/rect.height;
      m.a = sx;
      m.b = 0;
      m.c = 0;
      m.d = sy;
      m.tx = trect.x - sx * rect.x;
      m.ty = trect.y - sy * rect.y;
      return m;
    }

    private function fit(r:Rectangle, width:Number, height:Number, animate:Boolean=false):void {

      var sx:Number = width / r.width;
      var sy:Number = height / r.height;

      var scale:Number = Math.min(sx, sy);
      var tx:Number = - scale * r.x + (_container.width - scale * r.width) / 2;
      var ty:Number = - scale * r.y + (_container.height - scale * r.height) / 2;

      var tmpMatrix:Matrix = new Matrix(scale, 0, 0, scale, tx, ty);

      if (_tween != null) {
        _tween.endTween();
      }

      var duration:Number = getStyle("animationDuration");
      if (animate && duration > 0) {
        _startMatrix = _container.transform.matrix;
        _endMatrix = tmpMatrix;
        _tween = new Tween(this, 0.0, 1.0, duration);
        _tween.easingFunction = getStyle("easingFunction");
      } else {
        _container.transform.matrix = tmpMatrix;
      }

    }

    private function computeMinZoomLevel():void {
      if (_contentBounds != null) {
        var w:Number = unscaledWidth / _contentBounds.width;
        var h:Number = unscaledHeight / _contentBounds.height;
        _minZoomLevel = Math.min(w, h);
      }
    }

    private function computeMaxZoomLevel():void {

      var info:Node = getItemInfo(_root);

      if (info != null) {
        var w:Number = unscaledWidth / info.bounds.width;
        var h:Number = unscaledHeight / info.bounds.height;
        _maxZoomLevel = Math.min(w, h);
      } else {
        _maxZoomLevel = 10;
      }
    }

    /**
     * @private
     */
    public function set minZoomLevel (value:Number):void {
      if (value < 0) {
        _minZoomLevelSet = false;
        //_maxZoomLevel will be computed in updateDisplayList
      } else {
        _minZoomLevelSet = true;
        _minZoomLevel = value;
      }
    }

    [Inspectable(defaultValue="-1", type="Number")]
    /**
     * The minimum zoom level. By default this value is equal to <code>-1</code>
     * which means that this property is not defined. In that case, the minimum zoom
     * level is computed to display the entire chart.
     * @default -1
     */
    public function get minZoomLevel():Number {
      return _minZoomLevelSet ? _minZoomLevel : -1;
    }

    /**
     * @private
     */
    public function set maxZoomLevel (value:Number):void {
      if (value <0 ) {
        _maxZoomLevelSet = false;
        //_maxZoomLevel will be computed in updateDisplayList
      } else {
        _maxZoomLevelSet = true;
        _maxZoomLevel = value;
      }
    }

    [Inspectable(defaultValue="-1", type="Number")]
    /**
     * The maximum zoom level. By default this value is equal to <code>-1</code>
     * which means that this property is not defined. In that case, an arbitrary
     * maximum zoom level is be computed by the OrgChart.
     * @default -1
     */
    public function get maxZoomLevel():Number {
      return _maxZoomLevelSet ? _maxZoomLevel : -1;
    }

    /**
     * Computes the view bounds in the container coordinates system to clip
     * item renderers.
     */
    private function computeViewBounds(m:Matrix=null):Rectangle {
      if (m == null) {
        m = _container.transform.matrix;
      } else {
        m = m.clone();
      }
      m.invert();

      return transformRectangle(m, new Rectangle(0, 0, unscaledWidth, unscaledHeight));
    }

    /**
     * @private
     */
    private function updateVisibility(viewBounds:Rectangle):void {
      if (_rootModel != null) {
        updateVisibilityRecursively(_root, model, viewBounds, 0);
      }
    }

    /**
     * @private
     */
    private function updateVisibilityRecursively(node:Object, model:Object, bbox:Rectangle, depth:int):void {

      var info:Node = getItemInfo(node);

      var visible:Boolean = bbox.intersects(info.bbox);

      if (!visible && info.visible) {
        //the subtree was visible and now it's invisible
        //so hide the whole tree
        setVisibilityRecursively(node, visible, depth);
        info.visible = visible;
        return;
      }

      if (!visible && !info.visible) {
        //the whole sub tree is invisible and it's already set as invisble
        //->cut the whole subtree
        return;
      }

      info.visible = visible;

      visible = bbox.intersects(info.bounds);

      var ir:IListItemRenderer = getItemRenderer(node, false, false);

      if (visible) {
        if (ir == null) {
          ir = getItemRenderer(node, true, false);
          invalidateItemRendererDisplayList(ir);
        }

        //apply layout
        //if it's already at the correct position, does nothing.
        ir.setActualSize(info.bounds.width, info.bounds.height);
        ir.move(info.bounds.x, info.bounds.y);

        ir.styleName = getStyle("itemStyleName");

      }

      if (ir != null) {
        if (ir.visible != visible) {
          ir.visible = visible;

          if (!visible) {
            recycleItemRenderer(ir);
          } else {
            invalidateItemRendererDisplayList(ir);
          }
        }
      }

      if (hasChildren(node, false, true)) {
        var children:ICollectionView = getFilteredChildren(node);
        for (var i:IViewCursor = children.createCursor(); !i.afterLast;) {
          var child:Object = i.current;
          updateVisibilityRecursively(child, model, bbox, depth+1);
          try {
            i.moveNext();
          } catch (e:ItemPendingError) {
            pendingErrorHandler();
            break;
          }
        }
      }
    }

    /**
     * @private
     */
    private function setVisibilityRecursively(node:Object, visible:Boolean, depth:int):void {
      var ir:IListItemRenderer = getItemRenderer(node, false);

      if (ir != null) {
        ir.setVisible(visible, true);
        if (!visible) {
          recycleItemRenderer(ir);
        }
      }
      if (hasChildren(node, false, true)) {
        var children:ICollectionView = getChildren(node);
        for (var i:IViewCursor = children.createCursor(); !i.afterLast;) {
          var child:Object = i.current;
          setVisibilityRecursively(child, visible, depth+1);
          try {
            i.moveNext();
          } catch (e:ItemPendingError) {
            pendingErrorHandler();
            break;
          }
        }
      }
    }

    //--------------------------------------------------------------------------
    //
    // UIComponent implementation
    //
    //--------------------------------------------------------------------------

    private var _zoomAnim:AnimatedZoomReticle;
    private var _bitmap:Bitmap;

    /**
     * @private
     */
    override protected function createChildren():void {
      if (_container == null) {
        mask = new Shape();
        addChild(mask);

        _mouseShield = new Shape();
        addChildAt(_mouseShield, 0);

        _container = new UIComponent();
        //disable clipping
        _linkContainer = new UIComponent();
        _container.addChild(_linkContainer);
        addChild(_container);

        _bitmap = new Bitmap();
        _bitmap.visible = false;
        addChild(_bitmap);

        _zoomAnim = new AnimatedZoomReticle();
        addChild(_zoomAnim);

      }
    }

    /**
     * @private
     */
    override protected function measure():void {
      super.measure();
      measuredHeight = measuredWidth = measuredMinHeight = measuredMinWidth = 200;
      if (_fitNeeded) {
        _layoutInvalidated = true;
        invalidateDisplayList();
      }
    }

    private var _ensureItemVisibilityNeeded:Boolean = false;
    private var _ensureVisibilityItem:Object = null;

    private function ensureItemVisibility():void {

      if (_ensureVisibilityItem == null) {
        return;
      }

      //view bounds in container coordinates
      var viewBounds:Rectangle = computeViewBounds();
      var itemInfo:Node = getItemInfo(_ensureVisibilityItem);

      if (itemInfo == null) {
        return;
      }

      var bounds:Rectangle = itemInfo.bounds;

      if (!viewBounds.containsRect(bounds)) {

        //we need to move the view to make the item visible
        var m:Matrix = _container.transform.matrix;

        var tbounds:Rectangle = transformRectangle(m, itemInfo.bounds);

        var dx:Number = 0;
        var dy:Number = 0;
        var padding:int = 10;

        viewBounds = new Rectangle(0, 0, width, height);

        if (tbounds.right > viewBounds.right) {
          dx = viewBounds.right - tbounds.right - padding;
        } else if (tbounds.left < viewBounds.left) {
          dx = viewBounds.left - tbounds.left  + padding;
        }

        if (tbounds.top < viewBounds.top) {
          dy = viewBounds.top - tbounds.top + padding;
        } else if (tbounds.bottom > viewBounds.bottom) {
          dy = viewBounds.bottom - tbounds.bottom - padding;
        }

        translateBy(_container, dx, dy, true);
      }

      _ensureVisibilityItem = null;

    }

    /**
     * @private
     */
    private var _updateContainer:Boolean = true;

    private var _identityNeeded:Boolean = false;

    private function setIdentity():void {
      var m:Matrix = _container.transform.matrix; //clone
      var z:Number = 1/m.a;
      var x:Number = _container.width / 2;
      var y:Number = _container.height / 2;
      var tx:Number = x - z * x;
      var ty:Number = y - z * y;
      var tmp:Matrix = new Matrix(z, 0, 0, z, tx, ty);
      m.concat(tmp);
      _container.transform.matrix = m;
    }

    /**
     * Sets the zoom level of the OrgChart to 1.0, which is the identity.
     */
    public function resetZoom():void {
      _identityNeeded = true;
      invalidateDisplayList();
    }

    private function checkLimits():void {
      var scale:Number = _container.transform.matrix.a;
      //round as the mouse wheel zoom can be different at ~ 10-6
      if (Math.round(scale*100000) < Math.round(_minZoomLevel*100000)) {
        _fitNeeded = true;
      }

    }

    private function applyViewChanges():void {

      if (_initViewPositionNeeded) {
        if (getStyle("initialPosition") == FIT_INIT_POSITION) {
          _fitNeeded = true;
          _fitParams = null;
        } else {
          _centerParams = {
            item : _root,
            centerOnX: true,
            centerOnY: false,
            animate: false
          };
        }
        _initViewPositionNeeded = false;
      }

      checkLimits();

      if (_ensureItemVisibilityNeeded) {
        ensureItemVisibility();
        _ensureItemVisibilityNeeded = false;
      }

      if (_fitNeeded) {
        var rect:Rectangle = _contentBounds;
        if (_fitParams != null && _fitParams.item != null) {
          var itemInfo:Node = getItemInfo(_fitParams.item);
          if (itemInfo != null) {
            rect = itemInfo.bbox;
          }
        }
        fit(rect, unscaledWidth, unscaledHeight, _fitParams == null ? false : _fitParams.animate);
        invalidateVisibleItems();
        _fitNeeded = false;

      } else if (_centerParams != null) {
        doCenterOnItem()
        _centerParams = null;
      }

    }

    /**
     * @private
     */
    override protected function updateDisplayList(unscaledWidth:Number,
                                                  unscaledHeight:Number):void {

      if (_updateContainer) {
        _updateContainer = false;
        _container.move(0, 0);
      }

      _container.setActualSize(unscaledWidth, unscaledHeight);

      _mouseShield.width = unscaledWidth;
      _mouseShield.height = unscaledHeight;
      _mouseShield.x = 0;
      _mouseShield.y = 0;

      setChildIndex(_mouseShield, enabled ? 0 : numChildren-1);

      var g:Graphics = _mouseShield.graphics;
      g.clear();
      g.beginFill(0xffffff, enabled ? 0.0 : 0.7);
      g.drawRect(0, 0, unscaledWidth, unscaledHeight);

      g = Shape(mask).graphics;
      g.clear();
      g.beginFill(0x000000, 0.0);
      g.drawRect(0, 0, unscaledWidth, unscaledHeight);

      if (_rootModel == null) {
        return;
      }

      if (_clearItemRenderers) {
        clearItemRenderers();
        _clearItemRenderers = false;
      }

      if(!_minZoomLevelSet) {
        computeMinZoomLevel();
      }
      if (!_maxZoomLevelSet) {
        computeMaxZoomLevel();
      }

     applyViewChanges();

      if (_visibilityInvalidated && _root != null) {
        var r:Rectangle = computeViewBounds();
        updateVisibility(r);
        drawLinks(r);
      }

      if (_visibleItemChanged) {
        invalidateVisibleItems();
        _visibleItemChanged = false;
      }

      if (_rootModel == null) {
        _linkContainer.graphics.clear();
      }

    }

    // Effects

    private var _tween:Tween;
    private var _startMatrix:Matrix;
    private var _endMatrix:Matrix;

    /**
     * @private
     */
    public function onTweenUpdate(value:Object):void {

      if (_startMatrix.a != _endMatrix.a) {
       var oldScale:Number = _container.transform.matrix.a;
      }

      _container.transform.matrix = interpolateMatrix(_startMatrix, _endMatrix, value as Number);

      if (_startMatrix.a != _endMatrix.a) {
         var newScale:Number = _container.transform.matrix.a;

        var oldLevel:int = OrgUtil.getLodLevel(oldScale, _lodLevels);
        var newLevel:int = OrgUtil.getLodLevel(newScale, _lodLevels);

        if (_lodLevels != null && _lodLevels.length != 0 &&
            oldLevel != newLevel) {
          _visibleItemChanged = true;
        }
      }

       _visibilityInvalidated = true;
      invalidateDisplayList();
    }


    /**
     * @private
     */
    public function onTweenEnd(value:Object):void {
      _tween = null;
    }


    private var _fadingTween:Tween;

    private function startFading():void {

      if (getStyle("fadeDuration") == 0 || unscaledHeight < 1 || unscaledWidth < 1) {
        return;
      }

      if (_fadingTween) {
        _fadingTween.endTween();
      }

      var bitmapData:BitmapData = new BitmapData(unscaledWidth, unscaledHeight, false, getStyle("fadeBackgroundColor"));
      bitmapData.draw(this);
      _bitmap.bitmapData = bitmapData;
      _bitmap.visible = true;

      _fadingTween = new Tween(getFadeTweenListener(), 1, 0, getStyle("fadeDuration"), -1);
      _allowNavigation = false;
      _allowSelection = false;
    }

    private function getFadeTweenListener():Object {
      var saveAllowNavigation:Boolean = _allowNavigation;
      var saveAllowSelection:Boolean = _allowSelection;

      var l:Object = {
        onTweenUpdate: function (value:Object):void {
          _bitmap.alpha = value as Number;
        },
        onTweenEnd: function (value:Object):void {
          _bitmap.visible = false;
          _bitmap.alpha = 1;
          _fadingTween = null;
          _allowNavigation = saveAllowNavigation;
          _allowSelection = saveAllowSelection;
        }
      }
      return l;
    }

  }
}