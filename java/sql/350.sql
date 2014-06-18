

CREATE TABLE topo_map (
  topo_map_id bigint(20) NOT NULL AUTO_INCREMENT,
  report_state_id bigint(20) NOT NULL,
  PRIMARY KEY (topo_map_id),
  CONSTRAINT topo_map_ibfk1 FOREIGN KEY (report_state_id) REFERENCES report_state (report_state_id) ON DELETE CASCADE
) ENGINE=InnoDB;

create table oracle_salescloud (
  oracle_salescloud_id bigint(20) not null auto_increment,
  oracle_username varchar(255) default null,
  oracle_password varchar(255) default null,
  oracle_url varchar(255) default null,
  data_source_id bigint(20) not null,
  primary key (oracle_salescloud_id),
  constraint oracle_salescloud_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

alter table scheduled_task add task_type integer not null default 0;