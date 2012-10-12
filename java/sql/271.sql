alter table analysis_measure add currency_grouping bigint(20) default null;
alter table analysis_item add from_field_id bigint(20) default null;

drop table if exists treasure_data_table;
create table treasure_data_table(
  treasure_data_table_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  api_key varchar(255) default null,
  query text default null,
  database_id varchar(255) default null,
  primary key (treasure_data_table_id),
  constraint treasure_data_table_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists drillthrough_save;
create table drillthrough_save (
  drillthrough_save_id bigint(20) auto_increment not null,
  url_key varchar(255) not null,
  save_time datetime not null,
  report_id bigint(20) default null,
  dashboard_id bigint(20) default null,
  primary key (drillthrough_save_id),
  constraint drillthrough_save_ibfk1 foreign key (report_id) references analysis (analysis_id) on delete cascade,
  constraint drillthrough_save_ibfk2 foreign key (dashboard_id) references dashboard (dashboard_id) on delete cascade
);

drop table if exists drillthrough_report_save_filter;
create table drillthrough_report_save_filter (
  drillthrough_report_save_filter_id bigint(20) auto_increment not null,
  filter_id bigint(20) not null,
  drillthrough_save_id bigint(20) not null,
  primary key (drillthrough_report_save_filter_id),
  constraint drillthrough_report_save_filter_ibfk1 foreign key (filter_id) references filter (filter_id) on delete cascade,
  constraint drillthrough_report_save_filter_ibfk2 foreign key (drillthrough_save_id) references drillthrough_save (drillthrough_save_id) on delete cascade
);