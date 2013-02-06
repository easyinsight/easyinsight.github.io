drop table if exists reactivatable_user;

create table reactivatable_user (
  reactivatable_user_id bigint(20) auto_increment not null,
  old_user_id bigint(20) not null,
  email varchar(255) not null,
  first_name varchar(255) not null,
  last_name varchar(255) not null,
  company varchar(255) not null,
  reactivation_key varchar(255) not null,
  opt_out tinyint(4) not null default 0,
  primary key(reactivatable_user_id),
  key(reactivation_key),
  constraint reactivatable_user_ibfk1 foreign key(old_user_id) references user(user_id) on delete cascade
);
