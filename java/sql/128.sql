# alter table heat_map add limits_metadata_id bigint(20) default null;
# alter table heat_map add constraint heat_map_ibfk3 foreign key (limits_metadata_id) references limits_metadata (limits_metadata_id) on delete cascade;

drop table if exists persona;
create table persona (
  persona_id bigint(20) auto_increment not null,
  persona_name varchar(255) not null,
  account_id bigint(20) not null,
  primary key(persona_id),
  constraint persona_id_ibfk1 foreign key (account_id) references account (account_id) on delete cascade
) ENGINE=InnoDB;

alter table user add persona_id bigint(20) default null;
alter table user add constraint user_ibfk5 foreign key (persona_id) references persona (persona_id) on delete set null;

drop table if exists ui_visibility_setting;
 create table ui_visibility_setting (
  ui_visibility_setting_id bigint(20) auto_increment not null,
  persona_id bigint(20) not null,
  config_element varchar(255) not null,
  visible tinyint(4) not null default 1,
  primary key(ui_visibility_setting_id)
) ENGINE=InnoDB;
