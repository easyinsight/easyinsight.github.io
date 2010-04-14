drop table analysis_to_hierarchy_join;
drop table chart_field;
drop table crosstab_measure_column;
drop table crosstab_field;
alter table analysis_dimension drop hierarchy_id;
alter table analysis_dimension drop hierarchy_level_id;
alter table report_state drop key analysis_id;
alter table report_state drop analysis_id;
alter table report_structure drop key analysis_id;
alter table report_structure drop key analysis_item_id;
alter table report_structure add constraint report_structure_ibfk1 foreign key (analysis_id) references analysis (analysis_id) on delete cascade;
alter table report_structure add constraint report_structure_ibfk2 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade;

