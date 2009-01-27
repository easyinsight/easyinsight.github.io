drop table if exists goal_tree;
create table goal_tree (
    goal_tree_id bigint(20) auto_increment not null,
    name varchar(200) not null,
    description text default null,
    root_node bigint(20) not null,
    primary key(goal_tree_id)
);

drop table if exists goal_tree_node;
create table goal_tree_node (
    goal_tree_node_id bigint(20) auto_increment not null,
    parent_goal_tree_node_id bigint(20) default null,
    feed_id bigint(20),
    goal_value integer(11),
    analysis_measure_id bigint(20),
    filter_id bigint(20),
    name varchar(200) not null,
    description text default null,
    primary key(goal_tree_node_id),
    key parent_goal_tree_node_id (parent_goal_tree_node_id),
    key feed_id (feed_id),
    key analysis_measure_id (analysis_measure_id),
    key filter_id (filter_id),
    constraint goal_tree_node_ibfk1 foreign key (parent_goal_tree_node_id) references goal_tree_node (goal_tree_node_id) on delete cascade,
    constraint goal_tree_node_ibfk2 foreign key (feed_id) references data_feed (data_feed_id) on delete cascade,
    constraint goal_tree_node_ibfk3 foreign key (analysis_measure_id) references analysis_item (analysis_item_id) on delete cascade,
    constraint goal_tree_node_ibfk4 foreign key (filter_id) references filter (filter_id) on delete cascade
);

drop table if exists goal_tree_node_tag;
create table goal_tree_node_tag (
    goal_tree_node_tag_id bigint(20) auto_increment not null,
    goal_tree_node_id bigint(20) not null,
    tag varchar(255) not null,
    primary key(goal_tree_node_tag_id),
    key goal_tree_node_id (goal_tree_node_id),
    constraint goal_tree_node_tag_ibfk1 foreign key (goal_tree_node_id) references goal_tree_node (goal_tree_node_id) on delete cascade
);

drop table if exists goal_tree_node_to_insight;
create table goal_tree_node_to_insight (
    goal_tree_node_to_insight_id bigint(20) auto_increment not null,
    goal_tree_node_id bigint(20) not null,
    insight_id bigint(20) not null,
    primary key(goal_tree_node_to_insight_id),
    key goal_tree_node_id (goal_tree_node_id),
    key insight_id (insight_id),
    constraint goal_tree_node_to_insight_ibfk1 foreign key (goal_tree_node_id) references goal_tree_node (goal_tree_node_id) on delete cascade,
    constraint goal_tree_node_to_insight_ibfk2 foreign key (insight_id) references analysis (analysis_id) on delete cascade
);

drop table if exists goal_tree_node_to_feed;
create table goal_tree_node_to_feed (
    goal_tree_node_to_feed_id bigint(20) auto_increment not null,
    goal_tree_node_id bigint(20) not null,
    feed_id bigint(20) not null,
    primary key(goal_tree_node_to_feed_id),
    key goal_tree_node_id (goal_tree_node_id),
    key feed_id (feed_id),
    constraint goal_tree_node_to_feed_ibfk1 foreign key (goal_tree_node_id) references goal_tree_node (goal_tree_node_id) on delete cascade,
    constraint goal_tree_node_to_feed_ibfk2 foreign key (feed_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists goal_tree_node_to_solution;
create table goal_tree_node_to_solution (
    goal_tree_node_to_solution_id bigint(20) auto_increment not null,
    goal_tree_node_id bigint(20) not null,
    solution_id bigint(20) not null,
    primary key(goal_tree_node_to_solution_id),
    key goal_tree_node_id (goal_tree_node_id),
    key solution_id (solution_id),
    constraint goal_tree_node_to_solution_ibfk1 foreign key (goal_tree_node_id) references goal_tree_node (goal_tree_node_id) on delete cascade,
    constraint goal_tree_node_to_solution_ibfk2 foreign key (solution_id) references analysis (analysis_id) on delete cascade
);

drop table if exists solution_to_goal_tree;
create table solution_to_goal_tree (
    solution_to_goal_tree_id bigint(20) auto_increment not null,
    solution_id bigint(20) not null,
    goal_tree_id bigint(20) not null,
    primary key(solution_to_goal_tree_id),
    key solution_id (solution_id),
    key goal_tree_id (goal_tree_id),
    constraint solution_to_goal_tree_ibfk1 foreign key (solution_id) references solution (solution_id) on delete cascade,
    constraint solution_to_goal_tree_ibfk2 foreign key (goal_tree_id) references goal_tree (goal_tree_id) on delete cascade
);

drop table if exists goal_history;
create table goal_history (
    goal_history_id bigint(20) auto_increment not null,
    goal_tree_node_id bigint(20) not null,
    evaluation_date date not null,
    evaluation_result double not null,
    primary key(goal_history_id),
    key goal_tree_node_id (goal_tree_node_id),
    constraint goal_history_ibfk1 foreign key (goal_tree_node_id) references goal_tree_node (goal_tree_node_id) on delete cascade
);

drop table if exists user_to_goal_tree;
create table user_to_goal_tree (
    user_to_goal_tree_id bigint(20) auto_increment not null,
    goal_tree_id bigint(20) not null,
    user_id bigint(20) not null,
    user_role integer(11) not null,
    primary key(user_to_goal_tree_id),
    key user_id (user_id),
    key goal_tree_id (goal_tree_id),
    constraint user_to_goal_tree_ibfk1 foreign key (user_id) references user (user_id) on delete cascade,
    constraint user_to_goal_tree_ibfk2 foreign key (goal_tree_id) references goal_tree (goal_tree_id) on delete cascade
);