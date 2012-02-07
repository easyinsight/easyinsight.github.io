alter table account add account_reactivation_date datetime default null;
alter table analysis add auto_setup_delivery tinyint(4) not null default 0;

alter table drill_through add marmotscript text default null;