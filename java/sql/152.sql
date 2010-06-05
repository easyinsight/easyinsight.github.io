drop table if exists report_property;
create table report_property (
  report_property_id bigint(20) auto_increment not null,
  property_name varchar(255) not null,
  primary key (report_property_id)
);

drop table if exists report_numeric_property;
create table report_numeric_property (
  report_numeric_property_id bigint(20) auto_increment not null,
  report_property_id bigint(20) not null,
  property_value double not null,
  primary key (report_numeric_property_id),
  constraint report_numeric_property_ibfk1 foreign key (report_property_id) references report_property (report_property_id) on delete cascade
);

drop table if exists report_string_property;
create table report_string_property (
  report_string_property_id bigint(20) auto_increment not null,
  report_property_id bigint(20) not null,
  property_value varchar(255) not null,
  primary key (report_string_property_id),
  constraint report_string_property_ibfk1 foreign key (report_property_id) references report_property (report_property_id) on delete cascade
);

drop table if exists report_to_report_property;
create table report_to_report_property (
  report_to_report_property_id bigint(20) auto_increment not null,
  analysis_id bigint(20) not null,
  report_property_id bigint(20) not null,
  primary key (report_to_report_property_id),
  constraint report_to_report_property_ibfk1 foreign key (analysis_id) references analysis (analysis_id) on delete cascade,
  constraint report_to_report_property_ibfk2 foreign key (report_property_id) references report_property (report_property_id) on delete cascade
);