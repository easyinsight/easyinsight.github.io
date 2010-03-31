alter table account add marketplace_enabled tinyint(4) not null default 1;
alter table account add public_data_enabled tinyint(4) not null default 1;
alter table account add report_share_enabled tinyint(4) not null default 1;

drop table if exists marketo_data_source;
create table marketo_data_source (
  marketo_data_source_id bigint(20) auto_increment not null,
  data_feed_id bigint(20) not null,
  primary key(marketo_data_source_id),
  constraint marketo_data_source_ibfk1 foreign key (data_feed_id) references data_feed (data_feed_id) on delete cascade
);