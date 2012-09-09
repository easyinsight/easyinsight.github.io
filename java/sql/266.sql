drop table if exists DATABASE_DATA_SOURCE;

create table DATABASE_DATA_SOURCE (
  database_data_source_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  refresh_key varchar(255) not null,
  refresh_url text,
  primary key(database_data_source_id),
  constraint database_data_source_ibfk1 foreign key (data_source_id) references data_feed(data_feed_id) ON DELETE CASCADE
);
