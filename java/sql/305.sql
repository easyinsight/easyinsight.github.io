alter table date_range_filter add start_date_enabled tinyint(4) not null default 1;
alter table date_range_filter add end_date_enabled tinyint(4) not null default 1;
alter table date_range_filter add slider_range tinyint(4) not null default 1;

alter table join_override add source_cardinality tinyint(4) not null default 0;
alter table join_override add target_cardinality tinyint(4) not null default 0;
alter table join_override add force_outer_join tinyint(4) not null default 0;


