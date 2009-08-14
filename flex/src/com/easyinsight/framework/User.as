package com.easyinsight.framework
{
	import com.easyinsight.store.Merchant;

import flash.net.SharedObject;

import mx.collections.ArrayCollection;
import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	
	public class User
	{
		private var name:String;
		private var email:String;
		private var accountType:int;
		static private var _user:User;
		static private var notifier:UserEventNotifier;
        static private var sharedObject:SharedObject;
		public var commercialEnabled:Boolean;
		public var spaceAllowed:int;
		public var password:String;
		public var userName:String;
        public var accountAdmin:Boolean;
        //public var dataSourceCreator:Boolean;
        //public var insightCreator:Boolean;
        public var userID:int;
        public var activated:Boolean;

        public function User() {

        }
		
		static public function initializeUser(name:String, email:String, accountType:int,
		spaceAllowed:int, accountAdmin:Boolean, dataSourceCreator:Boolean, insightCreator:Boolean, userID:int, activated:Boolean):void {
			_user = new User();
			_user.name = name;
			_user.email = email;
            _user.accountAdmin = accountAdmin;
            //_user.dataSourceCreator = dataSourceCreator;
            //_user.insightCreator = insightCreator;
			_user.spaceAllowed = spaceAllowed;
			_user.accountType = accountType;
            _user.userID = userID;
            _user.activated = activated;
            try {
                sharedObject = SharedObject.getLocal(userID.toString());
            } catch (e:Error) {

            }

			/*_user.storeService = new RemoteObject();
			_user.storeService.destination = "store";
			_user.storeService.getMerchants.addEventListener(ResultEvent.RESULT, _user.gotMerchants);
			_user.storeService.getMerchants.send();*/
		}

        public function updateLabels(userName:String, fullName:String, email:String):void {
            this.email = email;
            this.userName = userName;
            this.name = fullName;
        }

        public function resync(userTransferObject:UserTransferObject):void {
            this.accountAdmin = userTransferObject.accountAdmin;
            //this.dataSourceCreator = userTransferObject.dataSourceCreator;
            //this.insightCreator = userTransferObject.insightCreator;
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
		
		public function getAccountType():int {
			return accountType;
		}

        public function setAccountType(type:int):void {
            this.accountType = type;
        }
		
		public function getEmail():String {
			return email;
		}
		
		public function getName():String {
			return name;
		}

		
		static public function getInstance():User {
			return _user;
		}

        static public function getSharedObject():SharedObject {
            return sharedObject;
        }

        static public function getCredentials(dataSourceID:int):Credentials {
            var idString:String = dataSourceID.toString();
            if(getSharedObject() != null && getSharedObject().data[idString] != null) {
                var c:Credentials = new Credentials();
                c.userName = User.getSharedObject().data[idString].username;
                c.password = User.getSharedObject().data[idString].password;
                c.encrypted = true;
                return c;
            }
            return null;
        }

        static public function saveCredentials(dataSourceID:int, c:Credentials):void {
            var idString:String = dataSourceID.toString();
            if (getSharedObject() != null) {
                getSharedObject().data[idString] = new Object();
                getSharedObject().data[idString].username = c.userName;
                getSharedObject().data[idString].password = c.password;
                getSharedObject().flush();
            }
        }

        }
}