alter table analysis_tags add use_count integer default 0;

create table tag_cloud_to_tag (
    tag_cloud_to_tag_id integer auto_increment not null,
    tag_cloud_id integer not null,
    tag_id integer not null,
    primary key(analysis_to_tag_id)
);