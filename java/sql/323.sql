create index dashboard_idx3 on dashboard (temporary_dashboard, account_visible);
create index dashboard_idx4 on dashboard (temporary_dashboard);

alter table drill_through add pass_through_field_id bigint(20) default null;

alter table zendesk add hack_method tinyint(4) not null default 0;

alter table data_feed add field_cleanup_enabled tinyint(4) not null default 0;
alter table data_feed add field_lookup_enabled tinyint(4) not null default 0;