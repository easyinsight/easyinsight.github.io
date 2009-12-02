alter table range_filter add low_value_variable varchar(255) default null;
alter table range_filter add high_value_variable varchar(255) default null;alter table analysis_item add description text default null;

drop table if exists report_sequence;
create table report_sequence (
  report_sequence_id bigint(20) auto_increment not null,
  analysis_item_id bigint(20) not null,
  primary key(report_sequence_id),
  constraint report_sequence_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade
);

drop table if exists date_sequence;
create table date_sequence (
  date_sequence_id bigint(20) auto_increment not null,
  date_type integer not null,
  report_sequence_id bigint(20) not null,
  primary key(date_sequence_id),
  constraint date_sequence_ibfk1 foreign key (report_sequence_id) references report_sequence (report_sequence_id) on delete cascade
);

drop table if exists timeline_report;
create table timeline_report (
  timeline_report_id bigint(20) auto_increment not null,
  contained_report_id bigint(20) not null,
  report_sequence_id bigint(20) not null,
  report_state_id bigint(20) not null,
  primary key(timeline_report_id),
  constraint timeline_report_ibfk1 foreign key (contained_report_id) references analysis (analysis_id) on delete cascade,
  constraint timeline_report_ibfk2 foreign key (report_sequence_id) references report_sequence (report_sequence_id) on delete cascade,
  constraint timeline_report_ibfk3 foreign key (report_state_id) references report_state (report_state_id) on delete cascade
);