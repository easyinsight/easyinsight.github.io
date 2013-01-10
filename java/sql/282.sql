alter table account add core_small_biz_connections integer not null default 0;
alter table account add addon_small_biz_connections integer not null default 0;

alter table account add core_designers integer not null default 0;

alter table account add core_storage integer not null default 0;
alter table account add addon_storage_units integer not null default 0;

alter table account add addon_designers integer not null default 0;

alter table account add addon_quickbase_connections integer not null default 0;
alter table account add addon_salesforce_connections integer not null default 0;
alter table account add unlimited_quickbase_connections integer not null default 0;

alter table account add enterprise_addon_cost integer not null default 0;

