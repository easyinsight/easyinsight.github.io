drop table if exists data_scrub;
create table data_scrub (
    data_scrub_id integer auto_increment not null,
    analysis_id integer,
    primary key(data_scrub_id)
);

drop table if exists text_replace_scrub;
create table text_replace_scrub(
    text_replace_scrub_id integer auto_increment not null,
    data_scrub_id integer,
    source_text varchar(255),
    target_text varchar(255),
    regex tinyint,
    case_sensitive tinyint,
    primary key (text_replace_scrub_id)
);

drop table if exists lookup_table_scrub;
create table lookup_table_scrub (
    lookup_table_scrub_id integer auto_increment not null,
    data_scrub_id integer,
    source_key integer,
    target_key integer,
    primary key (lookup_table_scrub_id)
);

drop table if exists lookup_table_scrub_pair;
create table lookup_table_scrub_pair (
    lookup_table_scrub_pair_id integer auto_increment not null,
    source_value varchar(255),
    target_value varchar(255),
    data_scrub_id integer,
    primary key(lookup_table_scrub_pair_id)
);

drop table if exists additional_items;
create table additional_items (
    additional_items_id integer auto_increment not null,
    analysis_id integer,
    analysis_item_id integer,
    primary key(additional_items_id)
);