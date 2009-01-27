drop table if exists value;
create table value (
    value_id integer auto_increment not null,
    primary key(value_id)
);

drop table if exists string_value;
create table string_value (
    string_value_id integer auto_increment not null,
    value_id integer,
    string_content varchar(255) not null,
    primary key(string_value_id)
);

drop table if exists date_value;
create table date_value (
    date_value_id integer auto_increment not null,
    value_id integer,
    date_contet datetime not null,
    primary key(date_value_id)
);

drop table if exists numeric_value;
create table numeric_value (
    numeric_value_id integer auto_increment not null,
    value_id integer,
    numeric_content double not null,
    primary key(numeric_value_id)
);

drop table if exists empty_value;
create table empty_value (
    empty_value_id integer auto_increment not null,
    value_id integer,
    primary key(empty_value_id)
);

drop table if exists filter_to_value;
create table filter_to_value (
    filter_to_value_id integer auto_increment not null,
    filter_value_id integer,
    value_id integer,
    primary key(filter_to_value_id)
);