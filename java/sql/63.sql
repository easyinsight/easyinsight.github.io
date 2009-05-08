drop table if exists password_storage;
drop table if exists session_id_storage;

create table password_storage (
  password_id bigint auto_increment not null,
  data_feed_id bigint(11) not null,
  username varchar(255) not null,
  password varchar(255) not null,
  primary key(password_id),
  key data_feed_id (data_feed_id),
  constraint password_ibfk1 foreign key (data_feed_id) references data_feed (data_feed_id) on delete cascade
);

create table session_id_storage (
  session_id_id bigint auto_increment not null,
  data_feed_id bigint(11) not null,
  session_id varchar(255) not null,
  primary key(session_id_id),
  key data_feed_id(data_feed_id),
  constraint session_id_ibfk1 foreign key (data_feed_id) references data_feed (data_feed_id) on delete cascade
); 