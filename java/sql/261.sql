drop table if exists batchbook_custom_field_source;
create table batchbook_custom_field_source (
  batchbook_custom_field_source_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  custom_field_id varchar(255) default null,
  primary key (batchbook_custom_field_source_id),
  constraint batchbook_custom_field_source_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

alter table analysis_date add date_time_field tinyint(4) not null default 0;

# alter table account add days_over_size_boundary integer not null default 0;
# alter table account add max_days_over_size_boundary integer not null default 0;

alter table data_feed add refresh_behavior integer not null default 0;
update data_feed set refresh_behavior = 3 where feed_type = 1;
update data_feed set refresh_behavior = 4 where feed_type = 3;
update data_feed set refresh_behavior = 3 where feed_type = 6;
update data_feed set refresh_behavior = 3 where feed_type = 13;
update data_feed set refresh_behavior = 5 where feed_type = 18;
update data_feed set refresh_behavior = 3 where feed_type = 21;
update data_feed set refresh_behavior = 5 where feed_type = 23;
update data_feed set refresh_behavior = 2 where feed_type = 34;
update data_feed set refresh_behavior = 3 where feed_type = 35;
update data_feed set refresh_behavior = 2 where feed_type = 39;

update data_feed, freshbooks set refresh_behavior = 5 where data_feed.data_feed_id = freshbooks.data_source_id and freshbooks.live_data_source = 0 and feed_type = 49;
update data_feed, freshbooks set refresh_behavior = 3 where data_feed.data_feed_id = freshbooks.data_source_id and freshbooks.live_data_source = 1 and feed_type = 49;

update data_feed set refresh_behavior = 5 where feed_type = 63;
update data_feed set refresh_behavior = 5 where feed_type = 72;
update data_feed set refresh_behavior = 3 where feed_type = 80;
update data_feed set refresh_behavior = 5 where feed_type = 82;
update data_feed set refresh_behavior = 5 where feed_type = 93;
update data_feed set refresh_behavior = 5 where feed_type = 97;
update data_feed set refresh_behavior = 5 where feed_type = 135;
update data_feed set refresh_behavior = 5 where feed_type = 141;
update data_feed set refresh_behavior = 5 where feed_type = 145;
