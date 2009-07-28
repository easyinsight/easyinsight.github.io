drop table if exists password_reset;
create table password_reset (
  password_reset_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  request_date datetime not null,
  password_request_string varchar(255) not null,
  primary key(password_reset_id),
  key user_id (user_id),
  constraint password_reset_unique1 unique (password_request_string),
  constraint password_reset_ibfk1 foreign key (user_id) references user(user_id) on delete cascade
);