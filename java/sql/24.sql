DROP TABLE IF EXISTS composite_feed;
CREATE TABLE composite_feed (
  composite_feed_id int(11) NOT NULL auto_increment,
  data_feed_id int(11) default NULL,
  FOREIGN KEY (data_feed_id) REFERENCES data_feed (data_feed_id) ON DELETE CASCADE,
  PRIMARY KEY  (composite_feed_id)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

--
-- Table structure for table composite_connection
--

DROP TABLE IF EXISTS composite_connection;
CREATE TABLE composite_connection (
  composite_connection_id int(11) NOT NULL auto_increment,
  composite_feed_id int(11) NOT NULL,
  source_feed_node_id int(11) NOT NULL,
  target_feed_node_id int(11) NOT NULL,
  source_join int(11) NOT NULL,
  target_join int(11) NOT NULL,
  PRIMARY KEY  (composite_connection_id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Table structure for table composite_node
--

DROP TABLE IF EXISTS composite_node;
CREATE TABLE composite_node (
  composite_node_id int(11) NOT NULL auto_increment,
  composite_feed_id int(11) NOT NULL,
  data_feed_id int(11) NOT NULL,
  PRIMARY KEY  (composite_node_id)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;