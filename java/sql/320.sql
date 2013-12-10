alter table report_delivery add configuration_id bigint(20) default null;




alter table ytd_field_extension add section varchar(255) default null;

alter table vertical_list_field_extension add section varchar(255) default null;

alter table multi_analysis_item_filter add exclude_report_fields tinyint(4) not null default 0;
alter table analysis_item_filter add exclude_report_fields tinyint(4) not null default 0;

alter table delivery_to_report add configuration_id bigint(20) default null;
alter table delivery_to_report add constraint delivery_to_report_ibfk4 foreign key (configuration_id) references saved_configuration (saved_configuration_id) on delete set null;

alter table filter add customizable tinyint(4) not null default 0;

alter table account drop addon_salesforce_connections;
