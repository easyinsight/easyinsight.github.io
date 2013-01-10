# alter table filter add parent_filters varchar(255) default null;

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

# alter table ytd_field_extension add date_field_id bigint(20) default null;
# alter table ytd_field_extension add constraint ytd_field_extension_ibfk3 foreign key (date_field_id) references analysis_item (analysis_item_id) on delete set null;

# alter table quickbase_composite_source add rebuild_fields tinyint(4) not null default 0;