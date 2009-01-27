drop table if exists group_to_insight;
create table group_to_insight (
    group_to_insight_id bigint auto_increment not null,
    insight_id bigint not null,
    group_id bigint not null,
    role integer not null,
    primary key(group_to_insight_id)
);

drop table if exists insight_policy_users;
create table insight_policy_users (
    insight_policy_users_id bigint auto_increment not null,
    user_id bigint not null,
    insight_id bigint not null,
    role integer not null,
    primary key(insight_policy_users_id)
);

alter table analysis add marketplace_visible tinyint default 0;
alter table analysis add publicly_visible tinyint default 0;
alter table analysis add feed_visibility tinyint default 0;