drop table if exists goal_tree_to_group;
create table goal_tree_to_group (
  goal_tree_to_group_id bigint(11) auto_increment not null,
  goal_tree_id bigint(11) not null,
  role int(8) not null,
  group_id bigint(11) not null,
  primary key(goal_tree_to_group_id),
  key goal_tree_id (goal_tree_id),
  key group_id (group_id),
  constraint goal_tree_to_group_ibfk1 foreign key (goal_tree_id) references goal_tree (goal_tree_id),
  constraint goal_tree_to_group_ibfk2 foreign key (group_id) references community_group (community_group_id)
);

drop table if exists goal_node_to_user;
create table goal_node_to_user (
  goal_node_to_user_id bigint(11) auto_increment not null,
  goal_tree_node_id bigint(11) not null,
  user_id bigint(11) not null,
  primary key(goal_node_to_user_id),
  key goal_tree_node_id (goal_tree_node_id),
  key user_id (user_id),
  constraint goal_node_to_user_ibfk1 foreign key (goal_tree_node_id) references goal_tree_node (goal_tree_node_id),
  constraint goal_node_to_user_ibfk2 foreign key (user_id) references user (user_id)
);