alter table user add invoice_recipient tinyint(4) not null default 0;
update user set invoice_recipient = 1 where account_admin = 1;
alter table user add refresh_reports tinyint(4) not null default 0;

drop table if exists data_source_refresh_log;
create table data_source_refresh_log (
  data_source_refresh_log_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  refresh_time datetime not null,
  primary key (data_source_refresh_log_id),
  constraint data_source_refresh_log_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists get_satisfaction_composite;
create table get_satisfaction_composite (
  get_satisfaction_composite_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  primary key (get_satisfaction_composite_id),
  constraint get_satisfaction_composite_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

alter table freshbooks add live_data_source tinyint(4) not null default 1;