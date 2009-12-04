drop table if exists heat_map;
create table heat_map (
  heat_map_id bigint(20) auto_increment not null,
  report_state_id bigint(20) not null,
  map_type integer not null,
  latitude double not null,
  longitude double not null,
  zoom_level integer not null,
  primary key(heat_map_id),
  constraint heat_map_ibfk1 foreign key (report_state_id) references report_state (report_state_id) on delete cascade
);

drop table if exists analysis_coordinate;
create table analysis_coordinate (
  analysis_coordinate_id bigint(20) auto_increment not null,
  analysis_item_id bigint(20) not null,
  precision_value integer not null,
  primary key(analysis_coordinate_id),
  constraint analysis_coordinate_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade
);

drop table if exists analysis_longitude;
create table analysis_longitude (
  analysis_longitude_id bigint(20) auto_increment not null,
  analysis_item_id bigint(20) not null,
  primary key(analysis_longitude_id),
  constraint analysis_longitude_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade
);

drop table if exists analysis_latitude;
create table analysis_latitude (
  analysis_latitude_id bigint(20) auto_increment not null,
  analysis_item_id bigint(20) not null,
  primary key(analysis_latitude_id),
  constraint analysis_latitude_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade
);