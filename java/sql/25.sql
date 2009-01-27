drop table if exists upload_policy_users;
create table upload_policy_users (
    upload_policy_users_id bigint auto_increment not null,
    feed_id bigint not null,
    user_id bigint not null,
    role integer not null,
    primary key(upload_policy_users_id)
);

drop table if exists upload_policy_groups;
create table upload_policy_groups (
    upload_policy_groups_id bigint auto_increment not null,
    feed_id bigint not null,
    group_id bigint not null,
    role integer not null,
    primary key(upload_policy_groups_id)
);

ALTER TABLE DATA_FEED ADD PUBLICLY_VISIBLE tinyint;
ALTER TABLE DATA_FEED ADD MARKETPLACE_VISIBLE tinyint;