drop table if exists action_log;
create table action_log (
  action_log_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  action_type integer not null,
  action_date datetime not null,
  primary key (action_log_id)
);

create index action_log_idx1 on action_log (action_date);

drop table if exists action_report_log;
create table action_report_log (
  action_report_log_id bigint(20) auto_increment not null,
  report_id bigint(20) not null,
  action_log_id bigint(20) not null,
  primary key (action_report_log_id),
  constraint action_report_log_ibfk1 foreign key (action_log_id) references action_log (action_log_id) on delete cascade,
  constraint action_report_log_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade
);

drop table if exists action_data_source_log;
create table action_data_source_log (
  action_data_source_log_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  action_log_id bigint(20) not null,
  primary key (action_data_source_log_id),
  constraint action_data_source_log_ibfk1 foreign key (action_log_id) references action_log (action_log_id) on delete cascade,
  constraint action_data_source_log_ibfk2 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists action_dashboard_log;
create table action_dashboard_log (
  action_dashboard_log_id bigint(20) auto_increment not null,
  dashboard_id bigint(20) not null,
  action_log_id bigint(20) not null,
  primary key (action_dashboard_log_id),
  constraint action_dashboard_log_ibfk1 foreign key (action_log_id) references action_log (action_log_id) on delete cascade,
  constraint action_dashboard_log_ibfk2 foreign key (dashboard_id) references dashboard (dashboard_id) on delete cascade
);

create index account_timed_state_idx2 on account_timed_state (account_state);

