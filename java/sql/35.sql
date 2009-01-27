drop table if exists analysis_to_hierarchy_join;
create table analysis_to_hierarchy_join (
    analysis_to_hierarchy_join bigint(20) auto_increment not null,
    analysis_id bigint(20),
    analysis_item_id bigint(20),
    primary key(analysis_to_hierarchy_join)
);