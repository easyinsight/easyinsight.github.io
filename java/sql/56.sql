drop table if exists treemap_report;
create table treemap_report (
  treemap_report_id bigint(20) NOT NULL auto_increment,
  report_state_id bigint(20) default NULL,
  PRIMARY KEY  (treemap_report_id),
  KEY report_state_id (report_state_id),
  CONSTRAINT treemap_report_ibfk_1 FOREIGN KEY (report_state_id) REFERENCES report_state (report_state_id) ON DELETE CASCADE
);

drop table if exists tree_report;
create table tree_report (
  tree_report_id bigint(20) NOT NULL auto_increment,
  report_state_id bigint(20) default NULL,
  PRIMARY KEY  (tree_report_id),
  KEY report_state_id (report_state_id),
  CONSTRAINT tree_report_ibfk_1 FOREIGN KEY (report_state_id) REFERENCES report_state (report_state_id) ON DELETE CASCADE
);