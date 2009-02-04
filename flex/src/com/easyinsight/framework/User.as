package com.easyinsight.framework
{
	import com.easyinsight.account.AccountType;
	import com.easyinsight.store.Merchant;
	
	import mx.collections.ArrayCollection;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	
	public class User
	{
		private var name:String;
		private var email:String;
		private var accountType:AccountType;
		private var credentialsMap:Object = new Object();
		static private var _user:User;
		static private var notifier:UserEventNotifier;
		public var commercialEnabled:Boolean;
		public var spaceAllowed:int;
		public var password:String;
		public var userName:String;
        public var accountAdmin:Boolean;
        public var dataSourceCreator:Boolean;
        public var insightCreator:Boolean;

		private var merchants:ArrayCollection = new ArrayCollection();
		private var storeService:RemoteObject;
		
		static public function initializeUser(name:String, email:String, accountType:AccountType,
		spaceAllowed:int, accountAdmin:Boolean, dataSourceCreator:Boolean, insightCreator:Boolean):void {
			_user = new User();
			_user.name = name;
			_user.email = email;
            _user.accountAdmin = accountAdmin;
            _user.dataSourceCreator = dataSourceCreator;
            _user.insightCreator = insightCreator;
			_user.spaceAllowed = spaceAllowed;
			_user.accountType = accountType;
			/*_user.storeService = new RemoteObject();
			_user.storeService.destination = "store";
			_user.storeService.getMerchants.addEventListener(ResultEvent.RESULT, _user.gotMerchants);
			_user.storeService.getMerchants.send();*/
		}

        public function updateLabels(userName:String, fullName:String, email:String) {
            this.email = email;
            this.userName = userName;
            this.name = fullName;
        }

        public function resync(userTransferObject:UserTransferObject):void {
            this.accountAdmin = userTransferObject.accountAdmin;
            this.dataSourceCreator = userTransferObject.dataSourceCreator;
            this.insightCreator = userTransferObject.insightCreator;
        }
		
		private function gotMerchants(event:ResultEvent):void {
			this.merchants = storeService.getMerchants.lastResult;
		}
		
		static public function destroy():void {
			_user = null;
		}
		
		static public function getEventNotifier():UserEventNotifier {
			if (notifier == null) {
				notifier = new UserEventNotifier();
			}
			return notifier;	
		}
		
		public function getAccountType():AccountType {
			return accountType;
		}

        public function setAccountType(type:AccountType) {
            this.accountType = type;
        }
		
		public function getEmail():String {
			return email;
		}
		
		public function getName():String {
			return name;
		}
		
		public function addCredentials(type:String, credentials:Credentials):void {
			credentialsMap[type] = credentials;
		}			
		
		public function getCredentials(type:String):Credentials {
			var credentials:Credentials = credentialsMap[type];			
			return credentials;
		}
		
		public function getAllCredentials():Object {
			return credentialsMap;
		}
		
		static public function getInstance():User {
			return _user;
		}
		
		public function getMerchants():ArrayCollection {
			return this.merchants;
		}
		
		public function addMerchant(merchant:Merchant):void {
			this.merchants.addItem(merchant);
		}		
	}
}