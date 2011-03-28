drop table if exists harvest;
create table harvest (
  harvest_id bigint auto_increment not null,
  data_feed_id bigint(11) not null,
  url VARCHAR(255) not null,
  username varchar(255) not null,
  password varchar(255) not null,
  primary key (harvest_id),
  key (data_feed_id),
  constraint harvest_ibfk1 foreign key (data_feed_id) references data_feed(data_feed_id) on delete cascade
);