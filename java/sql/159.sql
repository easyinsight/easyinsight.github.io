drop table if exists user_session;
create table user_session (
  user_session_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  session_number varchar(255) not null,
  user_session_date datetime not null,
  primary key(user_session_id),
  constraint user_session_ibfk1 foreign key (user_id) references user (user_id) on delete cascade
);