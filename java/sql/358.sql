drop table if exists infusionsoft_report_source;
create table infusionsoft_report_source (
  infusionsoft_report_source_id bigint(20) not null auto_increment,
  data_source_id bigint(20) not null,
  report_id varchar(20) default null,
  user_id varchar(20) default null,
  primary key (infusionsoft_report_source_id),
  constraint infusionsoft_report_source_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

alter table infusionsoft add user_id varchar(20) default null;

alter table account add recache_time tinyint(4) not null default 0;