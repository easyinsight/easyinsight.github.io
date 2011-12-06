# alter table user add created_on datetime default null;
# alter table user add updated_on datetime default null;

# alter table scorecard add scorecard_visible tinyint(4) not null default 1;
# alter table data_feed add archived tinyint(4) not null default 0;
# alter table analysis add archived tinyint(4) not null default 0;
# alter table dashboard add archived tinyint(4) not null default 0;
# alter table scorecard add archived tinyint(4) not null default 0;

alter table data_feed add concrete_fields_editable tinyint(4) not null default 0;

drop table if exists ytd_report;
create table ytd_report (
  ytd_report_id bigint(20) auto_increment not null,
  report_state_id bigint(20) not null,
  primary key (ytd_report_id),
  constraint ytd_report_ibfk1 foreign key (report_state_id) references report_state (report_state_id) on delete cascade
);

drop table if exists ytd_field_extension;
create table ytd_field_extension (
  trend_report_field_extension_id bigint(20) auto_increment not null,
  report_field_extension_id bigint(20) not null,
  benchmark_id bigint(20) default null,
  underline tinyint(4) not null default 0,
  primary key (trend_report_field_extension_id),
  constraint ytd_field_extension_ibfk1 foreign key (benchmark_id) references analysis_item (analysis_item_id) on delete cascade,
  constraint ytd_field_extension_ibfk2 foreign key (report_field_extension_id) references report_field_extension (report_field_extension_id) on delete cascade
);

drop table if exists compare_time_report;
create table compare_time_report (
  compare_time_report_id bigint(20) auto_increment not null,
  report_state_id bigint(20) not null,
  primary key (compare_time_report_id),
  constraint compare_time_report_ibfk1 foreign key (report_state_id) references report_state (report_state_id) on delete cascade
);