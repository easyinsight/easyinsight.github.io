alter table news_entry add tags varchar(255) default null;

drop table if exists mysql_database_connection;
create table mysql_database_connection (
  mysql_database_connection_id bigint(20) auto_increment not null,
  host_name varchar(255) default null,
  server_port integer default null,
  database_name varchar(255) default null,
  database_username varchar(255) default null,
  database_password varchar(255) default null,
  rebuild_fields tinyint(4) not null default 0,
  query_string text default null,
  data_source_id bigint(20) not null,
  primary key (mysql_database_connection_id),
  constraint mysql_database_connection_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

alter table account add current_size bigint(20) not null default 0;