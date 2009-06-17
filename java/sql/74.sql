alter table data_feed add visible tinyint(4) not null default 1;
alter table data_feed add parent_source_id bigint(20) default null;