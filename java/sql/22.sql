drop table if exists solution;
create table solution (
    solution_id bigint auto_increment not null,
    name varchar(100),
    description text,
    archive blob,
    primary key(solution_id)
);

drop table if exists solution_to_feed;
create table solution_to_feed (
    solution_to_feed_id bigint auto_increment not null,
    feed_id bigint not null,
    solution_id bigint not null,
    primary key(solution_to_feed_id)
);

drop table if exists user_to_solution;
create table user_to_solution (
    user_solution_id bigint auto_increment not null,
    user_id bigint,
    solution_id bigint,
    user_role integer,
    primary key(user_solution_id)
);

drop table if exists group_changes;
create table group_changes (
    group_changes_id bigint auto_increment not null,
    change_date datetime not null,
    message varchar(255) not null,
    primary key(group_changes_id)
);

drop table if exists comment;
create table comment (
    comment_id bigint auto_increment not null,
    parent_comment_id bigint,
    user_id bigint not null,
    comment_date datetime not null,
    subject varchar(100) not null,
    message text not null,
    primary key(comment_id)
);

drop table if exists insight_comment;
create table insight_comment (
    insight_comment_id bigint auto_increment not null,
    comment_id bigint not null,
    insight_id bigint not null,
    primary key(insight_comment_id)
);