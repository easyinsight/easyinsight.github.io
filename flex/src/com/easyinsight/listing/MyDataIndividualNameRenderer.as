/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/28/11
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.etl.LookupTableDescriptor;
import com.easyinsight.etl.LookupTableSource;
import com.easyinsight.framework.DataFolder;
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.report.ReportAnalyzeSource;
import com.easyinsight.scorecard.ScorecardDescriptor;
import com.easyinsight.solutions.InsightDescriptor;

import flash.display.DisplayObject;

import flash.events.MouseEvent;
import flash.geom.Rectangle;
import flash.text.StyleSheet;
import flash.text.TextFormat;
import flash.text.TextLineMetrics;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.IUITextField;
import mx.core.UIComponent;
import mx.core.UITextField;
import mx.core.UITextFormat;

public class MyDataIndividualNameRenderer extends UIComponent implements IListItemRenderer, IUITextField {

    private var textField:UITextField;

    public function MyDataIndividualNameRenderer() {
        super();

        textField = new UITextField();
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(textField);
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        textField.move(0, 0);
        textField.setActualSize(unscaledWidth, unscaledHeight);
    }

    private function onRollover(event:MouseEvent):void {
        setStyle("textDecoration", "underline");
    }

    private function onRollout(event:MouseEvent):void {
        setStyle("textDecoration", "none");
    }

    private var eiTarget:EIDescriptor;

    private var clickable:Boolean = false;

    public function set data(val:Object):void {
        if (clickable && val is DataFolder) {
            removeEventListener(MouseEvent.ROLL_OVER, onRollover);
            removeEventListener(MouseEvent.ROLL_OUT, onRollout);
            removeEventListener(MouseEvent.CLICK, onClick);
        } else if (!clickable && !(val is DataFolder)) {
            addEventListener(MouseEvent.ROLL_OVER, onRollover);
            addEventListener(MouseEvent.ROLL_OUT, onRollout);
            addEventListener(MouseEvent.CLICK, onClick);
        }
        eiTarget = val as EIDescriptor;
        if (eiTarget is DataFolder) {
            useHandCursor = false;
            buttonMode = false;
            mouseChildren = true;
            clickable = false;
        } else {
            useHandCursor = true;
            buttonMode = true;
            mouseChildren = false;
            clickable = true;
        }
        textField.text = eiTarget.name;
    }

    public function get data():Object {
        return eiTarget;
    }

