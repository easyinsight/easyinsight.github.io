alter table analysis_item_to_filter drop foreign key analysis_item_to_filter_ibfk1;
alter table analysis_item_to_filter drop foreign key analysis_item_to_filter_ibfk2;
alter table analysis_item_to_filter add constraint analysis_item_to_filter_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade;
alter table analysis_item_to_filter add constraint analysis_item_to_filter_ibfk2 foreign key (filter_id) references filter (filter_id) on delete cascade; 