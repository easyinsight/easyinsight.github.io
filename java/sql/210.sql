alter table composite_connection add source_item_id bigint(20) default null;
alter table composite_connection add constraint composite_connection_ibfk6 foreign key (source_item_id) references analysis_item (analysis_item_id) on delete cascade;
alter table composite_connection add target_item_id bigint(20) default null;
alter table composite_connection add constraint composite_connection_ibfk7 foreign key (target_item_id) references analysis_item (analysis_item_id) on delete cascade;

alter table composite_connection change source_join source_join bigint(20) default null;
alter table composite_connection change target_join target_join bigint(20) default null;

drop table if exists join_override;
create table join_override (
  join_override_id bigint(20) auto_increment not null,
  source_analysis_item_id bigint(20) not null,
  target_analysis_item_id bigint(20) not null,
  primary key(join_override_id),
  constraint join_override_ibfk1 foreign key (source_analysis_item_id) references analysis_item (analysis_item_id) on delete cascade,
  constraint join_override_ibfk2 foreign key (target_analysis_item_id) references analysis_item (analysis_item_id) on delete cascade
);

drop table if exists analysis_to_join_override;
create table analysis_to_join_override (
  analysis_to_join_override_id bigint(20) auto_increment not null,
  analysis_id bigint(20) not null,
  join_override_id bigint(20) not null,
  primary key (analysis_to_join_override_id),
  constraint analysis_to_join_override_ibfk1 foreign key (analysis_id) references analysis (analysis_id) on delete cascade,
  constraint analysis_to_join_override_ibfk2 foreign key (join_override_id) references join_override (join_override_id) on delete cascade
);