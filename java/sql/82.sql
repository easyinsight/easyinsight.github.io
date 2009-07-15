drop table if exists folder;
create table folder (
  folder_id bigint(20) auto_increment not null,
  folder_name varchar(255) not null,
  data_source_id bigint(20) not null,
  primary key (folder_id),
  constraint folder_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists folder_to_analysis_item;
create table folder_to_analysis_item (
  folder_to_analysis_item_id bigint(20) auto_increment not null,
  folder_id bigint(20) not null,
  analysis_item_id bigint(20) not null,
  primary key(folder_to_analysis_item_id),
  constraint folder_to_analysis_item_ibfk1 foreign key (folder_id) references folder (folder_id) on delete cascade,
  constraint folder_to_analysis_item_ibfk2 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade
);

drop table if exists folder_to_folder;
create table folder_to_folder (
  folder_to_folder_id bigint(20) auto_increment not null,
  parent_folder_id bigint(20) not null,
  child_folder_id bigint(20) not null,
  primary key(folder_to_folder_id),
  constraint folder_to_folder_ibfk1 foreign key (parent_folder_id) references folder (folder_id) on delete cascade,
  constraint folder_to_folder_ibfk2 foreign key (child_folder_id) references folder (folder_id) on delete cascade
);

drop table if exists salesforce_definition;
create table salesforce_definition (
  salesforce_definition_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  primary key(salesforce_definition_id),
  constraint salesforce_definition_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);