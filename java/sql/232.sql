alter table multi_date_filter add end_date_property varchar(255) default null;

alter table dashboard add ytd_date varchar(255) not null default 'December';
alter table dashboard add ytd_override tinyint(4) not null default 0;

alter table analysis add marmotscript text default null;
alter table data_feed add marmotscript text default null;

alter table filter add marmotscript text default null;

alter table dashboard add marmotscript text default null;