alter table analysis_item add original_display_name varchar(255) default null;

alter table filter add filter_name varchar(255) default null;
alter table filter add column_level_template tinyint(4) not null default 0;

drop table if exists named_filter_reference;
create table named_filter_reference (
  named_filter_reference_id bigint(20) auto_increment not null,
  filter_id bigint(20) not null,
  filter_name varchar(255) default null,
  primary key (named_filter_reference_id),
  constraint named_filter_reference_ibfk1 foreign key (filter_id) references filter (filter_id) on delete cascade
);

alter table scorecard add data_source_id bigint(20) default null;
alter table scorecard add constraint scorecard_ibfk3 FOREIGN KEY (data_source_id) REFERENCES data_feed (data_feed_id) ON DELETE CASCADE;

alter table quickbase_external_login add session_ticket varchar(255) default null;

alter table data_feed drop feed_views;
alter table data_feed drop feed_rating_average;
alter table data_feed drop feed_rating_count;
alter table data_feed drop validated_api_basic_auth;
alter table data_feed drop validated_api_enabled;
alter table data_feed drop adjust_dates;

drop table if exists dashboard_scorecard;
create table dashboard_scorecard (
  dashboard_scorecard_id bigint(20) auto_increment not null,
  scorecard_id bigint(20) not null,
  label_placement integer not null default 0,
  show_label tinyint(4) not null default 0,
  dashboard_element_id bigint(20) not null,
  primary key(dashboard_scorecard_id),
  constraint dashboard_scorecard_ibfk1 foreign key (scorecard_id) references scorecard (scorecard_id) on delete cascade,
  constraint dashboard_scorecard_ibfk2 foreign key (dashboard_element_id) references dashboard_element (dashboard_element_id) on delete cascade
);