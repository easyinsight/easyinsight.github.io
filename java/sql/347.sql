alter table smartsheet add rebuild_fields tinyint(4) not null default 0;

truncate excel_export;
alter table excel_export add url_key varchar(255) default null;

truncate PNG_EXPORT;
alter table png_export add url_key varchar(255) default null;

alter table FEDERATED_DATA_SOURCE add classifier_name varchar(255) default null;