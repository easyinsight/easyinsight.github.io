alter table report_package_to_report add report_order integer not null default 0;
alter table group_to_report_package add role integer not null default 0;