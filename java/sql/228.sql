alter table join_override add data_source_id bigint(20) default null;
alter table join_override add constraint join_override_ibfk5 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade;

drop table if exists multi_date_filter;
create table multi_date_filter (
  multi_date_filter_id bigint(20) auto_increment not null,
  filter_id bigint(20) not null,
  primary key (multi_date_filter_id),
  constraint multi_date_filter_ibfk1 foreign key (filter_id) references filter (filter_id) on delete cascade
);

drop table if exists date_level_wrapper;
create table date_level_wrapper (
  date_level_wrapper_id bigint(20) auto_increment not null,
  date_level integer not null,
  primary key (date_level_wrapper_id)
);

drop table if exists multi_date_filter_to_date_level_wrapper;
create table multi_date_filter_to_date_level_wrapper (
  multi_date_filter_to_date_level_wrapper_id bigint(20) auto_increment not null,
  filter_id bigint(20) not null,
  date_level_wrapper_id bigint(20) not null,
  primary key (multi_date_filter_to_date_level_wrapper_id),
  constraint multi_date_filter_to_date_level_wrapper_ibfk1 foreign key (filter_id) references filter (filter_id) on delete cascade,
  constraint multi_date_filter_to_date_level_wrapper_ibfk2 foreign key (date_level_wrapper_id) references date_level_wrapper (date_level_wrapper_id) on delete cascade
);

alter table quickbase_data_source add support_index tinyint(4) not null default 1;

alter table account add heat_map_enabled tinyint(4) not null default 1;
alter table account add ilog_enabled tinyint(4) not null default 1;

alter table composite_connection add left_join tinyint(4) not null default 0;
alter table composite_connection add right_join tinyint(4) not null default 0;
alter table join_override add left_join tinyint(4) not null default 0;
alter table join_override add right_join tinyint(4) not null default 0;