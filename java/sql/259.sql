drop table if exists batchbook2;
create table batchbook2 (
  batchbook2_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  url varchar(255) default null,
  auth_token varchar(255) default null,
  primary key (batchbook2_id),
  constraint batchbook2_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

alter table json_source add json_path varchar(255) default null;