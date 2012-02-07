alter table analysis_item add sort_item_id bigint(20) default null;
alter table analysis_item add constraint analysis_item_ibfk10 foreign key (sort_item_id) references analysis_item (analysis_item_id) on delete cascade;

alter table text_report_field_extension add sortable tinyint(4) not null default 1;

