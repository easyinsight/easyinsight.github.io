alter table field_rule add rule_data_source_id bigint(20) default null;
alter table field_rule add default_date varchar(255) default null;

alter table diagram_report change filter_name filter_name varchar(255) default null;
alter table trend_grid_report change filter_name filter_name varchar(255) default null;
alter table trend_report change filter_name filter_name varchar(255) default null;

create table redbooth (
  redbooth_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  access_token varchar(255) default null,
  refresh_token varchar(255) default null,
  primary key (redbooth_id),
  constraint redbooth_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);