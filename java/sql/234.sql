drop table if exists report_field_extension;
create table report_field_extension (
  report_field_extension_id bigint(20) auto_increment not null,
  primary key (report_field_extension_id)
);

drop table if exists trend_report_field_extension;
create table trend_report_field_extension (
  trend_report_field_extension_id bigint(20) auto_increment not null,
  report_field_extension_id bigint(20) not null,
  trend_date_id bigint(20) default null,
  icon_image varchar(255) default null,
  high_low integer not null default 0,
  primary key (trend_report_field_extension_id),
  constraint trend_report_field_extension_ibfk1 foreign key (trend_date_id) references analysis_item (analysis_item_id) on delete cascade,
  constraint trend_report_field_extension_ibfk2 foreign key (report_field_extension_id) references report_field_extension (report_field_extension_id) on delete cascade
);

alter table analysis_item add report_field_extension_id bigint(20) default null;
alter table analysis_item add constraint analysis_item_ibfk9 foreign key (report_field_extension_id) references report_field_extension (report_field_extension_id) on delete set null;

drop table if exists trend_report;
create table trend_report (
  trend_report_id bigint(20) auto_increment not null,
  report_state_id bigint(20) not null,
  filter_name varchar(255) not null,
  day_window varchar(255) not null,
  primary key(trend_report_id),
  constraint trend_report_ibfk1 foreign key (report_state_id) references report_state (report_state_id) on delete cascade
) ENGINE=InnoDB;

drop table if exists trend_grid_report;
create table trend_grid_report (
  trend_grid_report_id bigint(20) auto_increment not null,
  report_state_id bigint(20) not null,
  sort_index integer not null default -1,
  sort_direction tinyint(4) not null default 0,
  filter_name varchar(255) not null,
  day_window varchar(255) not null,
  primary key(trend_grid_report_id),
  constraint trend_grid_report_ibfk1 foreign key (report_state_id) references report_state (report_state_id) on delete cascade
) ENGINE=InnoDB;