drop table if exists combined_vertical_list;
create table combined_vertical_list (
  combined_vertical_list_id bigint(20) auto_increment not null,
  report_state_id bigint(20) not null,
  primary key(combined_vertical_list_id),
  constraint combined_vertical_list_ibfk1 foreign key (report_state_id) references report_state (report_state_id) on delete cascade
);

drop table if exists combined_vertical_list_to_report;
create table combined_vertical_list_to_report (
  combined_vertical_list_to_report_id bigint(20) auto_increment not null,
  parent_report_id bigint(20) not null,
  child_report_id bigint(20) not null,
  primary key (combined_vertical_list_to_report_id),
  constraint combined_vertical_list_to_report_ibfk1 foreign key (parent_report_id) references report_state (report_state_id) on delete cascade,
  constraint combined_vertical_list_to_report_ibfk2 foreign key (child_report_id) references analysis (analysis_id) on delete cascade
);

alter table analysis_date  add output_date_format varchar(255) default null;
alter table analysis_measure add underline tinyint(4) not null default 0;
alter table analysis_measure add fp_precision integer not null default 2;

drop table if exists flat_date_filter;
create table flat_date_filter (
  flat_date_filter_id bigint(20) auto_increment not null,
  filter_id bigint(20) not null,
  date_level integer not null,
  selected_value integer not null,
  primary key (flat_date_filter_id),
  constraint flat_date_filter_ibkf1 foreign key (filter_id) references filter (filter_id) on delete cascade
);

alter table filter add toggle_enabled tinyint(4) not null default 0;
alter table value_based_filter add exclude_empty tinyint(4) not null default 0;
alter table value_based_filter add all_option tinyint(4) not null default 0;
alter table analysis_measure add min_precision integer not null default 0;

drop table if exists reaggregate_analysis_measure;
create table reaggregate_analysis_measure (
  reaggregate_analysis_measure_id bigint(20) auto_increment not null,
  wrapped_measure_id bigint(20) not null,
  aggregate_item_id bigint(20) not null,
  analysis_item_id bigint(20) not null,
  primary key (reaggregate_analysis_measure_id),
  constraint reaggregate_analysis_measure_ibfk1 foreign key (aggregate_item_id) references analysis_item (analysis_item_id) on delete cascade,
  constraint reaggregate_analysis_measure_ibfk2 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade,
  constraint reaggregate_analysis_measure_ibfk3 foreign key (wrapped_measure_id) references analysis_item (analysis_item_id) on delete cascade
);