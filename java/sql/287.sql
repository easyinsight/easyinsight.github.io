create table report_multi_color_property (
  report_multi_color_property_id bigint(20) auto_increment not null,
  report_property_id bigint(20) not null,
  color1Start integer not null default 0,
  color1StartEnabled tinyint(4) not null default 0,
  color1End integer not null default 0,
  color1EndEnabled tinyint(4) not null default 0,
  primary key (report_multi_color_property_id),
  constraint report_multi_color_property_ibfk1 foreign key (report_property_id) references report_property (report_property_id) on delete cascade
);