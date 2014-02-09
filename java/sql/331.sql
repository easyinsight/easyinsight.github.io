create table data_source_additional_connection (
  data_source_additional_connection_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  connection_id bigint(20) not null,
  primary key (data_source_additional_connection_id),
  constraint data_source_additional_connection_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade,
  constraint data_source_additional_connection_ibfk2 foreign key (connection_id) references composite_connection (composite_connection_id) on delete cascade
);

alter table composite_connection change composite_feed_id composite_feed_id bigint(20) default null;

drop table if exists pivotalv5;
create table pivotalv5 (
  pivotalv5_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  token varchar(255) default null,
  primary key (pivotalv5_id),
  constraint pivotalv5_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

alter table dashboard_element_to_filter_set add position_index integer not null default 0;