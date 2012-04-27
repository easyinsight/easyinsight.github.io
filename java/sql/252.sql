alter table join_override add marmot_script text default null;

alter table data_feed add refresh_marmot_script text default null;

drop table if exists basecamp_next;
create table basecamp_next (
  basecamp_next_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  access_token text default null,
  refresh_token text default null,
  endpoint varchar(255) default null,
  primary key (basecamp_next_id),
  constraint basecamp_next_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);