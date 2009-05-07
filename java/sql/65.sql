alter table treemap_report add color_scheme integer not null default 1;

alter table chart_report add elevation_angle float not null default 0;
alter table chart_report add rotation_angle float not null default 0;