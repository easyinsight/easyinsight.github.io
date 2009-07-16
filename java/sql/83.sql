alter table analysis_hierarchy_item_to_hierarchy_level drop foreign key analysis_hierarchy_item_to_hierarchy_level_ibfk1;
alter table analysis_hierarchy_item_to_hierarchy_level add constraint analysis_hierarchy_item_to_hierarchy_level_ibfk1 FOREIGN KEY (hierarchy_level_id) REFERENCES hierarchy_level (hierarchy_level_id) ON DELETE CASCADE;
alter table analysis_hierarchy_item_to_hierarchy_level drop foreign key analysis_hierarchy_item_to_hierarchy_level_ibfk2;
alter table analysis_hierarchy_item_to_hierarchy_level add constraint analysis_hierarchy_item_to_hierarchy_level_ibfk2 FOREIGN KEY (analysis_item_id) REFERENCES analysis_item (analysis_item_id) ON DELETE CASCADE;

alter table hierarchy_level drop foreign key hierarchy_level_ibfk2;
alter table hierarchy_level add constraint hierarchy_level_ibfk2 foreign key (analysis_item_id) REFERENCES analysis_item (analysis_item_id) ON DELETE CASCADE;

alter table analysis_hierarchy_item drop foreign key analysis_hierarchy_item_ibfk2;
alter table analysis_hierarchy_item add constraint analysis_hierarchy_item_ibfk2 foreign key (analysis_item_id) REFERENCES analysis_item (analysis_item_id) ON DELETE CASCADE;