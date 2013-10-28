create table saved_configuration (
  saved_configuration_id bigint(20) auto_increment not null,
  dashboard_state_id bigint(20) not null,
  configuration_name varchar(255) not null,
  primary key (saved_configuration_id)
);

drop table if exists db_connection_cache;
create table db_connection_cache (
  db_connection_cache_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  call_data_id varchar(255) not null,
  status integer not null,
  primary key (db_connection_cache_id),
  constraint db_connection_cache_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists refresh_connection_cache;
create table refresh_connection_cache (
  refresh_connection_cache_id bigint(20) auto_increment not null,
  result_object blob default null,
  message text default null,
  call_data_id varchar(255) not null,
  status integer not null,
  primary key (refresh_connection_cache_id),
  unique index refresh_connection_cache_idx (call_data_id)
);