# alter table chart_definition change column chart_type chart_type integer;
# alter table chart_definition add chart_family integer;
# alter table analysis_item add display_name varchar(255);

drop table if exists list_limits_metadata;
create table list_limits_metadata (
    list_limits_metadata_id integer auto_increment not null,
    analysis_item_id integer,
    top_items tinyint,
    number_items integer,
    primary key(list_limits_metadata_id)
);

drop table if exists chart_limits_metadata;
create table chart_limits_metadata (
    list_limits_metadata_id integer auto_increment not null,
    analysis_item_id integer,
    top_items tinyint,
    number_items integer,
    primary key(list_limits_metadata_id)
);

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

drop table if exists group_to_feed_join;
create table group_to_feed_join (
    group_to_feed_join_id integer auto_increment not null,
    group_id integer,
    feed_id integer,
    primary key (group_to_feed_join_id)
);

drop table if exists group_to_user_join;
create table group_to_user_join (
    group_to_user_join_id integer auto_increment not null,
    group_id integer,
    user_id integer,
    binding_type integer,
    primary key (group_to_user_join_id)
);

alter table data_feed add description text;
alter table data_feed add attribution varchar(255);
alter table data_feed add owner_name varchar(255);