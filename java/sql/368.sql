alter table schedule add use_account_timezone tinyint(4) not null default 0;

alter table drillthrough_save add phantomjs_zoneid varchar(75) default null;
alter table image_selenium_trigger add phantomjs_zoneid varchar(75) default null;