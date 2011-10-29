drop table if exists kashoo;
create table kashoo (
  kashoo_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  primary key (kashoo_id),
  constraint kashoo_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);