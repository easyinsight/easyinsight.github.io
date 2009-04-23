alter table task_generator add requires_backfill tinyint(4);

alter table account_activity add account_state integer;

drop table if exists virtual_transform;
create table virtual_transform (
  virtual_transform_id bigint(20) auto_increment not null,
  transform_dimension_id bigint(20) not null,
  primary key(virtual_transform_id),
  key transform_dimension_id (transform_dimension_id),
  constraint virtual_transform_ibfk1 foreign key (transform_dimension_id) references analysis_item (analysis_item_id) on delete cascade
);

drop table if exists virtual_transform_to_value;
create table virtual_transform_to_value (
  virtual_transform_to_value_id bigint(20) auto_increment not null,
  virtual_transform_id bigint(20) not null,
  value_id bigint(20) not null,
  primary key(virtual_transform_to_value_id),
  key virtual_transform_id (virtual_transform_id),
  key value_id (value_id),
  constraint virtual_transform_to_value_ibfk1 foreign key (virtual_transform_id) references virtual_transform (virtual_transform_id) on delete cascade,
  constraint virtual_transform_to_value_ibfk2 foreign key (value_id) references value (value_id) on delete cascade
);

drop table if exists virtual_dimension;
create table virtual_dimension (
  virtual_dimension_id bigint(20) auto_increment not null,
  analysis_dimension_id bigint(20) not null,
  name varchar(100) not null,
  primary key (virtual_dimension_id),
  key analysis_dimension_id (analysis_dimension_id),
  constraint virtual_dimension_ibfk1 foreign key (analysis_dimension_id) references analysis_item (analysis_item_id) on delete cascade
);

drop table if exists virtual_dimension_to_virtual_transform;
create table virtual_dimension_to_virtual_transform (
  virtual_dimension_to_virtual_transform_id bigint(20) auto_increment not null,
  virtual_transform_id bigint(20) not null,
  virtual_dimension_id bigint(20) not null,
  primary key(virtual_dimension_to_virtual_transform_id),
  key virtual_transform_id (virtual_transform_id),
  key virtual_dimension_id (virtual_dimension_id),
  constraint virtual_dimension_to_virtual_transform_ibfk1 foreign key (virtual_transform_id) references virtual_transform (virtual_transform_id) on delete cascade,
  constraint virtual_dimension_to_virtual_transform_ibfk2 foreign key (virtual_dimension_id) references virtual_dimension (virtual_dimension_id) on delete cascade
);

drop table if exists six_sigma_measure;
create table six_sigma_measure (
  six_sigma_measure_id bigint(20) auto_increment not null,
  analysis_item_id bigint(20) not null,
  sigma_type integer not null,
  defects_measure_id bigint(20) not null,
  opportunities_measure_id bigint(20) not null,
  primary key(six_sigma_measure_id),
  key analysis_item_id (analysis_item_id),
  key defects_measure_id (defects_measure_id),
  key opportunities_measure_id (opportunities_measure_id),
  constraint six_sigma_measure_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade,
  constraint six_sigma_measure_ibfk2 foreign key (defects_measure_id) references analysis_item (analysis_item_id) on delete cascade,
  constraint six_sigma_measure_ibfk3 foreign key (opportunities_measure_id) references analysis_item (analysis_item_id) on delete cascade
);

drop table if exists data_source_to_virtual_dimension;
create table data_source_to_virtual_dimension (
  data_source_to_virtual_dimension_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  virtual_dimension_id bigint(20) not null,
  primary key(data_source_to_virtual_dimension_id),
  key data_source_id (data_source_id),
  key virtual_dimension_id (virtual_dimension_id),
  constraint data_source_to_virtual_dimension_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id),
  constraint data_source_to_virtual_dimension_ibfk2 foreign key (virtual_dimension_id) references virtual_dimension (virtual_dimension_id)
);

alter table analysis_item add virtual_dimension_id bigint(20) default null;