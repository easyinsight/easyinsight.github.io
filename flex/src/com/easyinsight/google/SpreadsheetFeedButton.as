package com.easyinsight.google
{
import com.easyinsight.administration.feed.GoogleFeedDefinition;
import com.easyinsight.customupload.UploadConfigEvent;
	import com.easyinsight.framework.Credentials;
	import com.easyinsight.framework.User;
import com.easyinsight.genredata.ModuleAnalyzeEvent;
import com.easyinsight.listing.DataFeedDescriptor;
	import com.easyinsight.listing.DescriptorAnalyzeSource;

import com.easyinsight.util.ProgressAlert;

import flash.events.Event;
	import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.containers.HBox;
	import mx.controls.Alert;
	import mx.controls.Button;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class SpreadsheetFeedButton extends HBox
	{

        public static var credentials:Credentials;

		private var _data:Worksheet;
		private var remoteService:RemoteObject;
		private var button:Button;
		private var setButtonProps:Boolean = true;

        private var _buttonVisible:Boolean = false;


        [Bindable(event="buttonVisibleChanged")]
        public function get buttonVisible():Boolean {
            return _buttonVisible;
        }

        public function set buttonVisible(value:Boolean):void {
            if (_buttonVisible == value) return;
            _buttonVisible = value;
            dispatchEvent(new Event("buttonVisibleChanged"));
        }

        [Embed(source="../../../../assets/media_play_green.png")]
    	public var dataIcon:Class;	
		
		public function SpreadsheetFeedButton()
		{
			super();			
			setStyle("horizontalAlign", "center");		
		}
		
		override protected function createChildren():void {
			super.createChildren();
			if (button == null) {
				button = new Button();
				button.toolTip = "Create Data Source";
				button.setStyle("icon", dataIcon);
                button.addEventListener(MouseEvent.CLICK, subscribe);
                BindingUtils.bindProperty(button, "visible", this, "buttonVisible");
				addChild(button);	
			}
		}
		
		override protected function commitProperties():void {
			super.commitProperties();
			/*if (!setButtonProps) {
                if (_data != null) {
                    if (_data.feedDescriptor == null) {
                        button.toolTip = "Create Data Source";
                        button.addEventListener(MouseEvent.CLICK, subscribe);
                    } else {
                        button.toolTip = "Analyze";
                        button.addEventListener(MouseEvent.CLICK, analyze);
                    }
                }
			}*/
		}							

		override public function set data(value:Object):void {
            if (value is Worksheet) {
                _data = value as Worksheet;
                buttonVisible = true;
            } else {
                buttonVisible = false;
            }
		}
		
		override public function get data():Object {
			return _data;
		}
		
		public function subscribe(event:Event):void {
			//var credentials:Credentials = User.getInstance().getCredentials("google");
			remoteService = new RemoteObject();
			remoteService.destination = "userUpload";
			remoteService.newExternalDataSource.addEventListener(ResultEvent.RESULT, successfulSubscription);
			remoteService.newExternalDataSource.addEventListener(FaultEvent.FAULT, failedSubscription);
            var googleDef:GoogleFeedDefinition = new GoogleFeedDefinition();
            googleDef.feedName = _data.spreadsheet + " - " + _data.title;
            googleDef.worksheetURL = _data.url;
            ProgressAlert.alert(this.parent.parent, "Creating data source...", null, remoteService.newExternalDataSource);
			remoteService.newExternalDataSource.send(googleDef, credentials);
		}
		
		private function analyze(event:Event):void {
			this.parent.dispatchEvent(new ModuleAnalyzeEvent(new DescriptorAnalyzeSource(_data.feedDescriptor.dataFeedID, _data.feedDescriptor.name)));
		}
		
		private function successfulSubscription(event:ResultEvent):void {
			toolTip = "Analyze";			
			var id:int = remoteService.newExternalDataSource.lastResult as int;
            var descriptor:DataFeedDescriptor = new DataFeedDescriptor();
            descriptor.dataFeedID = id;
            descriptor.name = _data.spreadsheet + " - " + _data.title;
			_data.feedDescriptor = descriptor;
			//this.parent.dispatchEvent(new AnalyzeEvent(new DescriptorAnalyzeSource(descriptor)));
			dispatchEvent(new UploadConfigEvent(UploadConfigEvent.UPLOAD_CONFIG_COMPLETE, descriptor.dataFeedID, descriptor.name));
		}
		
		private function failedSubscription(event:FaultEvent):void {
			Alert.show(event.fault.message);
		}
	}
}