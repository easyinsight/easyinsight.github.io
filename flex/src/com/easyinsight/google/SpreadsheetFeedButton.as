package com.easyinsight.google
{
import com.easyinsight.customupload.UploadResponse;
import com.easyinsight.customupload.wizard.FieldUploadInfo;


import com.easyinsight.framework.Credentials;
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.genredata.AnalyzeEvent;

import com.easyinsight.util.ProgressAlert;

import flash.events.Event;
	import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.containers.HBox;
import mx.controls.Alert;
import mx.controls.Button;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class SpreadsheetFeedButton extends HBox
	{

        public static var credentials:Credentials;

		private var _data:Worksheet;
		private var button:Button;
		private var setButtonProps:Boolean = true;
        private var uploadService:RemoteObject;

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
            var context:GoogleSpreadsheetUploadContext = new GoogleSpreadsheetUploadContext();
            context.worksheetURL = _data.url;
            uploadService = new RemoteObject();
            uploadService.destination = "userUpload";
            uploadService.analyzeUpload.addEventListener(ResultEvent.RESULT, analyzedStructure);
            ProgressAlert.alert(this.parent.parent, "Analyzing the spreadsheet...", null, uploadService.analyzeUpload);
            uploadService.analyzeUpload.send(context);
            var fieldUploadInfo:FieldUploadInfo;
		}

        private function analyzedStructure(event:ResultEvent):void {
            var uploadResponse:UploadResponse = uploadService.analyzeUpload.lastResult as UploadResponse;
            if (uploadResponse.successful) {
                var context:GoogleSpreadsheetUploadContext = new GoogleSpreadsheetUploadContext();
                context.worksheetURL = _data.url;
                //dispatchEvent(new AnalyzeEvent(new SpreadsheetSetupSource(context, uploadResponse.infos)));
                dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.SPREADSHEET_WIZARD, {uploadContext: context, fields: uploadResponse.infos})));
            } else {
                Alert.show(uploadResponse.failureMessage);
            }
        }
	}
}