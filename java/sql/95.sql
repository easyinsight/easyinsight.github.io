drop table if exists token;
create table token (
  token_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  token_type integer not null,
  token_value varchar(255) not null,
  primary key(token_id),
  key user_id (user_id),
  constraint token_ibfk1 foreign key (user_id) references user (user_id) on delete cascade
);