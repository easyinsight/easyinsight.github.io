drop table if exists gnip;
create table gnip (
  gnip_id bigint auto_increment not null,
  data_feed_id bigint(11) not null,
  publisher_id varchar(255) not null,
  publisher_scope varchar(255) not null,
  filter_id varchar(255) not null, 
  primary key(gnip_id),
  key data_feed_id (data_feed_id),
  constraint gnip_ibfk1 foreign key (data_feed_id) references data_feed (data_feed_id) on delete cascade
);