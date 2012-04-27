drop table if exists custom_rest_live;
create table custom_rest_live (
  custom_rest_live_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  endpoint_url varchar(255) default null,
  cr_username varchar(255) default null,
  cr_password varchar(255) default null,
  primary key (custom_rest_live_id),
  constraint custom_rest_live_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

create index dashboard_idx1 on dashboard (url_key);

update data_feed set version = 8 where feed_type = 10;
update data_feed set version = 7 where feed_type = 25;