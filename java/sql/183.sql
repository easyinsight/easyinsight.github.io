drop table if exists report_boolean_property;
create table report_boolean_property (
  report_boolean_property_id bigint(20) auto_increment not null,
  report_property_id bigint(20) not null,
  property_value tinyint(4) not null,
  primary key (report_boolean_property_id),
  constraint report_boolean_property_ibfk1 foreign key (report_property_id) references report_property (report_property_id) on delete cascade
);

drop table if exists derived_analysis_date_dimension;
create table derived_analysis_date_dimension (
  derived_analysis_date_dimension_id bigint(20) auto_increment not null,
  derivation_code text not null,
  analysis_item_id bigint(20) not null,
  primary key (derived_analysis_date_dimension_id),
  constraint derived_analysis_date_dimension_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade  
);