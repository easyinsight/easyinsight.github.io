drop table if exists exchange_report_install;
create table exchange_report_install (
  exchange_report_install_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  report_id bigint(20) not null,
  install_date datetime not null,
  primary key (exchange_report_install_id),
  constraint exchange_report_install_ibfk1 foreign key (user_id) references user (user_id) on delete cascade,
  constraint exchange_report_install_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade,
  index (install_date)
);

drop table if exists exchange_dashboard_install;
create table exchange_dashboard_install (
  exchange_dashboard_install_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  dashboard_id bigint(20) not null,
  install_date datetime not null,
  primary key (exchange_dashboard_install_id),
  constraint exchange_dashboard_install_ibfk1 foreign key (user_id) references user (user_id) on delete cascade,
  constraint exchange_dashboard_install_ibfk2 foreign key (dashboard_id) references dashboard (dashboard_id) on delete cascade,
  index (install_date)
);

alter table analysis add embed_username varchar(255) default null;
alter table analysis add embed_password varchar(255) default null;
alter table analysis add insecure_embed_enabled tinyint(4) not null default 0;

alter table last_value_filter add apply_across_report tinyint(4) not null default 1;
alter table last_value_filter add threshold integer not null default 1;

alter table first_value_filter add apply_across_report tinyint(4) not null default 1;
alter table first_value_filter add threshold integer not null default 1;