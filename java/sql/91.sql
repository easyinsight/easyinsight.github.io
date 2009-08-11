drop table if exists pattern_filter;
create table pattern_filter (
  pattern_filter_id bigint(20) auto_increment not null,
  filter_id bigint(20) not null,
  case_sensitive tinyint(4) not null,
  regex tinyint(4) not null,
  pattern varchar(255) not null,
  primary key(pattern_filter_id),
  key filter_id (filter_id),
  constraint pattern_filter_ibfk1 foreign key (filter_id) references filter (filter_id) on delete cascade
);