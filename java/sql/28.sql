drop table if exists time_based_analysis_measure;
create table time_based_analysis_measure (
    time_based_analysis_measure_id bigint auto_increment not null,
    analysis_item_id bigint,
    date_dimension bigint,
    wrapped_aggregation integer,
    primary key(time_based_analysis_measure_id)    
);

drop table if exists complex_analysis_measure;
create table complex_analysis_measure (
    complex_analysis_measure_id bigint auto_increment not null,
    analysis_item_id bigint,
    wrapped_aggregation integer,
    primary key(complex_analysis_measure_id)
);