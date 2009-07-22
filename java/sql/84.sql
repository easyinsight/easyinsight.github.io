drop table if exists goal_to_problem_filter;
create table goal_to_problem_filter (
  goal_to_problem_filter_id bigint(20) auto_increment not null,
  goal_tree_node_id bigint(20) not null,
  filter_id bigint(20) not null,
  primary key(goal_to_problem_filter_id),
  key goal_tree_node_id (goal_tree_node_id),
  key filter_id (filter_id),
  constraint goal_to_problem_filter_ibfk1 foreign key (goal_tree_node_id) references goal_tree_node (goal_tree_node_id) on delete cascade,
  constraint goal_to_problem_filter_ibfk2 foreign key (filter_id) references filter (filter_id) on delete cascade
);