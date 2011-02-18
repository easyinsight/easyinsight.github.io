update data_feed set feed_name = 'FreshBooks' where feed_name = 'Freshbooks';

delete from link where label = 'View Todo List in Basecamp';

alter table scorecard add creation_date datetime default null;
alter table scorecard add update_date datetime default null;

