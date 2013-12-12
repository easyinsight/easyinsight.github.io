alter table analysis_item add unqualified_display_name varchar(255) default null;
update analysis_item set unqualified_display_name = display_name;


alter table account add look_and_feel_customized tinyint(4) not null default 1;

alter table analysis_item_filter drop exclude_report_fields;

alter table filter add flexible_date_filter tinyint(4) not null default 0;
alter table filter add default_date_filter tinyint(4) not null default 0;

alter table account add field_model tinyint(4) not null default 0;
alter table account add exchange_author tinyint(4) not null default 0;


create index cache_to_rebuild_idx on cache_to_rebuild (cache_time);