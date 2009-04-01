DROP TABLE IF EXISTS gauge_report;
CREATE TABLE gauge_report (
  gauge_report_id bigint(20) NOT NULL auto_increment,
  report_state_id bigint(20) default NULL,
  gauge_type integer NOT NULL,
  max_value integer NOT NULL,
  PRIMARY KEY  (gauge_report_id),
  KEY report_state_id (report_state_id),
  CONSTRAINT gauge_report_ibfk_1 FOREIGN KEY (report_state_id) REFERENCES report_state (report_state_id) ON DELETE CASCADE
);

alter table analysis add report_type integer(11) not null default 1;