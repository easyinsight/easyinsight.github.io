alter table data_feed add api_key varchar(100);
alter table data_feed add unchecked_api_enabled tinyint(1) default 0;
alter table data_feed add unchecked_api_basic_auth tinyint(1) default 0;
alter table data_feed add validated_api_enabled tinyint(1) default 0;
alter table data_feed add validated_api_basic_auth tinyint(1) default 0;
alter table data_feed add inherit_account_api_settings tinyint(1) default 1;

alter table account add unchecked_api_enabled tinyint(1) default 1;
alter table account add validated_api_enabled tinyint(1) default 1;
alter table account add unchecked_api_allowed tinyint(1) default 1;
alter table account add validated_api_allowed tinyint(1) default 1;
alter table account add dynamic_api_enabled tinyint(1) default 1;
alter table account add basic_auth_allowed tinyint(1) default 1;
