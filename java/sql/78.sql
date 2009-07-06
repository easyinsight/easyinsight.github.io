alter table goal_tree add goal_tree_icon varchar(255) default null;
alter table goal_tree add default_milestone_id bigint(20) default null;
alter table goal_tree add constraint goal_tree_ibfk3 foreign key (default_milestone_id) references milestone (milestone_id);