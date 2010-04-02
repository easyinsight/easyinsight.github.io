alter table analysis add url_key varchar(100) default null;
create index analysis_url_idx on analysis (url_key);
alter table community_group add url_key varchar(100) default null;
create index community_group_url_idx on community_group (url_key);
alter table goal_tree add url_key varchar(100) default null;
create index goal_tree_url_idx on goal_tree (url_key);
alter table report_package add url_key varchar(100) default null;
create index report_package_url_idx on report_package (url_key);

alter table data_feed drop feed_key;
alter table data_feed drop genre;
alter table data_feed drop analysis_id;

drop table if exists report_based_data_source;
create table report_based_data_source (
  report_based_data_source_id bigint(20) auto_increment not null,
  data_feed_id bigint(20) not null,
  report_id bigint(20) not null,
  primary key(report_based_data_source_id),
  constraint report_based_data_source_ibfk1 foreign key (data_feed_id) references data_feed (data_feed_id) on delete cascade,
  constraint report_based_data_source_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade
) ENGINE=InnoDB;
