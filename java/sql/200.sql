drop table if exists user_sales_email_schedule;
create table user_sales_email_schedule (
  user_sales_email_schedule_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  target_number integer not null,
  send_date datetime not null,
  primary key (user_sales_email_schedule_id),
  constraint user_sales_email_schedule_ibfk1 foreign key (user_id) references user (user_id) on delete cascade
);

drop table if exists campaign_monitor;
create table campaign_monitor (
  campaign_monitor_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  api_key varchar(255) default null,
  url varchar(255) default null,
  primary key (campaign_monitor_id),
  constraint campaign_monitor_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists cleardb;
create table cleardb (
  cleardb_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  api_key varchar(255) default null,
  app_id varchar(255) default null,
  primary key (cleardb_id),
  constraint cleardb_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists cleardb_child;
create table cleardb_child (
  cleardb_child_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  cleardb_table varchar(255) default null,
  primary key (cleardb_child_id),
  constraint cleardb_child_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);