drop table if exists analysis_measure_grouping;
create table analysis_measure_grouping (
  analysis_measure_grouping_id bigint(20) auto_increment not null,
  analysis_item_id bigint(20) not null,
  primary key(analysis_measure_grouping_id),
  constraint analysis_measure_grouping_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id)  
);

drop table if exists analysis_measure_grouping_join;
create table analysis_measure_grouping_join (
  analysis_measure_grouping_join_id bigint(20) auto_increment not null,
  analysis_item_id bigint(20) not null,
  measure_id bigint(20) not null,
  primary key(analysis_measure_grouping_join_id),
  constraint analysis_measure_grouping_join_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id),
  constraint analysis_measure_grouping_join_ibfk2 foreign key (measure_id) references analysis_item (analysis_item_id)
);