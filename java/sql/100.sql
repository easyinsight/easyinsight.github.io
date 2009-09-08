alter table highrise drop foreign key highrise_ibfk1;
alter table highrise add constraint highrise_ibfk1 FOREIGN KEY (feed_id) REFERENCES data_feed (data_feed_id) ON DELETE CASCADE;