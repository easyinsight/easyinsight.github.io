

drop table if exists join_override_to_source_fields;
create table join_override_to_source_fields(
  join_override_to_source_fields_id bigint(20) auto_increment not null,
  join_override_id bigint(20) not null,
  analysis_item_id bigint(20) not null,
  primary key (join_override_to_source_fields_id),
  constraint join_override_to_source_fields_ibfk1 foreign key (join_override_id) references join_override (join_override_id) on delete cascade,
  constraint join_override_to_source_fields_ibfk2 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade
);

drop table if exists join_override_to_target_fields;
create table join_override_to_target_fields(
  join_override_to_target_fields_id bigint(20) auto_increment not null,
  join_override_id bigint(20) not null,
  analysis_item_id bigint(20) not null,
  primary key (join_override_to_target_fields_id),
  constraint join_override_to_target_fields_ibfk1 foreign key (join_override_id) references join_override (join_override_id) on delete cascade,
  constraint join_override_to_target_fields_ibfk2 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade
);

alter table join_override add join_type integer not null default 1;

 create table data_source_to_addon_report (
   data_source_to_addon_report_id bigint(20) auto_increment not null,
   data_source_id bigint(20) not null,
   report_id bigint(20) not null,
   primary key (data_source_to_addon_report_id),
   constraint data_source_to_addon_report_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade,
   constraint data_source_to_addon_report_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade
 );

 alter table benchmark add user_id bigint(20) default null;
 alter table benchmark add report_id bigint(20) default null;
 alter table benchmark add dashboard_id bigint(20) default null;
 alter table benchmark add data_source_id bigint(20) default null;
 alter table benchmark add html tinyint(4) default null;

alter table composite_connection change source_feed_node_id source_feed_node_id bigint(20) default null;
alter table composite_connection change target_feed_node_id target_feed_node_id bigint(20) default null;