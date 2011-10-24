drop table if exists diagram_report_field_extension;
create table diagram_report_field_extension (
  diagram_report_field_extension_id bigint(20) auto_increment not null,
  report_field_extension_id bigint(20) not null,
  x integer not null default 0,
  y integer not null default 0,
  primary key (diagram_report_field_extension_id),
  constraint diagram_report_field_extension_ibfk1 foreign key (report_field_extension_id) references report_field_extension (report_field_extension_id) on delete cascade
);

drop table if exists diagram_report;
create table diagram_report (
  diagram_report_id bigint(20) auto_increment not null,
  report_state_id bigint(20) not null,
  filter_name varchar(255) not null,
  day_window varchar(255) not null,
  primary key(diagram_report_id),
  constraint diagram_report_ibfk1 foreign key (report_state_id) references report_state (report_state_id) on delete cascade
) ENGINE=InnoDB;

drop table if exists diagram_report_link;
create table diagram_report_link (
  diagram_report_link_id bigint(20) auto_increment not null,
  source_id bigint(20) not null,
  target_id bigint(20) not null,
  link_name varchar(255) not null,
  primary key (diagram_report_link_id),
  constraint diagram_report_link_ibfk1 foreign key (source_id) references analysis_item (analysis_item_id) on delete cascade,
  constraint diagram_report_link_ibfk2 foreign key (target_id) references analysis_item (analysis_item_id) on delete cascade
) ENGINE=InnoDB;

drop table if exists diagram_report_to_diagram_report_link;
create table diagram_report_to_diagram_report_link (
  diagram_report_to_diagram_report_link_id bigint(20) auto_increment not null,
  diagram_report_id bigint(20) not null,
  diagram_report_link_id bigint(20) not null,
  primary key (diagram_report_to_diagram_report_link_id),
  constraint diagram_report_to_diagram_report_link_ibfk1 foreign key (diagram_report_id) references report_state (report_state_id) on delete cascade,
  constraint diagram_report_to_diagram_report_link_ibfk2 foreign key (diagram_report_link_id) references diagram_report_link (diagram_report_link_id) on delete cascade
) ENGINE=InnoDB;

alter table analysis_item add marmotscript text default null;

alter table filter add trend_filter tinyint(4) not null default 0;

drop table if exists text_report_field_extension;
create table text_report_field_extension (
  text_report_field_extension_id bigint(20) auto_increment not null,
  align varchar(255) default null,
  font_size integer not null default 0,
  word_wrap tinyint(4) not null default 0,
  fixed_width integer not null default 0,
  report_field_extension_id bigint(20) not null,
  primary key (text_report_field_extension_id),
  constraint text_report_field_extension_ibfk1 foreign key (report_field_extension_id) references report_field_extension (report_field_extension_id) on delete cascade
) ENGINE=InnoDB;

alter table dashboard_element add preferred_height integer not null default 0;
alter table dashboard_element add preferred_width integer not null default 0;

alter table dashboard add background_color integer not null default 13421772;
alter table dashboard add padding integer not null default 3;
alter table dashboard add border_color integer not null default 3;
alter table dashboard add border_thickness integer not null default 1;