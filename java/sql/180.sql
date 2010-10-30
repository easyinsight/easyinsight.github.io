drop table if exists whole_foods_source;
create table whole_foods_source (
  whole_foods_source_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  username varchar(255) default null,
  wf_password varchar(255) default null,
  initialized tinyint(4) not null,
  primary key(whole_foods_source_id),
  constraint whole_foods_source_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);