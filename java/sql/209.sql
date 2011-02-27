alter table data_feed add last_refresh_start datetime default null;

alter table filter add show_on_report_view tinyint(4) not null default 1;