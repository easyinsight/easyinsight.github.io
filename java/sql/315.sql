create index report_cache_idx1 on report_cache (report_id, cache_key(255));







CREATE TABLE multi_analysis_item_filter (
  multi_analysis_item_filter_id bigint(20) NOT NULL AUTO_INCREMENT,
  filter_id bigint(20) NOT NULL,
  show_all tinyint(4) NOT NULL default 1,
  selected tinyint(4) not null default 0,
  position integer not null default 0,
  PRIMARY KEY (multi_analysis_item_filter_id),
  CONSTRAINT multi_analysis_item_filter_ibfk1 FOREIGN KEY (filter_id) REFERENCES filter (filter_id) ON DELETE CASCADE
);

drop table if exists analysis_item_handle;
create table analysis_item_handle (
  analysis_item_handle_id bigint(20) not null auto_increment,
  analysis_item_id bigint(20) default null,
  name varchar(255) default null,
  primary key (analysis_item_handle_id),
  constraint analysis_item_handle_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade
);

drop table if exists multi_analysis_item_filter_to_analysis_item_handle;
 CREATE TABLE multi_analysis_item_filter_to_analysis_item_handle (
  multi_analysis_item_filter_to_analysis_item_id bigint(20) NOT NULL AUTO_INCREMENT,
  filter_id bigint(20) NOT NULL,
  analysis_item_handle_id bigint(20) NOT NULL,
  PRIMARY KEY (multi_analysis_item_filter_to_analysis_item_id),
  CONSTRAINT multi_analysis_item_filter_to_analysis_item_handle_ibfk1 FOREIGN KEY (filter_id) REFERENCES filter (filter_id) ON DELETE CASCADE,
  CONSTRAINT multi_analysis_item_filter_to_analysis_item_handle_ibfk2 FOREIGN KEY (analysis_item_handle_id) REFERENCES analysis_item_handle (analysis_item_handle_id) ON DELETE CASCADE
);

drop table if exists selected_analysis_item_filter_to_analysis_item_handle;
 CREATE TABLE selected_analysis_item_filter_to_analysis_item_handle (
  selected_analysis_item_filter_to_analysis_item_id bigint(20) NOT NULL AUTO_INCREMENT,
  filter_id bigint(20) NOT NULL,
  analysis_item_handle_id bigint(20) NOT NULL,
  PRIMARY KEY (selected_analysis_item_filter_to_analysis_item_id),
  CONSTRAINT selected_analysis_item_filter_to_analysis_item_handle_ibfk1 FOREIGN KEY (filter_id) REFERENCES filter (filter_id) ON DELETE CASCADE,
  CONSTRAINT selected_analysis_item_filter_to_analysis_item_handle_ibfk2 FOREIGN KEY (analysis_item_handle_id) REFERENCES analysis_item_handle (analysis_item_handle_id) ON DELETE CASCADE
);

alter table flat_date_filter add all_option tinyint(4) not null default 0;