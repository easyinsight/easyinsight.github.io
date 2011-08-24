alter table filter add minimum_role integer not null default 4;

drop table if exists month_cutoff_filter;
create table month_cutoff_filter (
  month_cutoff_filter_id bigint(20) auto_increment not null,
  filter_id bigint(20) not null,
  cutoff_month integer not null,
  primary key (month_cutoff_filter_id),
  constraint month_cutoff_filter_ibfk1 foreign key (filter_id) references filter (filter_id)
);

alter table quickbase_data_source add weights_id bigint(20) not null default 0;