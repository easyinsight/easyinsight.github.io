drop table if exists insight_comment;
create table insight_comment (
    insight_comment_id bigint(20) auto_increment not null,
    insight_id bigint(20) not null,
    user_id bigint(20) not null,
    comment text not null,
    time_created datetime not null,
    time_updated datetime default null,
    primary key(insight_comment_id),
    key insight_id (insight_id),
    key user_id (user_id),
    constraint insight_comment_ibfk1 foreign key (insight_id) references analysis (analysis_id),
    constraint insight_comment_ibfk2 foreign key (user_id) references user (user_id)
);

drop table if exists data_source_comment;
create table data_source_comment (
    data_source_comment_id bigint(20) auto_increment not null,
    data_source_id bigint(20) not null,
    user_id bigint(20) not null,
    comment text not null,
    time_created datetime not null,
    time_updated datetime default null,
    primary key(data_source_comment_id),
    key data_source_id (data_source_id),
    key user_id (user_id),
    constraint data_source_comment_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id),
    constraint data_source_comment_ibfk2 foreign key (user_id) references user (user_id)
);

drop table if exists group_comment;
create table group_comment (
    group_comment_id bigint(20) auto_increment not null,
    group_id bigint(20) not null,
    user_id bigint(20) not null,
    comment text not null,
    time_created datetime not null,
    time_updated datetime default null,
    primary key(group_comment_id),
    key group_id (group_id),
    key user_id (user_id),
    constraint group_comment_ibfk1 foreign key (group_id) references community_group (community_group_id),
    constraint group_comment_ibfk2 foreign key (user_id) references user (user_id)
);

drop table if exists group_audit_message;
create table group_audit_message (
    group_audit_message_id bigint(20) auto_increment not null,
    group_id bigint(20) not null,
    user_id bigint(20) not null,
    comment text not null,
    audit_time datetime not null,
    primary key(group_audit_message_id),
    key group_id (group_id),
    key user_id (user_id),
    constraint group_audit_message_ibfk1 foreign key (group_id) references community_group (community_group_id),
    constraint group_audit_message_ibfk2 foreign key (user_id) references user (user_id)
);

drop table if exists insight_audit_message;
create table insight_audit_message (
    insight_audit_message_id bigint(20) auto_increment not null,
    insight_id bigint(20) not null,
    user_id bigint(20) not null,
    comment text not null,
    audit_time datetime not null,
    primary key(insight_audit_message_id),
    key insight_id (insight_id),
    key user_id (user_id),
    constraint insight_audit_message_ibfk1 foreign key (insight_id) references analysis (analysis_id),
    constraint insight_audit_message_ibfk2 foreign key (user_id) references user (user_id)
);

drop table if exists data_source_audit_message;
create table data_source_audit_message (
    data_source_audit_message_id bigint(20) auto_increment not null,
    data_source_id bigint(20) not null,
    user_id bigint(20) not null,
    comment text not null,
    audit_time datetime not null,
    primary key(data_source_audit_message_id),
    key data_source_id (data_source_id),
    key user_id (user_id),
    constraint data_source_audit_message_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id),
    constraint data_source_audit_message_ibfk2 foreign key (user_id) references user (user_id)
);