drop table if exists basecamp;
create table basecamp (
  basecamp_id bigint auto_increment not null,
  data_feed_id bigint(11) not null,
  url varchar(255) not null,
  primary key(basecamp_id),
  key data_feed_id (data_feed_id),
  constraint basecamp_ibfk1 foreign key (data_feed_id) references data_feed (data_feed_id) on delete cascade
);