DROP TABLE IF EXISTS bandwidth_usage;
CREATE TABLE bandwidth_usage (
  bandwidth_usage_id bigint(20) NOT NULL auto_increment,
  account_id bigint(20) default NULL,
  used_bandwidth bigint(20) NOT NULL DEFAULT 0,
  bandwidth_date DATE NOT NULL,
  PRIMARY KEY  (bandwidth_usage_id),
  KEY account_id(account_id),
  CONSTRAINT bandwidth_usage_ibfk_1 FOREIGN KEY (account_id) REFERENCES account (account_id) ON DELETE CASCADE
);