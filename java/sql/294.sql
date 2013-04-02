
drop table if exists postgres_database_connection;
create table postgres_database_connection (
  postgres_database_connection_id bigint(20) auto_increment not null,
  host_name varchar(255) default null,
  server_port integer default null,
  database_name varchar(255) default null,
  database_username varchar(255) default null,
  database_password varchar(255) default null,
  rebuild_fields tinyint(4) not null default 0,
  query_string text default null,
  data_source_id bigint(20) not null,
  primary key (postgres_database_connection_id),
  constraint postgres_database_connection_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);


drop table if exists pdf_delivery_Extension;
create table pdf_delivery_extension (
  pdf_delivery_extension_id bigint(20) auto_increment not null,
  report_delivery_id bigint(20) default null,
  delivery_to_report_id bigint(20) default null,
  show_header tinyint(4) not null default 0,
  export_mode tinyint(4) not null default 0,
  delivery_to_dashboard_id bigint(20) default null,
  width integer not null default 0,
  height integer not null default 0,
  primary key (pdf_delivery_extension_id),
  constraint pdf_delivery_extension_ibfk1 foreign key (report_delivery_id) references report_delivery (report_delivery_id) on delete cascade,
  constraint pdf_delivery_extension_ibfk2 foreign key (delivery_to_report_id) references delivery_to_report (delivery_to_report_id) on delete cascade,
  constraint pdf_delivery_extension_ibfk3 foreign key (delivery_to_dashboard_id) references delivery_to_dashboard (delivery_to_dashboard_id) on delete cascade
);

alter table email_selenium_processor add action_type integer not null default 0;

alter table quickbase_composite_source add rebuild_fields tinyint(4) not null default 0;



alter table value_based_filter add new_type tinyint(4) not null default 0;

alter table mysql_database_connection add timeout integer not null default 5;
alter table sql_server_database_connection add timeout integer not null default 5;
alter table oracle_database_connection add timeout integer not null default 5;
alter table postgres_database_connection add timeout integer not null default 5;

drop table if exists delivery_to_dashboard;
create table delivery_to_dashboard (
  delivery_to_dashboard_id bigint(20) auto_increment not null,
  general_delivery_id bigint(20) not null,
  dashboard_id bigint(20) not null,
  delivery_index int(11) NOT NULL,
  delivery_format int(11) NOT NULL,
  delivery_label varchar(255) DEFAULT NULL,
  send_if_no_data tinyint(4) NOT NULL,
  primary key (delivery_to_dashboard_id),
  constraint delivery_to_dashboard_ibfk1 foreign key (general_delivery_id) references general_delivery (general_delivery_id) on delete cascade,
  constraint delivery_to_dashboard_ibfk2 foreign key (dashboard_id) references dashboard (dashboard_id) on delete cascade
);