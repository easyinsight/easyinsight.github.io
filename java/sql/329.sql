alter table text_report_field_extension add force_to_summary tinyint(4) not null default 0;

create table dashboard_element_to_filter_set (
  dashboard_element_to_filter_set_id bigint(20) auto_increment not null,
  dashboard_element_id bigint(20) not null,
  filter_set_id bigint(20) not null,
  primary key (dashboard_element_to_filter_set_id),
  constraint dashboard_element_to_filter_set_ibfk1 foreign key (dashboard_element_id) references dashboard_element (dashboard_element_id),
  constraint dashboard_element_to_filter_set_ibfk2 foreign key (filter_set_id) references filter_set (filter_set_id)
);



alter table analysis add data_source_field_report tinyint(4) not null default 0;

drop table if exists smartsheet;
create table smartsheet (
  smartsheet_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  access_token varchar(255) default null,
  refresh_token varchar(255) default null,
  sheet_id varchar(255) default null,
  primary key (smartsheet_id),
  constraint smartsheet_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

alter table analysis_item drop high_is_good;
alter table analysis_item drop label_column;
alter table analysis_item drop marmotscript;
alter table analysis_item add formatting_type tinyint(4) not null default 1;
update analysis_item, formatting_configuration set analysis_item.formatting_type = formatting_configuration.formatting_type WHERE analysis_item.formatting_configuration_id = formatting_configuration.formatting_configuration_id;
alter table analysis_item drop formatting_configuration_id;

alter table drill_through add pass_through_field_id bigint(20) default null;