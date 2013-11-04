drop table if exists saved_configuration;
create table saved_configuration (
  saved_configuration_id bigint(20) auto_increment not null,
  dashboard_state_id bigint(20) not null,
  url_key varchar(255) not null,
  configuration_name varchar(255) not null,
  primary key (saved_configuration_id),
  constraint saved_configuration_ibfk1 foreign key (dashboard_state_id) references dashboard_state (dashboard_state_id) on delete cascade
);