alter table goal_tree_node drop foreign key goal_tree_node_ibfk2;
alter table goal_tree_node drop foreign key goal_tree_node_ibfk3;
alter table goal_tree_node drop foreign key goal_tree_node_ibfk4;
alter table goal_tree_node add constraint goal_tree_node_ibfk2 FOREIGN KEY (feed_id) REFERENCES data_feed (data_feed_id) ON DELETE SET NULL;
alter table goal_tree_node add constraint goal_tree_node_ibfk3 FOREIGN KEY (analysis_measure_id) REFERENCES analysis_item (analysis_item_id) ON DELETE SET NULL;
alter table goal_tree_node add constraint goal_tree_node_ibfk4 FOREIGN KEY (filter_id) REFERENCES filter (filter_id) ON DELETE SET NULL;