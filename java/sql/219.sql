alter table dashboard_element add header_image_id bigint(20) default null;
alter table dashboard_element add constraint dashboard_stack_ibfk2 foreign key (header_image_id) references user_image (user_image_id) on delete set null;
alter table dashboard_element add label varchar(255) default null;
alter table dashboard_element add header_background_color integer not null default 16777215;
alter table dashboard_element add header_background_alpha float not null default 0;
alter table dashboard_element add padding_left integer not null default 5;
alter table dashboard_element add padding_right integer not null default 5;
alter table dashboard_element add padding_bottom integer not null default 5;
alter table dashboard_element add padding_top integer not null default 5;
alter table dashboard_element add filter_border_style varchar(25) not null default 'solid';
alter table dashboard_element add filter_border_color integer not null default 13421772;
alter table dashboard_element add filter_background_color integer not null default 16777215;
alter table dashboard_element add filter_background_alpha integer not null default 0;

alter table dashboard add public_visible tinyint(4) not null default 0;
alter table dashboard add padding_left integer not null default 5;
alter table dashboard add padding_right integer not null default 5;
alter table dashboard add filter_border_style varchar(25) not null default 5;
alter table dashboard add filter_border_color integer not null default 13421772;
alter table dashboard add filter_background_color integer not null default 16777215;
alter table dashboard add filter_background_alpha integer not null default 0;

alter table user add fixed_dashboard_id bigint(20) default null;
alter table user add constraint user_ibfk8 foreign key (fixed_dashboard_id) references dashboard (dashboard_id) on delete set null;

update report_numeric_property left join report_property on report_numeric_property.report_property_id = report_property.report_property_id set property_value = 0xF7F7F7 where report_property.property_name = 'rowColor1';
update report_numeric_property left join report_property on report_numeric_property.report_property_id = report_property.report_property_id set property_value = 0xFFFFFF where report_property.property_name = 'rowColor2';
update report_numeric_property left join report_property on report_numeric_property.report_property_id = report_property.report_property_id set property_value = 0xFFFFFF where report_property.property_name = 'headerColor1';
update report_numeric_property left join report_property on report_numeric_property.report_property_id = report_property.report_property_id set property_value = 0xEFEFEF where report_property.property_name = 'headerColor2';

alter table scorecard_to_kpi add kpi_index integer not null default 0;