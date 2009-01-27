# alter table map_definition add map_type integer;

create table dynamic_service_code (
    dynamic_service_code_id bigint auto_increment not null,
    feed_id integer,
    interface_bytecode blob,
    impl_bytecode blob,
    primary key(dynamic_service_code_id)
);

create table dynamic_service_descriptor (
    dynamic_service_descriptor_id bigint auto_increment not null,
    feed_id integer,    
    primary key(dynamic_service_descriptor_id)
);

alter table data_feed add dynamic_service_definition_id bigint default 0; 