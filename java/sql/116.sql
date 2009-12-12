drop table if exists report_package;
create table report_package (
  report_package_id bigint(20) auto_increment not null,
  package_name varchar(255) not null,
  connection_visible tinyint(4) not null default 0,
  publicly_visible tinyint(4) not null default 0,
  marketplace_visible tinyint(4) not null default 0,
  data_source_id bigint(20) default null,
  date_created datetime not null,
  author_name varchar(255) not null,
  description varchar(255) default null,
  limited_source tinyint(4) not null default 0,
  temporary_package tinyint(4) not null default 0,
  primary key(report_package_id),
  constraint report_package_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists report_package_to_report;
create table report_package_to_report (
  report_package_to_report_id bigint(20) auto_increment not null,
  report_package_id bigint(20) not null,
  report_id bigint(20) not null,
  primary key(report_package_to_report_id),
  constraint report_package_to_report_ibfk1 foreign key (report_package_id) references report_package (report_package_id) on delete cascade,
  constraint report_package_to_report_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade
);

drop table if exists user_to_report_package;
create table user_to_report_package (
  user_to_report_package_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  role integer not null,
  report_package_id bigint(20) not null,
  primary key(user_to_report_package_id),
  constraint user_to_report_package_ibfk1 foreign key (user_id) references user (user_id) on delete cascade,
  constraint user_to_report_package_ibfk2 foreign key (report_package_id) references report_package (report_package_id) on delete cascade
);

drop table if exists group_to_report_package;
create table group_to_report_package (
  group_to_report_package_id bigint(20) auto_increment not null,
  group_id bigint(20) not null,
  report_package_id bigint(20) not null,
  primary key(group_to_report_package_id),
  constraint group_to_report_package_ibfk1 foreign key (group_id) references community_group (community_group_id) on delete cascade,
  constraint group_to_report_package_ibfk2 foreign key (report_package_id) references report_package (report_package_id) on delete cascade
);

drop table if exists user_package_rating;
create table user_package_rating (
  user_package_rating_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  report_package_id bigint(20) not null,
  rating integer,
  primary key(user_package_rating_id),
  constraint user_package_rating_ibfk1 foreign key (user_id) references user (user_id) on delete cascade,
  constraint user_package_rating_ibfk2 foreign key (report_package_id) references report_package (report_package_id) on delete cascade
);