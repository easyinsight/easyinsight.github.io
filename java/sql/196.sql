alter table highrise add join_tasks_to_contacts tinyint(4) not null default 0;
alter table basecamp add include_todo_comments tinyint(4) not null default 0;

drop table if exists quickbase_composite_source;
create table quickbase_composite_source (
  quickbase_composite_source_id bigint(20) auto_increment not null,
  session_ticket varchar(255) default null,
  application_token varchar(255) default null,
  data_source_id bigint(20) not null,
  host_name varchar(255) default null,
  primary key (quickbase_composite_source_id),
  constraint quickbase_composite_source_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists quickbase_data_source;
create table quickbase_data_source (
  quickbase_data_source_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  database_id varchar(255) default null,
  primary key(quickbase_data_source_id),
  constraint quickbase_data_source_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

alter table report_property add enabled tinyint(4) not null default 1;

alter table application_skin add account_id bigint(20) default null;
alter table application_skin add constraint application_skin_ibfk2 foreign key (account_id) references account (account_id) on delete cascade;

alter table application_skin add global_skin tinyint(4) not null default 0;

alter table user_image add public_visibility tinyint(4) not null default 0;