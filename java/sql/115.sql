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