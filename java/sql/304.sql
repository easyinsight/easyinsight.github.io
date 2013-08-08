alter table UPLOAD_BYTES add upload_successful tinyint(4) not null default 0;

alter table account add new_pricing_model_invoice tinyint(4) not null default 0;

alter table system_settings add max_filter_values integer not null default 2500;
alter table system_settings add max_operations integer not null default 10000000;
alter table basecamp add full_projects tinyint(4) not null default 0;

alter table account_tag add tag_index integer not null default 0;