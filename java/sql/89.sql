drop table if exists last_value_filter;
create table last_value_filter(
  last_value_filter_id bigint(20) auto_increment not null,
  filter_id bigint(20) not null,
  primary key(last_value_filter_id),
  key filter_id (filter_id),
  constraint last_value_filter_ibfk1 foreign key (filter_id) references filter (filter_id) on delete cascade
);