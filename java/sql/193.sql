drop table if exists dashboard;
create table dashboard (
  dashboard_id bigint(20) auto_increment not null,
  dashboard_name varchar(255) not null,
  url_key varchar(255) not null,
  account_visible tinyint(4) not null,
  data_source_id bigint(20) not null,
  creation_date timestamp not null,
  update_date timestamp not null,
  description text not null,
  author_name varchar(255) not null,
  exchange_visible tinyint(4) not null default 0,
  temporary_dashboard tinyint(4) not null,
  primary key(dashboard_id),
  constraint dashboard_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade,
  index (account_visible),
  index (exchange_visible)
);

drop table if exists dashboard_report;
create table dashboard_report (
  dashboard_report_id bigint(20) auto_increment not null,
  report_id bigint(20) not null,
  label_placement integer not null default 0,
  show_label tinyint(4) not null default 0,
  dashboard_element_id bigint(20) not null,
  primary key(dashboard_report_id),
  constraint dashboard_report_ibfk1 foreign key (report_id) references analysis (analysis_id) on delete cascade,
  constraint dashboard_report_ibfk2 foreign key (dashboard_element_id) references dashboard_element (dashboard_element_id) on delete cascade
);

drop table if exists dashboard_stack;
create table dashboard_stack (
  dashboard_stack_id bigint(20) auto_increment not null,
  stack_size integer not null,
  effect integer not null,
  effect_duration integer not null,
  dashboard_element_id bigint(20) not null,
  primary key (dashboard_stack_id),
  constraint dashboard_stack_ibfk1 foreign key (dashboard_element_id) references dashboard_element (dashboard_element_id) on delete cascade
);

drop table if exists dashboard_stack_item;
create table dashboard_stack_item (
  dashboard_stack_item_id bigint(20) auto_increment not null,
  dashboard_element_id bigint(20) not null,
  dashboard_stack_id bigint(20) not null,
  item_position integer not null,
  primary key (dashboard_stack_item_id),
  constraint dashboard_stack_item_ibfk1 foreign key (dashboard_element_id) references dashboard_element (dashboard_element_id) on delete cascade,
  constraint dashboard_stack_item_ibfk2 foreign key (dashboard_stack_id) references dashboard_stack (dashboard_stack_id) on delete cascade
);

drop table if exists dashboard_grid;
create table dashboard_grid (
  dashboard_grid_id bigint(20) auto_increment not null,
  dashboard_element_id bigint(20) not null,
  number_rows integer not null,
  number_columns integer not null,
  primary key(dashboard_grid_id),
  constraint dashboard_grid_ibfk1 foreign key (dashboard_element_id) references dashboard_element (dashboard_element_id) on delete cascade
);

drop table if exists dashboard_grid_item;
create table dashboard_grid_item (
  dashboard_grid_item_id bigint(20) auto_increment not null,
  dashboard_element_id bigint(20) not null,
  row_position integer not null,
  column_position integer not null,
  dashboard_grid_id bigint(20) not null,
  primary key (dashboard_grid_item_id),
  constraint dashboard_grid_item_ibfk1 foreign key (dashboard_element_id) references dashboard_element (dashboard_element_id) on delete cascade
);

drop table if exists user_to_dashboard;
create table user_to_dashboard (
  user_to_dashboard_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  dashboard_id bigint(20) not null,
  primary key(user_to_dashboard_id),
  constraint user_to_dashboard_ibfk1 foreign key (user_id) references user (user_id) on delete cascade,
  constraint user_to_dashboard_ibfk2 foreign key (dashboard_id) references dashboard (dashboard_id) on delete cascade
);

drop table if exists dashboard_to_filter;
create table dashboard_to_filter (
  dashboard_to_filter_id bigint(20) auto_increment not null,
  dashboard_id bigint(20) not null,
  filter_id bigint(20) not null,
  primary key (dashboard_to_filter_id),
  constraint dashboard_to_filter_ibfk1 foreign key (dashboard_id) references dashboard (dashboard_id) on delete cascade,
  constraint dashboard_to_filter_ibfk2 foreign key (filter_id) references filter (filter_id) on delete cascade
);

drop table if exists dashboard_element;
create table dashboard_element (
  dashboard_element_id bigint(20) auto_increment not null,
  element_type integer not null,
  primary key (dashboard_element_id)
);

drop table if exists dashboard_to_dashboard_element;
create table dashboard_to_dashboard_element (
  dashboard_to_dashboard_element_id bigint(20) auto_increment not null,
  dashboard_id bigint(20) not null,
  dashboard_element_id bigint(20) not null,
  primary key (dashboard_to_dashboard_element_id),
  constraint dashboard_to_dashboard_element_ibfk1 foreign key (dashboard_id) references dashboard (dashboard_id) on delete cascade,
  constraint dashboard_to_dashboard_element_ibfk2 foreign key (dashboard_element_id) references dashboard_element (dashboard_element_id) on delete cascade
);

drop table if exists dashboard_user_rating;
create table dashboard_user_rating (
  dashboard_user_rating_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  rating integer not null,
  dashboard_id bigint(20) not null,
  primary key (dashboard_user_rating_id),
  constraint dashboard_user_rating_ibfk1 foreign key (user_id) references user (user_id) on delete cascade,
  constraint dashboard_user_rating_ibfk2 foreign key (dashboard_id) references dashboard (dashboard_id) on delete cascade
);

drop table if exists dashboard_install;
create table dashboard_install (
  dashboard_install_id bigint(20) auto_increment not null,
  dashboard_id bigint(20) not null,
  user_id bigint(20) not null,
  install_count integer not null default 1,
  primary key (dashboard_install_id),
  constraint dashboard_install_ibfk1 foreign key (user_id) references user (user_id) on delete cascade,
  constraint dashboard_install_ibfk2 foreign key (dashboard_id) references dashboard (dashboard_id) on delete cascade
);

drop table if exists report_install;
create table report_install (
  report_install_id bigint(20) auto_increment not null,
  report_id bigint(20) not null,
  user_id bigint(20) not null,
  install_count integer not null default 1,
  primary key (report_install_id),
  constraint report_install_ibfk1 foreign key (user_id) references user (user_id) on delete cascade,
  constraint report_install_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade
);

drop table if exists dashboard_element_to_report_property;
create table dashboard_element_to_report_property (
  dashboard_element_to_report_property_id bigint(20) auto_increment not null,
  dashboard_element_id bigint(20) not null,
  report_property_id bigint(20) not null,
  primary key (dashboard_element_to_report_property_id),
  constraint dashboard_element_to_report_property_ibfk1 foreign key (dashboard_element_id) references dashboard_element (dashboard_element_id) on delete cascade,
  constraint dashboard_element_to_report_property_ibfk2 foreign key (report_property_id) references report_property (report_property_id) on delete cascade
);