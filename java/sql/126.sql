alter table kpi add day_window integer not null default 2;
alter table kpi add threshold double not null default 0;

alter table kpi add date_dimension_id bigint(20) default null;
alter table kpi add constraint kpi_ibfk3 foreign key (date_dimension_id) references analysis_item (analysis_item_id) on delete set null;

alter table kpi_value add directional tinyint(4) not null default 1;