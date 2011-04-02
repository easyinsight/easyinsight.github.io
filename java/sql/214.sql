drop table if exists external_login;
create table external_login (
  external_login_id bigint(20) auto_increment not null,
  primary key (external_login_id)
) ENGINE=InnoDB ;

drop table if exists quickbase_external_login;
create table quickbase_external_login (
  quickbase_external_login_id bigint(20) auto_increment not null,
  external_login_id bigint(20) not null,
  host_name varchar(255) not null,
  primary key (quickbase_external_login_id),
  constraint quickbase_external_login_ibfk1 foreign key (external_login_id) references external_login (external_login_id)
) ENGINE=InnoDB ;

alter table account add external_login_id bigint(20) default null;
alter table account add constraint user_ibfk6 foreign key (external_login_id) references external_login (external_login_id) on delete cascade;

drop table if exists federated_field_mapping;
create table federated_field_mapping (
  federated_field_mapping_id bigint(20) auto_increment not null,
  federated_key varchar(255) default null,
  source_key varchar(255) default null,
  data_source_id bigint(20) not null,
  federated_data_source_id bigint(20) not null,
  primary key (federated_field_mapping_id),
  constraint federated_field_mapping_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade,
  constraint federated_field_mapping_ibfk2 foreign key (federated_data_source_id) references data_feed (data_feed_id) on delete cascade
) ENGINE=InnoDB;