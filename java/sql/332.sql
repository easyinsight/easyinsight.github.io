alter table application_skin add connection_type integer not null default 0;

create table report_install_info (
  report_install_info_id bigint(20) auto_increment not null,
  origin_report_id bigint(20) not null,
  result_report_id bigint(20) not null,
  data_source_id bigint(20) not null,
  install_date datetime not null,
  primary key (report_install_info_id)
);

create index report_install_info_idx1 on report_install_info (origin_report_id);
create index report_install_info_idx2 on report_install_info (result_report_id);

create table dashboard_install_info (
  dashboard_install_info_id bigint(20) auto_increment not null,
  origin_dashboard_id bigint(20) not null,
  result_dashboard_id bigint(20) not null,
  data_source_id bigint(20) not null,
  install_date datetime not null,
  primary key (dashboard_install_info_id)
);

create index dashboard_install_info_idx1 on dashboard_install_info (origin_dashboard_id);
create index dashboard_install_info_idx2 on dashboard_install_info (result_dashboard_id);

drop table if exists field_rule;
create table field_rule (
  field_rule_id bigint(20) auto_increment not null,
  link_id bigint(20) default null,
  data_source_id bigint(20) not null,
  field_type integer not null default 0,
  tag_id bigint(20) default null,
  display_name varchar(255) default null,
  all_fields tinyint(4) not null default 0,
  extension_id bigint(20) default null,
  field_order integer not null default 0,
  primary key (field_rule_id),
  constraint field_rule_ibfk1 foreign key (link_id) references link (link_id) on delete set null,
  constraint field_rule_ibfk2 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade,
  constraint field_rule_ibfk3 foreign key (extension_id) references report_field_extension (report_field_extension_id) on delete cascade,
  constraint field_rule_ibfk4 foreign key (tag_id) references account_tag (account_tag_id) on delete cascade
);