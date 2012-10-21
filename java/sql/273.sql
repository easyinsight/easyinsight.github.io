# alter table list_limits_metadata add show_remainder_as_other tinyint(4) not null default 0;
# alter table limits_metadata add show_remainder_as_other tinyint(4) not null default 0;

drop table if exists youtrack_source;
create table youtrack_source (
  youtrack_source_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  cookie varchar(255) default null,
  url varchar(255) default null,
  primary key (youtrack_source_id),
  constraint youtrack_source_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists custom_rolling_interval;
create table custom_rolling_interval (
  custom_rolling_interval_id bigint(20) auto_increment not null,
  filter_label varchar(255) default null,
  interval_number integer not null,
  start_date_script text default null,
  start_date_defined tinyint(4) not null default 1,
  end_date_script text default null,
  end_date_defined tinyint(4) not null default 1,
  primary key (custom_rolling_interval_id)
);

drop table if exists filter_to_custom_rolling_interval;
create table filter_to_custom_rolling_interval (
  filter_to_custom_rolling_interval_id bigint(20) auto_increment not null,
  filter_id bigint(20) not null,
  custom_rolling_interval_id bigint(20) not null,
  primary key(filter_to_custom_rolling_interval_id),
  constraint filter_to_custom_rolling_interval_ibfk1 foreign key (filter_id) references filter (filter_id) on delete cascade,
  constraint filter_to_custom_rolling_interval_ibfk2 foreign key (custom_rolling_interval_id) references custom_rolling_interval (custom_rolling_interval_id) on delete cascade
);

alter table user add news_dismiss_date datetime default null;