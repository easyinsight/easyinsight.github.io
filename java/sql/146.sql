alter table analysis_dimension drop hierarchy_id;
alter table analysis_dimension drop hierarchy_level_id;

alter table analysis_hierarchy_item add constraint analysis_hierarchy_item_ibfk1 foreign key (hierarchy_level_id) references hierarchy_level (hierarchy_level_id) on delete set null;
alter table analysis_step drop key correlation_dimension_id;
alter table analysis_step add constraint analysis_step_ibfk1 foreign key (correlation_dimension_id) references analysis_item (analysis_item_id) on delete set null;
alter table analysis_step drop key start_date_dimension_id;
alter table analysis_step add constraint analysis_step_ibfk2 foreign key (start_date_dimension_id) references analysis_item (analysis_item_id) on delete set null;
alter table analysis_step drop key end_date_dimension_id;
alter table analysis_step add constraint analysis_step_ibfk3 foreign key (end_date_dimension_id) references analysis_item (analysis_item_id) on delete set null;
alter table analysis_step drop key analysis_item_id;
alter table analysis_step add constraint analysis_step_ibfk4 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade;

alter table upload_policy_users add constraint upload_policy_users_ibfk1 foreign key (feed_id) references data_feed (data_feed_id) on delete cascade;