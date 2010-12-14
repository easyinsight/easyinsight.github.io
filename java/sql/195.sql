drop table if exists google_analytics;
create table google_analytics (
  google_analytics_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  token_key varchar(255) default null,
  token_secret varchar(255) default null,
  primary key (google_analytics_id),
  constraint google_analytics_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

alter table google_feed add token_key varchar(255) default null;
alter table google_feed add token_secret varchar(255) default null;

drop table if exists google_docs_token;
create table google_docs_token (
  google_docs_token_id bigint(20) auto_increment not null,
  token_key varchar(255) default null,
  token_secret varchar(255) default null,
  user_id bigint(20) not null,
  primary key (google_docs_token_id),
  constraint google_docs_token_ibfk1 foreign key (user_id) references user (user_id) on delete cascade
);

