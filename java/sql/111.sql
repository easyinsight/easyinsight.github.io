drop table if exists custom_data_source;
create table custom_data_source (
  custom_data_source_id bigint(20) auto_increment not null,
  data_feed_id bigint(20) not null,
  wsdl_url varchar(255) not null,
  primary key(custom_data_source_id),
  constraint custom_data_source_ibfk1 foreign key (data_feed_id) references data_feed (data_feed_id) on delete cascade
);

alter table png_export change user_id user_id bigint(20) default null;