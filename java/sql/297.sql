

alter table dashboard_element add html_width integer not null default 0;

 drop table if exists chart_report_field_extension;
 create table chart_report_field_extension (
  chart_report_field_extension_id bigint(20) auto_increment not null,
  report_field_extension_id bigint(20) not null,
  goal_field bigint(20) default null,
  primary key (chart_report_field_extension_id),
  constraint chart_report_field_extension_ibfk1 foreign key (report_field_extension_id) references report_field_extension (report_field_extension_id) on delete cascade,
  constraint chart_report_field_extension_ibfk2 foreign key (goal_field) references analysis_item (analysis_item_id) on delete set null
 );

 alter table pattern_filter add auto_wild_card tinyint(4) not null default 0;

create table revised_action_log (
   revised_action_log_id bigint(20) auto_increment not null,
   data_source_id bigint(20) default null,
   dashboard_id bigint(20) default null,
   report_id bigint(20) default null,
   action_date datetime not null,
   action_type integer not null,
   general_action_type integer not null,
   user_id bigint(20) not null,
   primary key (revised_action_log_id),
   constraint revised_action_log_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade,
   constraint revised_action_log_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade,
   constraint revised_action_log_ibfk3 foreign key (dashboard_id) references dashboard (dashboard_id) on delete cascade,
   constraint revised_action_log_ibfk4 foreign key (user_id) references user (user_id) on delete cascade
);

create index revised_action_log_idx1 on revised_action_log (user_id, action_date);

alter table range_filter add show_slider tinyint(4) not null default 1;

alter table quickbase_composite_source add inline_fields tinyint(4) not null default 0;