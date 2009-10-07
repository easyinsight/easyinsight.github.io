drop table if exists analysis_item_to_filter;
create table analysis_item_to_filter (
  analysis_item_to_filter_id bigint(20) auto_increment not null,
  analysis_item_id bigint(20) not null,
  filter_id bigint(20) not null,
  primary key (analysis_item_to_filter_id),
  constraint analysis_item_to_filter_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id),
  constraint analysis_item_to_filter_ibfk2 foreign key (filter_id) references filter (filter_id)
);

alter table analysis_item add high_is_good tinyint(4) not null default 1; 