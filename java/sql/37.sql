drop table if exists database_version;
create table database_version (
  database_version_id bigint(20) not null auto_increment,
  version int(11) not null default 37,
  primary key(database_version_id)
);