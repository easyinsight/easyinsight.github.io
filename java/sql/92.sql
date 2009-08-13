alter table account drop unchecked_api_allowed;
alter table account drop unchecked_api_enabled;
alter table account drop validated_api_allowed;
alter table account drop validated_api_enabled;
alter table account drop dynamic_api_enabled;
alter table account add api_enabled tinyint(4) not null default 1;

alter table solution add footer_text text default null;
alter table solution add logo_link varchar(255) default null;