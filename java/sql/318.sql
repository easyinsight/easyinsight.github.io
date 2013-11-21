alter table account_tag add data_source_tag tinyint(4) not null default 1;
alter table account_tag add report_tag tinyint(4) not null default 0;
alter table account_tag add field_tag tinyint(4) not null default 0;



create table field_to_tag (
  field_to_tag_id bigint(20) auto_increment not null,
  analysis_item_id bigint(20) not null,
  account_tag_id bigint(20) not null,
  primary key (field_to_tag_id),
  constraint field_to_tag_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade,
  constraint field_to_tag_ibfk2 foreign key (account_tag_id) references account_tag (account_tag_id) on delete cascade
);

create table report_to_tag (
  report_to_tag_id bigint(20) auto_increment not null,
  tag_id bigint(20) not null,
  report_id bigint(20) not null,
  primary key (report_to_tag_id),
  constraint report_to_tag_ibfk1 foreign key (tag_id) references account_tag (account_tag_id) on delete cascade,
  constraint report_to_tag_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade
);

create table dashboard_to_tag (
  dashboard_to_tag_id bigint(20) auto_increment not null,
  tag_id bigint(20) not null,
  dashboard_id bigint(20) not null,
  primary key (dashboard_to_tag_id),
  constraint dashboard_to_tag_ibfk1 foreign key (tag_id) references account_tag (account_tag_id) on delete cascade,
  constraint dashboard_to_tag_ibfk2 foreign key (dashboard_id) references dashboard (dashboard_id) on delete cascade
);

create table storage_database (
  storage_database_id bigint(20) auto_increment not null,
  database_name varchar(255) not null,
  host varchar(255) not null,
  port integer not null,
  database_alias varchar(255) not null,
  database_username varchar(255) not null,
  database_password varchar(255) not null,
  primary key (storage_database_id)
);



create table group_to_tag (
  group_to_tag_id bigint(20) auto_increment not null,
  account_tag_id bigint(20) not null,
  group_id bigint(20) not null,
  primary key (group_to_tag_id),
  constraint group_to_tag_ibfk1 foreign key (account_tag_id) references account_tag (account_tag_id) on delete cascade,
  constraint group_to_tag_ibfk2 foreign key (group_id) references community_group (community_group_id) on delete cascade
);



drop table if exists distinct_cached_addon_report_source;
create table distinct_cached_addon_report_source (
  distinct_cached_addon_report_source_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  report_id bigint(20) default null,
  primary key (distinct_cached_addon_report_source_id),
  constraint distinct_cached_addon_report_source_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade,
  constraint distinct_cached_addon_report_source_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade
);

create table hibernate_tag (
  hibernate_tag_id bigint(20) auto_increment not null,
  tag_id bigint(20) not null,
  primary key (hibernate_tag_id),
  constraint hibernate_tag_ibfk1 foreign key (tag_id) references account_tag (account_tag_id) on delete cascade
);

create table filter_to_field_tag (
  filter_to_field_tag_id bigint(20) auto_increment not null,
  filter_id bigint(20) not null,
  hibernate_tag_id bigint(20) not null,
  primary key (filter_to_field_tag_id),
  constraint filter_to_field_tag_ibfk1 foreign key (filter_id) references filter (filter_id) on delete cascade,
  constraint filter_to_field_tag_ibfk2 foreign key (hibernate_tag_id) references hibernate_tag (hibernate_tag_id) on delete cascade
);

drop table if exists ordering_analysis_item_filter_to_analysis_item_handle;
 CREATE TABLE ordering_analysis_item_filter_to_analysis_item_handle (
  ordering_analysis_item_filter_to_analysis_item_handle_id bigint(20) NOT NULL AUTO_INCREMENT,
  filter_id bigint(20) NOT NULL,
  analysis_item_handle_id bigint(20) NOT NULL,
  PRIMARY KEY (ordering_analysis_item_filter_to_analysis_item_handle_id),
  CONSTRAINT ordering_analysis_item_filter_to_analysis_item_handle_ibfk1 FOREIGN KEY (filter_id) REFERENCES filter (filter_id) ON DELETE CASCADE,
  CONSTRAINT ordering_analysis_item_filter_to_analysis_item_handle_ibfk2 FOREIGN KEY (analysis_item_handle_id) REFERENCES analysis_item_handle (analysis_item_handle_id) ON DELETE CASCADE
);



drop table if exists analysis_item_to_report_field_extension;
create table analysis_item_to_report_field_extension (
  analysis_item_to_report_field_extension_id bigint(20) auto_increment not null,
  analysis_item_id bigint(20) not null,
  report_field_extension_id bigint(2) not null,
  extension_type integer not null,
  primary key (analysis_item_to_report_field_extension_id),
  constraint analysis_item_to_report_field_extension_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade,
  constraint analysis_item_to_report_field_extension_ibfk2 foreign key (report_field_extension_id) references report_field_extension (report_field_extension_id) on delete cascade
);