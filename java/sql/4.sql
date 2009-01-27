drop table if exists feed_to_analysis_item;
create table feed_to_analysis_item (
    feed_to_analysis_item_id integer auto_increment not null,
    feed_id integer not null,
    analysis_item_id integer not null,
    primary key(feed_to_analysis_item_id)
);

alter table analysis_item change key_name key_name varchar(255);