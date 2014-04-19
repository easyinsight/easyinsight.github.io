alter table system_settings add header_image_id bigint(20) not null default 0;
alter table storage_database add database_dialect tinyint(4) not null default 1;
alter table storage_database add general_pool tinyint(4) not null default 1;