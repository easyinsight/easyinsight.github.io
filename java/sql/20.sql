drop table if exists limits_metadata;
create table limits_metadata (
    limits_metadata_id integer auto_increment not null,
    top_items tinyint,
    number_items integer,
    primary key(limits_metadata_id)
);

drop table if exists list_limits_metadata;
create table list_limits_metadata (
    list_limits_metadata_id integer auto_increment not null,
    limits_metadata_id bigint,
    analysis_item_id integer,
    primary key(list_limits_metadata_id)
);

alter table list_definition add list_limits_metadata_id bigint;
alter table list_definition add show_row_numbers tinyint default 0;

drop table if exists community_group;
create table community_group (
    community_group_id integer auto_increment not null,
    name varchar(255),
    publicly_visible tinyint,
    publicly_joinable tinyint,
    description text,
    tag_cloud_id integer,
    primary key(community_group_id)
);

drop table if exists group_to_tag;
create table group_to_tag (
    group_to_tag_id integer auto_increment not null,
    group_id integer not null,
    analysis_tags_id integer,
    FOREIGN KEY (group_id) REFERENCES community_group (community_group_id) ON DELETE CASCADE,
    FOREIGN KEY (analysis_tags_id) REFERENCES analysis_tags (analysis_tags_id) ON DELETE CASCADE,
    primary key(group_to_tag_id)
);

drop table if exists group_to_user_join;
create table group_to_user_join (
    group_to_user_join_id integer auto_increment not null,
    group_id integer,
    user_id integer,
    binding_type integer,
    primary key (group_to_user_join_id)
);

drop table if exists feed_group_policy;
create table feed_group_policy (
    feed_group_policy_id integer auto_increment not null,
    feed_id integer not null,
    FOREIGN KEY (feed_id) REFERENCES data_feed (data_feed_id) ON DELETE CASCADE,
    primary key(feed_group_policy_id)
);

drop table if exists feed_group_policy_group;
create table feed_group_policy_group (
    feed_group_policy_group_id integer auto_increment not null,
    feed_group_policy_id integer not null,
    group_id integer not null,
    FOREIGN KEY (group_id) REFERENCES community_group (community_group_id) ON DELETE CASCADE,
    FOREIGN KEY (feed_group_policy_id) REFERENCES feed_group_policy (feed_group_policy_id) ON DELETE CASCADE,
    primary key(feed_group_policy_group_id)
);