drop table if exists filter_to_value;
create table filter_to_value (
    filter_to_value_id integer auto_increment not null,
    filter_id integer,
    value_id integer,
    FOREIGN KEY (filter_id) REFERENCES filter (filter_id) ON DELETE CASCADE,
    FOREIGN KEY (value_id) REFERENCES value (value_id) ON DELETE CASCADE,
    primary key(filter_to_value_id)
);