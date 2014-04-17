

alter table user add user_currency tinyint(4) not null default 0;
alter table user add user_locale varchar(20) not null default '0';
alter table user add user_date_format tinyint(4) not null default 6;

