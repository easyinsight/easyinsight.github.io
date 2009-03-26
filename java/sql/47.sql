DROP TABLE IF EXISTS group_to_goal_tree_join;
CREATE TABLE group_to_goal_tree_join (
  group_to_goal_tree_join_id bigint(20) NOT NULL auto_increment,
  group_id bigint(20) default NULL,
  goal_tree_id bigint(20) default NULL,
  PRIMARY KEY  (group_to_goal_tree_join_id),
  KEY goal_tree_id (goal_tree_id),
  KEY group_id (group_id),
  CONSTRAINT group_to_goal_tree_join_ibfk_1 FOREIGN KEY (goal_tree_id) REFERENCES goal_tree (goal_tree_id) ON DELETE CASCADE,
  CONSTRAINT group_to_goal_tree_join_ibfk_2 FOREIGN KEY (group_id) REFERENCES community_group (community_group_id) ON DELETE CASCADE
);