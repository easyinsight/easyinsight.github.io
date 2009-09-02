drop table if exists analysis_item_to_link;
create table analysis_item_to_link (
  analysis_item_to_link_id bigint(20) auto_increment not null,
  analysis_item_id bigint(20) not null,
  link_id bigint(20) not null,
  primary key(analysis_item_to_link_id),
  constraint analysis_item_to_link_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id),
  constraint analysis_item_to_link_ibfk2 foreign key (link_id) references link (link_id)
);

alter table analysis_dimension add summary tinyint(4) not null default 0;