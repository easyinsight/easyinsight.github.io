drop table if exists redirect_data_source;
create table redirect_data_source (
  redirect_data_source_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  delegate_data_source_id bigint(20) not null,
  primary key(redirect_data_source_id),
  constraint redirect_data_source_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade,
  constraint redirect_data_source_ibfk2 foreign key (delegate_data_source_id) references data_feed (data_feed_id) on delete cascade
);