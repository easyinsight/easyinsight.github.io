alter table data_feed add current_version integer(11);

update data_feed set current_version = 1;

drop table if exists analysis_step;
create table analysis_step (
  analysis_step_id bigint(20) auto_increment not null,
  correlation_dimension_id bigint(20) not null,
  start_date_dimension_id bigint(20) not null,
  end_date_dimension_id bigint(20) not null,
  analysis_item_id bigint(20) not null,
  primary key(analysis_step_id),
  key correlation_dimension_id (correlation_dimension_id),
  key start_date_dimension_id (start_date_dimension_id),
  key end_date_dimension_id (end_date_dimension_id),
  key analysis_item_id (analysis_item_id),
  constraint analysis_step_ibfk1 foreign key (correlation_dimension_id) references analysis_item (analysis_item_id) on delete cascade,
  constraint analysis_step_ibfk2 foreign key (start_date_dimension_id) references analysis_item (analysis_item_id) on delete cascade,
  constraint analysis_step_ibfk3 foreign key (end_date_dimension_id) references analysis_item (analysis_item_id) on delete cascade,
  constraint analysis_step_ibfk4 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade
);