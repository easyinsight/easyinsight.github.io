alter table report_delivery add send_if_no_data tinyint(4) not null default 1;
alter table delivery_to_report add send_if_no_data tinyint(4) not null default 1;