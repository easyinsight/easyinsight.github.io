drop table if exists graphic_definition;
create table graphic_definition (
    graphic_definition_id integer auto_increment not null,
    analysis_id int(11) default NULL,
    primary key(graphic_definition_id)
);

drop table if exists yahoo_map_definition;
create table yahoo_map_definition (
    yahoo_map_definition_id integer auto_increment not null,
    analysis_id int(11),
    graphic_definition_id int(11),
    primary key(yahoo_map_definition_id)
);

drop table if exists map_definition;
create table map_definition (
    map_definition_id integer auto_increment not null,
    analysis_id int(11),
    graphic_definition_id int(11),
    primary key(map_definition_id)
);