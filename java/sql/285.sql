alter table analysis_item add kpi tinyint(4) not null default 0;
create index analysis_item_idx_kpi on analysis_item (kpi);
alter table data_feed add kpi_source tinyint(4) not null default 0;
create index data_feed_idx_kpi on data_feed (kpi_source);

drop table if exists sql_server_database_connection;
create table sql_server_database_connection (
  sql_server_database_connection_id bigint(20) auto_increment not null,
  host_name varchar(255) default null,
  server_port integer default null,
  database_name varchar(255) default null,
  database_username varchar(255) default null,
  database_password varchar(255) default null,
  rebuild_fields tinyint(4) not null default 0,
  query_string text default null,
  data_source_id bigint(20) not null,
  primary key (sql_server_database_connection_id),
  constraint sql_server_database_connection_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists oracle_database_connection;
create table oracle_database_connection (
  oracle_database_connection_id bigint(20) auto_increment not null,
  host_name varchar(255) default null,
  server_port integer default null,
  sid varchar(255) default null,
  database_username varchar(255) default null,
  database_password varchar(255) default null,
  rebuild_fields tinyint(4) not null default 0,
  query_string text default null,
  data_source_id bigint(20) not null,
  primary key (oracle_database_connection_id),
  constraint oracle_database_connection_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);