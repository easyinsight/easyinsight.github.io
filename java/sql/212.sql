alter table solution add data_source_type integer default null;
alter table solution drop description;
alter table solution drop author;
alter table solution drop footer_text;
alter table solution drop screencast_directory;
alter table solution drop screencast_mp4_name;
alter table solution drop goal_tree_id;

drop table if exists zendesk;
create table zendesk (
  zendesk_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  zendesk_username varchar(255) default null,
  zendesk_password varchar(255) default null,
  url varchar(255) default null,
  primary key (zendesk_id),
  constraint zendesk_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

alter table named_item_key add indexed tinyint(4) not null default 0;