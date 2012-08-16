drop table if exists authentication_log;
create table authentication_log(
  authentication_log_id bigint(20) auto_increment not null,
  username VARCHAR(255) not null,
  user_id bigint(20),
  success tinyint(4),
  login_time datetime not null,
  ip_address varchar(255),
  login_type int,
  user_agent varchar(255),
  primary key (authentication_log_id),
  constraint authentication_log_ibfk1 foreign key (user_id) references user(user_id)
);