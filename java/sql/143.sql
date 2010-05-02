drop table group_to_feed_join;

alter table account add upgraded tinyint(4) not null default 0;

# alter table distributed_lock add lock_time bigint(20) not null default 0;

# alter table user add initial_setup_done tinyint(4) not null default 1;

drop table if exists email_audit;
create table email_audit (
  email_audit_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  email_category varchar(255) not null,
  primary key(email_audit_id),
  constraint email_audit_ibfk1 foreign key (user_id) references user (user_id) on delete cascade
);