alter table ytd_field_extension add line_above tinyint(4) not null default 0;

drop table if exists vertical_list_field_extension;
create table vertical_list_field_extension (
  vertical_list_field_extension_id bigint(20) auto_increment not null,
  report_field_extension_id bigint(20) not null,
  line_above tinyint(4) not null default 0,
  primary key (vertical_list_field_extension_id),
  constraint vertical_list_field_extension_ibfk1 foreign key (report_field_extension_id) references report_field_extension (report_field_extension_id) on delete cascade
);

alter table user add analyst tinyint(4) not null default 1;