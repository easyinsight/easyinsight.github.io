alter table solution add data_source_type integer default null;
alter table solution drop description;
alter table solution drop author;
alter table solution drop footer_text;
alter table solution drop screencast_directory;
alter table solution drop screencast_mp4_name;
alter table solution drop goal_tree_id;