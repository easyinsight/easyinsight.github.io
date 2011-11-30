alter table analysis add folder integer not null default 1;
alter table dashboard add folder integer not null default 1;
alter table scorecard add folder integer not null default 1;

alter table analysis_measure add min_precision integer not null default 0;

update report_numeric_property inner join report_property on report_numeric_property.report_property_id = report_property.report_property_id set report_numeric_property.property_value = 16711680 where report_property.property_name = 'color1';
update report_numeric_property inner join report_property on report_numeric_property.report_property_id = report_property.report_property_id set report_numeric_property.property_value = 16776960 where report_property.property_name = 'color2';
update report_numeric_property inner join report_property on report_numeric_property.report_property_id = report_property.report_property_id set report_numeric_property.property_value = 47889 where report_property.property_name = 'color3';

alter table drill_through add dashboard_id bigint(20) default null;
alter table drill_through add constraint drill_through_ibfk3 foreign key (dashboard_id) references dashboard (dashboard_id) on delete cascade;
alter table drill_through change report_id report_id bigint(20) default null;

alter table link add default_link tinyint(4) not null default 0;