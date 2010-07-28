alter table range_filter add current_low_value double not null default 0;
alter table range_filter add current_high_value double not null default 0;

alter table range_filter add current_low_value_defined tinyint(4) not null default 0;
alter table range_filter add current_high_value_defined tinyint(4) not null default 0;

alter table data_feed add account_visible tinyint(4) not null default 0;
alter table analysis add account_visible tinyint(4) not null default 0;

