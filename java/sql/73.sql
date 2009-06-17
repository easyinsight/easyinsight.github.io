alter table analysis_hierarchy_item change hierarchy_level_id hierarchy_level_id bigint(20) default null;

alter table filter add intrinsic tinyint(4) not null default 0;