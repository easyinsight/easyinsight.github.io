drop table if exists feed_persistence_metadata;
create table feed_persistence_metadata (
    feed_persistence_metadata_id integer auto_increment not null,
    feed_id integer,
    size integer,
    version integer,
    primary key(feed_persistence_metadata_id)
);

drop table if exists feed_persistence_metadata_bucket;
create table feed_persistence_metadata_bucket (
    feed_persistence_metadata_bucket_id integer auto_increment not null,
    feed_persistence_metadata_id integer,
    key_id integer,
    bucket_name varchar(100),
    primary key(feed_persistence_metadata_bucket_id)
);

DROP TABLE IF EXISTS tag_cloud_to_tag;
CREATE TABLE tag_cloud_to_tag (
  tag_cloud_to_tag_id int(11) NOT NULL auto_increment,
  tag_cloud_id int(11),
  analysis_tags_id int(11),
  FOREIGN KEY (tag_cloud_id) REFERENCES tag_cloud (tag_cloud_id) ON DELETE CASCADE,
  FOREIGN KEY (analysis_tags_id) REFERENCES analysis_tags (analysis_tags_id) ON DELETE CASCADE,
  PRIMARY KEY  (tag_cloud_to_tag_id)
) ENGINE=InnoDB AUTO_INCREMENT=439 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS feed_to_tag;
CREATE TABLE feed_to_tag (
  feed_to_tag_id int(11) NOT NULL auto_increment,
  feed_id int(11),
  analysis_tags_id int(11),
  FOREIGN KEY (feed_id) REFERENCES data_feed (data_feed_id) ON DELETE CASCADE,
  FOREIGN KEY (analysis_tags_id) REFERENCES analysis_tags (analysis_tags_id) ON DELETE CASCADE,
  PRIMARY KEY  (feed_to_tag_id)
) ENGINE=InnoDB AUTO_INCREMENT=439 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS analysis_to_tag;
CREATE TABLE analysis_to_tag (
  analysis_to_tag_id int(11) NOT NULL auto_increment,
  analysis_id int(11),
  analysis_tags_id int(11),
  FOREIGN KEY (analysis_id) REFERENCES analysis (analysis_id) ON DELETE CASCADE,
  FOREIGN KEY (analysis_tags_id) REFERENCES analysis_tags (analysis_tags_id) ON DELETE CASCADE,
  PRIMARY KEY  (analysis_to_tag_id)
) ENGINE=InnoDB AUTO_INCREMENT=439 DEFAULT CHARSET=latin1;

alter table analysis drop tag_cloud_id;