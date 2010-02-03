drop table if exists kpi_role;
create table kpi_role (
  kpi_role_id bigint(20) auto_increment not null,
  user_id bigint(20) default null,
  group_id bigint(20) default null,
  kpi_id bigint(20) not null,
  owner tinyint(4) not null default 0,
  responsible tinyint(4) not null default 0,
  primary key(kpi_role_id),
  constraint kpi_role_ibfk1 foreign key (kpi_id) references kpi (kpi_id) on delete cascade,
  constraint kpi_role_ibfk2 foreign key (user_id) references user (user_id) on delete cascade,
  constraint kpi_role_ibfk3 foreign key (group_id) references community_group (community_group_id) on delete cascade
);