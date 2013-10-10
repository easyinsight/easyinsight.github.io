alter table dashboard add dashboard_version tinyint(4) not null default 0;
alter table dashboard add persist_state tinyint(4) not null default 0;

alter table analysis_item add reload_from_data_source tinyint(4) not null default 0;
alter table analysis_item add parent_item_id bigint(20) default null;
alter table analysis_item add constraint analysis_item_ibfk12 foreign key (parent_item_id) references analysis_item (analysis_item_id) on delete set null;

alter table analysis_item add flex_id bigint(20) not null default 0;

alter table dashboard_element add url_key varchar(50) default null;

create table dashboard_state (
  dashboard_state_id bigint(20) auto_increment not null,
  dashboard_id bigint(20) default null,
  report_id bigint(20) default null,
  primary key (dashboard_state_id),
  constraint dashboard_state_ibfk1 foreign key (dashboard_id) references dashboard (dashboard_id) on delete cascade,
  constraint dashboard_state_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade
);

drop table if exists dashboard_state_to_filter;
create table dashboard_state_to_filter (
  dashboard_state_to_filter_id bigint(20) auto_increment not null,
  dashboard_state_id bigint(20) not null,
  filter_key varchar(255) not null,
  source_filter_id bigint(20) not null,
  filter_id bigint(20) not null,
  primary key (dashboard_state_to_filter_id),
  constraint dashboard_state_to_filter_ibfk1 foreign key (dashboard_state_id) references dashboard_state (dashboard_state_id) on delete cascade,
  constraint dashboard_state_to_filter_ibfk2 foreign key (filter_id) references filter (filter_id),
  constraint dashboard_state_to_filter_ibfk3 foreign key (source_filter_id) references filter (filter_id)
);

create table dashboard_state_stack_position (
  dashboard_state_stack_position_id bigint(20) auto_increment not null,
  dashboard_state_id bigint(20) not null,
  url_key varchar(50) not null,
  stack_position integer not null,
  primary key (dashboard_state_stack_position_id),
  constraint dashboard_state_stack_position_ibfk1 foreign key (dashboard_state_id) references dashboard_state (dashboard_state_id) on delete cascade
);

create table dashboard_state_to_report (
  dashboard_state_to_report_id bigint(20) auto_increment not null,
  dashboard_state_id bigint(20) not null,
  report_id bigint(20) not null,
  url_key varchar(50) not null,
  primary key (dashboard_state_to_report_id),
  constraint dashboard_state_to_report_ibfk1 foreign key (dashboard_state_id) references dashboard_state (dashboard_state_id) on delete cascade,
  constraint dashboard_state_to_report_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade
);

create table dashboard_link (
  dashboard_link_id bigint(20) auto_increment not null,
  dashboard_state_id bigint(20) not null,
  url_key varchar(50) not null,
  primary key (dashboard_link_id),
  constraint dashboard_link_ibfk1 foreign key (dashboard_state_id) references dashboard_state (dashboard_state_id) on delete cascade
);

alter table filter add drillthrough tinyint(4) not null default 0;

alter table analysis_item add based_on_report_field bigint(20) default null;
alter table analysis_item add constraint analysis_item_ibfk16 foreign key (based_on_report_field) references analysis_item (analysis_item_id) on delete set null;