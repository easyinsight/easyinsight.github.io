drop table if exists rolling_range_filter;
create table rolling_range_filter (
    rolling_range_filter_id bigint auto_increment not null,
    interval_value integer not null,
    filter_id int(11) default NULL,
    FOREIGN KEY (filter_id) REFERENCES filter (filter_id) ON DELETE CASCADE,
    primary key(rolling_range_filter_id)
);