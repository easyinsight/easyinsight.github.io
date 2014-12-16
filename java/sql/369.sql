create table netsuite_composite (
  netsuite_composite_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  netsuite_username varchar(255) default null,
  netsuite_password varchar(255) default null,
  netsuite_account_id varchar(255) default null,
  netsuite_role varchar (255) default null,
  primary key (netsuite_composite_id),
  constraint netsuite_composite_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

create table netsuite_composite_tables (
  netsuite_composite_tables_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  included_table_name varchar(255) not null,
  primary key (netsuite_composite_tables_id),
  constraint netsuite_composite_tables_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

create table netsuite_table (
  netsuite_table_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  netsuite_table varchar(255) default null,
  primary key (netsuite_table_id),
  constraint netsuite_table_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists async_report_request;
create table async_report_request (
  async_report_request_id bigint(20) auto_increment not null,
  claimed tinyint(4) not null,
  report blob not null,
  user_id bigint(20) not null,
  metadata blob not null,
  request_type tinyint(4) not null,
  request_created datetime not null,
  primary key (async_report_request_id),
  index async_report_request_idx1 (claimed)
);

alter table account add async_requests tinyint(4) not null default 0;