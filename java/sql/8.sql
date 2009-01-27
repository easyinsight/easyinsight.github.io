drop table if exists salesforce_subject_definition;
create table salesforce_subject_definition (
    salesforce_subject_definition_id integer auto_increment not null,
    data_feed_id integer not null,
    subject_type integer not null,
    primary key(salesforce_subject_definition_id)
);

drop table if exists formatting_configuration;
create table formatting_configuration (
    formatting_configuration_id integer auto_increment not null,
    formatting_type integer not null,
    text_uom varchar(100),
    primary key(formatting_configuration_id)
);

alter table analysis_item add formatting_configuration_id integer;