

alter table json_source add pagination_method tinyint(4) not null default 1;
alter table json_source add per_page_limit integer(10) not null default 0;
alter table json_source add first_page_number integer(10) not null default 0;
alter table json_source add page_field varchar(100) default null;
alter table json_source add limit_field varchar(100) default null;
alter table json_source add offset_field varchar(100) default null;