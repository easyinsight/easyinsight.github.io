alter table milestone drop foreign key milestone_ibfk1;
alter table milestone add constraint milestone_ibfk1 foreign key (account_id) references account (account_id) on delete cascade;

drop table if exists solution_to_feed;
CREATE TABLE `solution_to_feed` (
  `solution_to_feed_id` bigint(20) NOT NULL auto_increment,
  `feed_id` bigint(20) NOT NULL,
  `solution_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`solution_to_feed_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
alter table solution_to_feed add constraint solution_to_feed_ibfk1 foreign key (solution_id) references solution (solution_id) on delete cascade;
alter table solution_to_feed add constraint solution_to_feed_ibfk2 foreign key (feed_id) references data_feed (data_feed_id) on delete cascade;

alter table solution_tag drop foreign key solution_tag_ibfk1;
alter table solution_tag add constraint solution_tag_ibfk1 foreign key (solution_id) references solution (solution_id) on delete cascade;