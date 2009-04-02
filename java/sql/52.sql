DROP TABLE IF EXISTS analysis_hierarchy_item_to_hierarchy_level;
CREATE TABLE analysis_hierarchy_item_to_hierarchy_level (
  analysis_hierarchy_item_to_hierarchy_level_id bigint(20) auto_increment not null,
  analysis_item_id bigint(20) not null,
  hierarchy_level_id bigint(20) not null,
  primary key(analysis_hierarchy_item_to_hierarchy_level_id),
  key hierarchy_level_id (hierarchy_level_id),
  key analysis_item_id (analysis_item_id),
  constraint analysis_hierarchy_item_to_hierarchy_level_ibfk1 foreign key (hierarchy_level_id) references hierarchy_level (hierarchy_level_id),
  constraint analysis_hierarchy_item_to_hierarchy_level_ibfk2 foreign key (analysis_item_id) references analysis_item (analysis_item_id)
);


# ALTER TABLE hierarchy_level add position integer not null default 0;

# ALTER TABLE hierarchy_level drop parent_item_id;