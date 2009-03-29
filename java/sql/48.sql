drop table if exists report_state;
create table report_state (
  report_state_id bigint(20) auto_increment not null,
  analysis_id bigint(20) not null,
  primary key(report_state_id),
  key analysis_id (analysis_id),
  constraint report_state_ibfk1 foreign key (analysis_id) references analysis (analysis_id)
);

DROP TABLE IF EXISTS chart_report;
CREATE TABLE chart_report (
  chart_report_id bigint(20) NOT NULL auto_increment,
  report_state_id bigint(20) default NULL,
  chart_type int(11) default NULL,
  chart_family int(11) default NULL,
  limits_metadata_id bigint(20) default NULL,
  PRIMARY KEY  (chart_report_id),
  KEY report_state_id (report_state_id),
  KEY limits_metadata_id (limits_metadata_id),
  CONSTRAINT chart_report_ibfk_1 FOREIGN KEY (report_state_id) REFERENCES report_state (report_state_id) ON DELETE CASCADE,
  CONSTRAINT chart_report_ibfk_2 FOREIGN KEY (limits_metadata_id) REFERENCES limits_metadata (limits_metadata_id)
);

DROP TABLE IF EXISTS crosstab_report;
CREATE TABLE crosstab_report (
  crosstab_report_id bigint(20) NOT NULL auto_increment,
  report_state_id bigint(20) default NULL,
  PRIMARY KEY  (crosstab_report_id),
  KEY report_state_id (report_state_id),
  CONSTRAINT crosstab_report_ibfk_1 FOREIGN KEY (report_state_id) REFERENCES report_state (report_state_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS list_report;
CREATE TABLE list_report (
  list_report_id bigint(20) NOT NULL auto_increment,
  report_state_id bigint(20) default NULL,
  list_limits_metadata_id bigint(20) default NULL,
  show_row_numbers tinyint(4) default '0',
  PRIMARY KEY  (list_report_id),
  KEY report_state_id (report_state_id),
  CONSTRAINT list_report_ibfk_1 FOREIGN KEY (report_state_id) REFERENCES report_state (report_state_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS map_report;
CREATE TABLE map_report (
  map_report_id bigint(20) NOT NULL auto_increment,
  report_state_id bigint(20) default NULL,
  map_type int(11) default NULL,
  PRIMARY KEY  (map_report_id),
  KEY report_state_id (report_state_id),
  KEY graphic_definition_id (graphic_definition_id),
  CONSTRAINT map_report_ibfk_1 FOREIGN KEY (report_state_id) REFERENCES report_state (report_state_id) ON DELETE CASCADE
);

alter table analysis add report_state_id bigint(20);