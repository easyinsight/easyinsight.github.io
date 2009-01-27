drop table if exists dynamic_service_method;
create table dynamic_service_method (
    dynamic_service_method_id bigint auto_increment not null,
    method_name varchar(255) not null,
    dynamic_service_descriptor_id bigint not null,
    method_type integer not null,
    foreign key(dynamic_service_descriptor_id) REFERENCES dynamic_service_descriptor (dynamic_service_descriptor_id) ON DELETE CASCADE,
    primary key(dynamic_service_method_id)
);

drop table if exists dynamic_service_method_key;
create table dynamic_service_method_key (
    dynamic_service_method_key_id bigint auto_increment not null,
    dynamic_service_method_id bigint not null,
    analysis_item_id bigint not null,
    foreign key(dynamic_service_method_id) REFERENCES dynamic_service_method (dynamic_service_method_id) ON DELETE CASCADE,
    primary key(dynamic_service_method_key_id)
);