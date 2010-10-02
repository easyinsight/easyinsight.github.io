alter table heat_map add display_type integer not null default 1;
alter table heat_map add point_report_id bigint default null;
alter table heat_map add constraint heat_map_ibfk2 foreign key (point_report_id) references analysis (analysis_id) on delete set null;

alter table analysis_measure add row_count_field tinyint not null default 0;

alter table data_feed add adjust_dates tinyint not null default 0;

drop table if exists data_source_refresh_audit;
create table data_source_refresh_audit (
  data_source_refresh_audit_id bigint(20) auto_increment not null,
  account_id bigint(20) not null,
  data_source_id bigint(20) not null,
  server_id varchar(255) not null,
  start_time timestamp default 0,
  end_time timestamp default 0,
  primary key (data_source_refresh_audit_id),
  constraint data_source_refresh_audit_ibfk1 foreign key (account_id) references account (account_id) on delete cascade,
  constraint data_source_refresh_audit_ibfk2 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);
