alter table analysis add solution_visible tinyint(4) not null default 0;

drop table if exists user_report_rating;
create table user_report_rating (
  user_report_rating_id bigint(20) auto_increment not null,
  report_id bigint(20) not null,
  user_id bigint(20) not null,
  rating integer not null,
  primary key(user_report_rating_id),
  constraint user_report_rating_ibfk1 foreign key (report_id) references analysis (analysis_id) on delete cascade,
  constraint user_report_rating_ibfk2 foreign key (user_id) references user (user_id) on delete cascade
);

drop table if exists user_data_source_rating;
create table user_data_source_rating (
  user_data_source_rating_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  data_source_id bigint(20) not null,
  rating integer not null,
  primary key (user_data_source_rating_id),
  constraint user_data_source_rating_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade,
  constraint user_data_source_rating_ibfk2 foreign key (user_id) references user (user_id) on delete cascade
);