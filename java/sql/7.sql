drop table if exists measure_condition;

create table measure_condition (
    measure_condition_id integer auto_increment not null,
    low_color integer not null,
    low_value integer not null,
    high_color integer not null,
    high_value integer not null,
    primary key(measure_condition_id)
);

drop table if exists measure_condition_range;
create table measure_condition_range (
    measure_condition_range_id integer auto_increment not null,
    low_measure_condition_id integer,
    high_measure_condition_id integer,
    value_range_type integer not null,
    primary key(measure_condition_range_id)
);

alter table analysis_measure add measure_condition_range_id integer; 