drop table if exists twitter;

create table twitter (
  data_feed_id bigint(20) not null,
  search varchar(160) not null,
  key(data_feed_id),
  constraint twitter_ibfk1 foreign key (data_feed_id) references data_feed(data_feed_id) on delete cascade
);