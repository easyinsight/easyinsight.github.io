

alter table excel_export add excel_format integer not null default 0;

drop table if exists summary_report;
create table summary_report (
  summary_report_id bigint(20) auto_increment not null,
  report_state_id bigint(20) not null,
  primary key (summary_report_id),
  constraint summary_report_ibfk1 foreign key (report_state_id) references report_state (report_state_id) on delete cascade
);

drop table if exists news_entry;
create table news_entry (
  news_entry_id bigint(20) auto_increment not null,
  news_entry_text text default null,
  news_entry_title varchar(255) default null,
  entry_time datetime not null,
  primary key (news_entry_id)
);

