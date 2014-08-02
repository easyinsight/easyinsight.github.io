CREATE TABLE multi_summary (
  multi_summary_id bigint(20) NOT NULL AUTO_INCREMENT,
  report_state_id bigint(20) NOT NULL,
  PRIMARY KEY (multi_summary_id),
  CONSTRAINT multi_summary_ibfk1 FOREIGN KEY (report_state_id) REFERENCES report_state (report_state_id) ON DELETE CASCADE
) ENGINE=InnoDB;

drop table if exists multi_summary_to_report;
CREATE TABLE multi_summary_to_report (
  multi_summary_to_report_id bigint(20) not null auto_increment,
  source_report_id bigint(20) not null,
  report_stub_id bigint(20) not null,
  primary key (multi_summary_to_report_id),
  CONSTRAINT multi_summary_to_report_ibfk1 FOREIGN KEY (source_report_id) REFERENCES multi_summary (report_state_id) ON DELETE CASCADE,
  CONSTRAINT multi_summary_to_report_ibfk2 FOREIGN KEY (report_stub_id) REFERENCES report_stub (report_stub_id) ON DELETE CASCADE
);