alter table user add time_zone integer not null default 0;

# alter table account add billing_failures integer not null default 0;

alter table user add html_or_flex tinyint(4) not null default 0;

drop table if exists data_source_fault;
create table data_source_fault (
  data_source_fault_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  fault_message text default null,
  primary key (data_source_fault_id),
  constraint data_source_fault_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

# alter table basecamp add company_project_join tinyint(4) not null default 0;