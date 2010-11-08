drop table if exists constant_contact;
create table constant_contact (
  constant_contact_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  username varchar(255) default null,
  token_key varchar(255) default null,
  token_secret_key varchar(255) default null,
  primary key(constant_contact_id),
  constraint constant_constact_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade 
);