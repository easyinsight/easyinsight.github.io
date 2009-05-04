alter table goal_node_to_user drop foreign key goal_node_to_user_ibfk1;
alter table goal_node_to_user add constraint goal_node_to_user_ibfk1 foreign key (goal_tree_node_id) references goal_tree_node (goal_tree_node_id) on delete cascade;
alter table goal_node_to_user drop foreign key goal_node_to_user_ibfk2;
alter table goal_node_to_user add constraint goal_node_to_user_ibfk2 foreign key (user_id) references user (user_id) on delete cascade;