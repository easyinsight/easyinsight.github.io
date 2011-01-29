drop table if exists server;
create table server (
  server_id bigint(20) auto_increment not null,
  server_host varchar(255) not null,
  enabled tinyint(4) not null default 0,
  primary key (server_id)
);