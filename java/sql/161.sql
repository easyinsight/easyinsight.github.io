alter table lookup_table add url_key varchar(255) default null;

drop table if exists derived_analysis_dimension;
create table derived_analysis_dimension (
  derived_analysis_dimension_id bigint(20) auto_increment not null,
  derivation_code text not null,
  analysis_item_id bigint(20) not null,
  primary key (derived_analysis_dimension_id),
  constraint derived_analysis_dimension_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade  
);