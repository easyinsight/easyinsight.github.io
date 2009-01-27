create table analysis_list (
    analysis_list_id integer auto_increment not null,
    analysis_dimension_id integer not null,
    delimiter varchar(10),
    expanded tinyint,
    primary key(analysis_list_id)
);