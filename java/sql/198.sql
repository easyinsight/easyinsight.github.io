drop table if exists data_source_problem;
create table data_source_problem (
  data_source_problem_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  problem_text varchar(255) not null,
  primary key (data_source_problem_id),
  constraint data_source_problem_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);