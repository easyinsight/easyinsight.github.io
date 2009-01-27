drop table if exists hierarchy_level;
create table hierarchy_level (
    hierarchy_level_id bigint(20) auto_increment not null,
    level integer(11) not null,
    parent_item_id bigint(20) default null,
    analysis_item_id bigint(20) not null,
    primary key(hierarchy_level_id),
    key (analysis_item_id),
    constraint hierarchy_level_ibfk2 foreign key (analysis_item_id) references analysis_item (analysis_item_id)
);

drop table if exists analysis_hierarchy_item;
create table analysis_hierarchy_item (
    analysis_hierarchy_item_id bigint(20) auto_increment not null,
    analysis_item_id bigint(20) not null,
    hierarchy_level_id bigint(20) not null,
    primary key(analysis_hierarchy_item_id),
    key (analysis_item_id),
    constraint analysis_hierarchy_item_ibfk2 foreign key (analysis_item_id) references analysis_item (analysis_item_id)
);