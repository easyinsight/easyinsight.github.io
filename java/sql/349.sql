drop table if exists netsuite;
create table netsuite (
  netsuite_id bigint(20) auto_increment not null,
  account_id varchar(255) default null,
  netsuite_username varchar(255) default null,
  netsuite_password varchar(255) default null,
  query text default null,
  rebuild_fields tinyint(4) not null default 0,
  connection_timeout integer not null default 0,
  data_source_id bigint(20) not null,
  primary key (netsuite_id),
  constraint netsuite_ibkf1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

alter table account add special_storage_caching varchar(255) default null;