    private function onClick(event:MouseEvent):void {
        var analyzeSource:PerspectiveInfo = null;
        var selectedObject:Object = eiTarget;
        if (selectedObject is LookupTableDescriptor) {
            dispatchEvent(new AnalyzeEvent(new LookupTableSource(LookupTableDescriptor(selectedObject).id)));
        } else if (selectedObject is DashboardDescriptor) {
            dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.DASHBOARD_VIEW, {dashboardID:DashboardDescriptor(selectedObject).id})));
        } else if (selectedObject is ScorecardDescriptor) {
            dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.SCORECARD_VIEW, {scorecardID:ScorecardDescriptor(selectedObject).id})));
        } else if (selectedObject is InsightDescriptor) {
            analyzeSource = new ReportAnalyzeSource(InsightDescriptor(selectedObject));
            dispatchEvent(new AnalyzeEvent(analyzeSource));
        }
    }

    public function set imeMode(value:String):void {
        textField.imeMode = value;
    }

    public function get imeMode():String {
        return textField.imeMode;
    }

    public function replaceText(beginIndex:int, endIndex:int, newText:String):void {
        textField.replaceText(beginIndex, endIndex, newText);
    }

    public function get caretIndex():int {
        return 0;
    }

    public function get maxScrollH():int {
        return textField.maxScrollH;
    }

    public function get numLines():int {
        return textField.numLines;
    }

    public function get scrollH():int {
        return textField.scrollH;
    }

    public function setColor(color:uint):void {
    }

    public function get maxScrollV():int {
        return textField.maxScrollV;
    }

    public function getImageReference(id:String):DisplayObject {
        return textField.getImageReference(id);
    }

    public function get scrollV():int {
        return textField.scrollV;
    }

    public function get border():Boolean {
        return false;
    }

    public function get text():String {
        return textField.text;
    }

    public function get styleSheet():StyleSheet {
        return textField.styleSheet;
    }

    public function getCharBoundaries(charIndex:int):Rectangle {
        return textField.getCharBoundaries(charIndex);
    }

    public function get background():Boolean {
        return textField.background;
    }

    public function set scrollH(value:int):void {
        textField.scrollH = value;
    }

    public function getFirstCharInParagraph(charIndex:int):int {
        return 0;
    }

    public function get type():String {
        return textField.type;
    }

    public function replaceSelectedText(value:String):void {
    }

    public function set borderColor(value:uint):void {
    }

    public function get alwaysShowSelection():Boolean {
        return false;
    }

    public function get sharpness():Number {
        return textField.sharpness;
    }

    public function get textColor():uint {
        return 0;
    }

    public function set defaultTextFormat(format:TextFormat):void {
        textField.defaultTextFormat = format;
    }

    public function get condenseWhite():Boolean {
        return false;
    }

    public function get displayAsPassword():Boolean {
        return false;
    }

    public function get autoSize():String {
        return textField.autoSize;
    }

    public function setSelection(beginIndex:int, endIndex:int):void {
    }

    public function set scrollV(value:int):void {
    }

    public function set useRichTextClipboard(value:Boolean):void {
    }

    public function get selectionBeginIndex():int {
        return 0;
    }

    public function get selectable():Boolean {
        return false;
    }

    public function set border(value:Boolean):void {
    }

    public function set multiline(value:Boolean):void {
    }

    public function set background(value:Boolean):void {
    }

    public function set embedFonts(value:Boolean):void {
    }

    public function set text(value:String):void {
    }

    public function get selectionEndIndex():int {
        return 0;
    }

    public function set mouseWheelEnabled(value:Boolean):void {
    }

    public function appendText(newText:String):void {
    }

    public function get antiAliasType():String {
        return "";
    }

    public function set styleSheet(value:StyleSheet):void {
        textField.styleSheet = value;
    }

    public function set textColor(value:uint):void {
    }

    public function get wordWrap():Boolean {
        return textField.wordWrap;
    }

    public function getLineIndexAtPoint(x:Number, y:Number):int {
        return 0;
    }

    public function get htmlText():String {
        return "";
    }

    public function get thickness():Number {
        return 0;
    }

    public function getLineIndexOfChar(charIndex:int):int {
        return 0;
    }

    public function get bottomScrollV():int {
        return 0;
    }

    public function set restrict(value:String):void {
    }

    public function set alwaysShowSelection(value:Boolean):void {
    }

    public function getTextFormat(beginIndex:int = -1, endIndex:int = -1):TextFormat {
        return null;
    }

    public function set sharpness(value:Number):void {
    }

    public function set type(value:String):void {
        textField.type = value;
    }

    public function setTextFormat(format:TextFormat, beginIndex:int = -1, endIndex:int = -1):void {
        textField.setTextFormat(format, beginIndex, endIndex);
    }

    public function set gridFitType(gridFitType:String):void {
    }

    public function getUITextFormat():UITextFormat {
        return textField.getUITextFormat();
    }

    public function get borderColor():uint {
        return 0;
    }

    public function set condenseWhite(value:Boolean):void {
    }

    public function get textWidth():Number {
        return 0;
    }

    public function getLineOffset(lineIndex:int):int {
        return 0;
    }

    public function set displayAsPassword(value:Boolean):void {
    }

    public function set autoSize(value:String):void {
    }

    public function get defaultTextFormat():TextFormat {
        return textField.defaultTextFormat;
    }

    public function get useRichTextClipboard():Boolean {
        return false;
    }

    public function get nonZeroTextHeight():Number {
        return 0;
    }

    public function set backgroundColor(value:uint):void {
    }

    public function get embedFonts():Boolean {
        return textField.embedFonts;
    }

    public function set selectable(value:Boolean):void {
    }

    public function get multiline():Boolean {
        return textField.multiline;
    }

    public function set maxChars(value:int):void {
    }

    public function get textHeight():Number {
        return textField.textHeight;
    }

    public function getLineText(lineIndex:int):String {
        return "";
    }

    public function get mouseWheelEnabled():Boolean {
        return false;
    }

    public function get restrict():String {
        return "";
    }

    public function getParagraphLength(charIndex:int):int {
        return 0;
    }

    public function get gridFitType():String {
        return "";
    }

    public function set ignorePadding(value:Boolean):void {
    }

    public function set antiAliasType(antiAliasType:String):void {
    }

    public function get backgroundColor():uint {
        return 0;
    }

    public function getCharIndexAtPoint(x:Number, y:Number):int {
        return 0;
    }

    public function get maxChars():int {
        return 0;
    }

    public function get ignorePadding():Boolean {
        return false;
    }

    public function get length():int {
        return text.length;
    }

    public function set wordWrap(value:Boolean):void {
    }

    public function set thickness(value:Number):void {
    }

    public function getLineLength(lineIndex:int):int {
        return 0;
    }

    public function truncateToFit(truncationIndicator:String = null):Boolean {
        return false;
    }

    public function set htmlText(value:String):void {
    }

    public function getLineMetrics(lineIndex:int):TextLineMetrics {
        return textField.getLineMetrics(lineIndex);
    }
}
}
