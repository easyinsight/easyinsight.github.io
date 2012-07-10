drop table if exists file_based_data_source;
create table file_based_data_source (
  file_based_data_source_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  auth_username varchar(255) default null,
  auth_password varchar(255) default null,
  endpoint varchar(255) default null,
  http_method integer not null default 1,
  primary key (file_based_data_source_id),
  constraint file_based_data_source_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

alter table json_source add auth_username varchar(255) default null;
alter table json_source add auth_password varchar(255) default null;
alter table json_source add http_method integer not null default 1;

alter table derived_analysis_dimension add apply_before_aggregation tinyint(4) not null default 1;