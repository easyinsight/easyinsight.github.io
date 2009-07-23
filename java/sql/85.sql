drop table if exists goal_outcome;
create table goal_outcome (
  goal_outcome_id bigint(20) auto_increment not null,
  goal_tree_node_id bigint(20) not null,
  evaluation_date datetime not null,
  problem_evaluated tinyint(4) not null,
  start_value double default null,
  end_value double default null,
  outcome_value integer not null,
  direction integer not null,
  primary key(goal_outcome_id),
  key goal_tree_node_id (goal_tree_node_id),
  constraint goal_outcome_ibfk1 foreign key (goal_tree_node_id) references goal_tree_node (goal_tree_node_id) on delete cascade
);