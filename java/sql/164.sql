drop table if exists freshbooks;
create table freshbooks (
  freshbooks_id bigint(20) auto_increment not null,
  url varchar(255) not null,
  token_key varchar(255) not null,
  token_secret_key varchar(255) not null,
  data_source_id bigint(20) not null,
  primary key(freshbooks_id),
  constraint freshbooks_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists wow_armory;
create table wow_armory (
  wow_armory_id bigint(20) auto_increment not null,
  server varchar(255) not null,
  guild_name varchar(255) not null,
  data_source_id bigint(20) not null,
  primary key (wow_armory_id),
  constraint wow_armory_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);