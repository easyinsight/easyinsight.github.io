alter table date_range_filter add bounding_start_date datetime default null;
alter table date_range_filter add bounding_end_date datetime default null;
alter table date_range_filter add start_dimension bigint(20) default null;
alter table date_range_filter add constraint date_range_filter_ibfk3 foreign key (start_dimension) references analysis_item (analysis_item_id) on delete set null;
alter table date_range_filter add end_dimension bigint(20) default null;
alter table date_range_filter add constraint date_range_filter_ibfk4 foreign key (end_dimension) references analysis_item (analysis_item_id) on delete set null;

drop table if exists lookup_table;
create table lookup_table (
  lookup_table_id bigint(20) auto_increment not null,
  lookup_table_name varchar(200) not null,
  source_item_id bigint(20) not null,
  target_item_id bigint(20) not null,
  data_source_id bigint(20) not null,
  primary key(lookup_table_id),
  constraint lookup_table_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade,
  constraint lookup_table_ibfk2 foreign key (source_item_id) references analysis_item (analysis_item_id) on delete cascade,
  constraint lookup_table_ibfk3 foreign key (target_item_id) references analysis_item (analysis_item_id) on delete cascade
) ENGINE=InnoDB;

drop table if exists lookup_pair;
create table lookup_pair (
  lookup_pair_id bigint(20) auto_increment not null,
  lookup_table_id bigint(20) not null,
  source_value varchar(200) not null,
  target_value varchar(200) default null,
  target_date_value datetime default null,
  primary key (lookup_pair_id),
  constraint lookup_pair_ibfk1 foreign key (lookup_table_id) references lookup_table (lookup_table_id) on delete cascade
) ENGINE=InnoDB;

alter table analysis_item add lookup_table_id bigint(20) default null;
alter table analysis_item add constraint analysis_item_ibfk7 foreign key (lookup_table_id) references lookup_table (lookup_table_id) on delete cascade;