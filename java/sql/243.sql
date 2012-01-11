drop table if exists delivery_to_filter_definition;
create table delivery_to_filter_definition (
  delivery_to_filter_definition_id bigint(20) auto_increment not null,
  filter_id bigint(20) not null,
  report_delivery_id bigint(20) not null,
  primary key (delivery_to_filter_definition_id),
  constraint delivery_to_filter_definition_ibfk1 foreign key (filter_id) references filter (filter_id) on delete cascade,
  constraint delivery_to_filter_definition_ibfk2 foreign key (report_delivery_id) references report_delivery (report_delivery_id) on delete cascade
);

drop table if exists delivery_to_report_to_filter;
create table delivery_to_report_to_filter (
  delivery_to_report_to_filter_id bigint(20) auto_increment not null,
  delivery_to_report_id bigint(20) not null,
  filter_id bigint(20) not null,
  primary key (delivery_to_report_to_filter_id),
  constraint delivery_to_report_to_filter_ibfk1 foreign key (filter_id) references filter (filter_id) on delete cascade,
  constraint delivery_to_report_to_filter_ibfk2 foreign key (delivery_to_report_id) references delivery_to_report (delivery_to_report_id) on delete cascade
) type = InnoDB;

alter table dashboard_report add auto_calculate_height tinyint(4) not null default 0;

alter table report_delivery add delivery_label varchar(255) default null;

alter table delivery_to_report add delivery_label varchar(255) default null;
alter table general_delivery add delivery_label varchar(255) default null;