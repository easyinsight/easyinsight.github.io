drop table if exists gantt_chart;
create table gantt_chart (
  gantt_chart_id bigint(20) auto_increment not null,
  report_state_id bigint(20) not null,
  primary key(gantt_chart_id),
  constraint gantt_chart_ibfk1 foreign key (report_state_id) references report_state (report_state_id) on delete cascade
);

drop table if exists data_source_refresh_audit;
create table data_source_refresh_audit (
  data_source_refresh_audit_id bigint(20) auto_increment not null,
  account_id bigint(20) not null,
  data_source_id bigint(20) not null,
  server_id varchar(255) not null,
  start_time timestamp not null,
  end_time timestamp default 0,
  primary key (data_source_refresh_audit_id),
  constraint data_source_refresh_audit_ibfk1 foreign key (account_id) references account (account_id) on delete cascade,
  constraint data_source_refresh_audit_ibfk2 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade 
);