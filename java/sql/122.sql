drop table if exists scorecard;
create table scorecard (
  scorecard_id bigint(20) auto_increment not null,
  scorecard_name varchar(255) not null,
  user_id bigint(20) default null,
  group_id bigint(20) default null,
  scorecard_order integer not null default 0,
  primary key(scorecard_id),
  constraint scorecard_ibfk1 foreign key (user_id) references user (user_id) on delete cascade,
  constraint scorecard_ibfk2 foreign key (group_id) references community_group (community_group_id) on delete cascade
)ENGINE=InnoDB;

drop table if exists kpi;
create table kpi (
  kpi_id bigint(20) auto_increment not null,
  data_feed_id bigint(20) not null,
  analysis_measure_id bigint(20) not null,
  kpi_name varchar(200) not null,
  connection_visible tinyint(4) not null default 0,
  description text default null,
  high_is_good tinyint(4) default null,
  goal_defined tinyint(4) not null default 0,
  goal_value double default null,
  icon_image varchar(255) default null,
  temporary tinyint(4) not null default 0,
  primary key(kpi_id),
  constraint kpi_ibfk1 foreign key (data_feed_id) references data_feed (data_feed_id) on delete cascade,
  constraint kpi_ibfk2 foreign key (analysis_measure_id) references analysis_item (analysis_item_id) on delete cascade
)ENGINE=InnoDB;

drop table if exists kpi_value;
create table kpi_value (
  kpi_value_id bigint(20) auto_increment not null,
  start_value double default null,
  end_value double default null,
  evaluation_date datetime default null,
  outcome_value integer not null,
  problem_evaluated tinyint(4) not null,
  direction integer not null,
  kpi_id bigint(20) not null,
  primary key(kpi_value_id),
  constraint kpi_value_ibfk1 foreign key (kpi_id) references kpi (kpi_id) on delete cascade
)ENGINE=InnoDB;

drop table if exists kpi_to_problem_filter;
create table kpi_to_problem_filter (
  kpi_to_problem_filter_id bigint(20) auto_increment not null,
  kpi_id bigint(20) not null,
  filter_id bigint(20) not null,
  primary key(kpi_to_problem_filter_id),
  constraint kpi_to_problem_filter_ibfk1 foreign key (kpi_id) references kpi (kpi_id) on delete cascade,
  constraint kpi_to_problem_filter_ibfk2 foreign key (filter_id) references filter (filter_id) on delete cascade
);

drop table if exists kpi_to_filter;
create table kpi_to_filter (
  kpi_to_filter_id bigint(20) auto_increment not null,
  kpi_id bigint(20) not null,
  filter_id bigint(20) not null,
  primary key (kpi_to_filter_id),
  constraint kpi_to_filter_ibfk1 foreign key (kpi_id) references kpi (kpi_id) on delete cascade,
  constraint kpi_to_filter_ibfk2 foreign key (filter_id) references filter (filter_id) on delete cascade
) ENGINE=INNODB;

drop table if exists scorecard_to_kpi;
create table scorecard_to_kpi (
  scorecard_to_kpi_id bigint(20) auto_increment not null,
  scorecard_id bigint(20) not null,
  kpi_id bigint(20) not null,
  primary key(scorecard_to_kpi_id),
  constraint scorecard_to_kpi_ibfk1 foreign key (scorecard_id) references scorecard (scorecard_id) on delete cascade,
  constraint scorecard_to_kpi_ibfk2 foreign key (kpi_id) references kpi (kpi_id) on delete cascade
)ENGINE=InnoDB;

alter table goal_tree_node drop foreign key goal_tree_node_ibfk2;
alter table goal_tree_node drop foreign key goal_tree_node_ibfk3;
alter table goal_tree_node drop foreign key goal_tree_node_ibfk4;
alter table goal_tree_node drop analysis_measure_id;
alter table goal_tree_node drop filter_id;
alter table goal_tree_node drop goal_defined;
alter table goal_tree_node drop goal_measure_description;
alter table goal_tree_node drop goal_milestone_id;
alter table goal_tree_node drop goal_value;
alter table goal_tree_node drop high_is_good;
alter table goal_tree_node drop feed_id;

alter table goal_tree_node add kpi_id bigint(20) default null;
alter table goal_tree_node add constraint goal_tree_node_ibfk2 foreign key (kpi_id) references kpi (kpi_id) on delete set null;

drop table goal_tree_node_to_feed;
drop table goal_tree_node_to_insight;
drop table goal_tree_node_to_solution;
drop table goal_to_filter;
drop table goal_to_problem_filter;
drop table goal_node_to_user;
drop table goal_outcome;
drop table goal_history;