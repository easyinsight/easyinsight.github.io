drop table if exists user_action_audit;
create table user_action_audit (
  user_action_audit_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  action_name varchar(255) not null,
  action_date datetime not null,
  primary key(user_action_audit_id),
  constraint user_action_audit_ibfk1 foreign key (user_id) references user (user_id)
);