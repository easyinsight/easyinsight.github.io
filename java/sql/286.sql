alter table filter add field_choice_filter_label varchar(255) default null;
alter table filter add section integer not null default 0;
alter table analysis_item add field_type integer not null default 0;
alter table trend_report_field_extension add trend_comparison_id bigint(20) default null;
alter table trend_report_field_extension add constraint trend_report_field_extension_ibfk5 FOREIGN KEY (trend_comparison_id) REFERENCES analysis_item (analysis_item_id) ON DELETE SET NULL;