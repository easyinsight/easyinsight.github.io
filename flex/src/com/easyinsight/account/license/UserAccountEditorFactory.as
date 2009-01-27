package com.easyinsight.account.license
{
	import mx.core.IFactory;

	public class UserAccountEditorFactory implements IFactory
	{
		private var editors:Array;
		private var userMap:Object;
		
		public function UserAccountEditorFactory(editors:Array, userMap:Object) {
			this.editors = editors;
			this.userMap = userMap;
		}

		public function newInstance() {
			var userAccountEditor:UserAccountEditor = new UserAccountEditor(userMap);
			this.editors.push(userAccountEditor);
			return userAccountEditor;			
		}
		
	}
}