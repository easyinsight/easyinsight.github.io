drop table if exists highrise;
create table highrise (
  highrise_id bigint(20) auto_increment not null,
  feed_id bigint(20) not null,
  url varchar(255) not null,
  primary key(highrise_id),
  constraint highrise_ibfk1 foreign key (feed_id) references data_feed (data_feed_id) on delete cascade
);