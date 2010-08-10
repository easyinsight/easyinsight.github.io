drop table if exists analysis_text;
create table analysis_text (
  analysis_text_id bigint(20) auto_increment not null,
  analysis_item_id bigint(20) not null,
  html tinyint(4) not null,
  primary key (analysis_text_id),
  constraint analysis_text_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id) 
);