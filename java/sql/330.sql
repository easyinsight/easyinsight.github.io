CREATE TABLE solve360 (
  solve360_id bigint(20) NOT NULL AUTO_INCREMENT,
  data_source_id bigint(20) NOT NULL,
  user_email varchar(255) DEFAULT NULL,
  auth_key varchar(255) DEFAULT NULL,
  PRIMARY KEY (solve360_id),
  KEY solve360_ibfk1 (data_source_id),
  CONSTRAINT solve360_ibfk1 FOREIGN KEY (data_source_id) REFERENCES data_feed (data_feed_id) ON DELETE CASCADE
)