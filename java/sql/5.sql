drop table if exists item_key;
create table item_key (
    item_key_id integer auto_increment not null,
    primary key(item_key_id)
);

drop table if exists named_item_key;
create table named_item_key (
    named_item_key_id integer auto_increment not null,
    name varchar(100) not null,
    item_key_id integer not null,
    primary key(named_item_key_id)
);

drop table if exists derived_item_key;
create table derived_item_key (
    derived_item_key_id integer auto_increment not null,
    item_key_id integer not null,
    parent_item_key_id integer not null,
    feed_id integer not null,
    primary key(derived_item_key_id)
);