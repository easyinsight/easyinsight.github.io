alter table quickbase_composite_source add support_index tinyint(4) not null default 1;
alter table quickbase_composite_source add qb_username varchar(255) default null;
alter table quickbase_composite_source add qb_password varchar(255) default null;
alter table quickbase_composite_source add preserve_credentials tinyint(4) not null default 0;

alter table constant_contact add brief_mode tinyint(4) not null default 0;

alter table dashboard_stack add consolidate_header_elements tinyint(4) not null default 0;