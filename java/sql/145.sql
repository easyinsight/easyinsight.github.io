drop table if exists linkedin_data_source;
create table linkedin_data_source (
  linkedin_data_source_id bigint(20) auto_increment not null,
  feed_id bigint(20) not null,
  token_key varchar(255) default null,
  token_secret_key varchar(255) default null,
  primary key(linkedin_data_source_id),
  constraint linkedin_data_source_ibfk1 foreign key (feed_id) references data_feed (data_feed_id) on delete cascade
);