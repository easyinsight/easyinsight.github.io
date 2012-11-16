alter table drill_through add show_drillthrough_filters tinyint(4) not null default 0;
alter table drill_through add filter_row_groupings tinyint(4) not null default 0;

alter table account add last_boundary_date datetime default null;