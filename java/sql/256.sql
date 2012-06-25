update data_feed set last_refresh_start = null where feed_type = 135;

drop table if exists json_source;
create table json_source (
  json_source_id bigint(20) auto_increment not null,
  url varchar(255) default null,
  data_source_id bigint(20) not null,
  primary key (json_source_id),
  constraint json_source_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);