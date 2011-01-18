drop table if exists batchbook;
create table batchbook (
  batchbook_id bigint(20) auto_increment not null,
  url varchar(255) default null,
  data_source_id bigint(20) not null,
  api_key varchar(255) default null,
  primary key (batchbook_id),
  constraint batchbook_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);