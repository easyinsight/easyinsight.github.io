drop table if exists report_structure_x_axis;
drop table if exists report_structure_y_axis;
drop table if exists report_structure_z_axis;

drop table if exists report_structure;
create table report_structure (
  report_structure_id bigint(11) auto_increment not null,
  structure_key varchar(20) not null,
  analysis_id bigint(11) not null,
  analysis_item_id bigint(11) not null,
  primary key(report_structure_id),
  key analysis_id (analysis_id),
  key analysis_item_id (analysis_item_id),
  constraint report_structure_ibfk1 foreign key (analysis_id) references analysis (analysis_id) on delete cascade,
  constraint report_structure_ibfk2 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade
);