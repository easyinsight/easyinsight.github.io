alter table token add data_source_id bigint(20) default null;
alter table token add constraint token_ibfk3 foreign key (data_source_id) REFERENCES data_feed (data_feed_id) ON DELETE CASCADE;
alter table token change user_id user_id bigint(20) default null;