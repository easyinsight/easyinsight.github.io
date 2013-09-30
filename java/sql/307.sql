alter table account add special_storage varchar(255) default null;



alter table account add hourly_refresh_enabled tinyint(4) not null default 0;

alter table scheduled_data_source_refresh add interval_type tinyint(4) not null default 0;
alter table scheduled_data_source_refresh add interval_units integer not null default 0;

