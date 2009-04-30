alter table account_activity add max_users integer not null default 0;
alter table account_activity add max_size bigint(20) not null default 0;

alter table account add unique (name);

alter table group_comment drop foreign key group_comment_ibfk1;
alter table group_comment add constraint group_comment_ibfk1 foreign key (group_id) references community_group (community_group_id) on delete cascade;
alter table group_comment drop foreign key group_comment_ibfk2;
alter table group_comment add constraint group_comment_ibfk2 foreign key (user_id) references user (user_id) on delete cascade;

alter table group_audit_message drop foreign key group_audit_message_ibfk1;
alter table group_audit_message add constraint group_audit_message_ibfk1 foreign key (group_id) references community_group (community_group_id) on delete cascade;
alter table group_audit_message drop foreign key group_audit_message_ibfk2;
alter table group_audit_message add constraint group_audit_message_ibfk2 foreign key (user_id) references user (user_id) on delete cascade;

alter table data_source_audit_message drop foreign key data_source_audit_message_ibfk1;
alter table data_source_audit_message add constraint data_source_audit_message_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade;
alter table data_source_audit_message drop foreign key data_source_audit_message_ibfk2;
alter table data_source_audit_message add constraint data_source_audit_message_ibfk2 foreign key (user_id) references user (user_id) on delete cascade;

alter table user add unique (email);
alter table user add unique (username);

alter table account_activation add unique (activation_key);

alter table account_activity drop foreign key account_activity_ibfk1;
alter table account_activity add constraint account_activity_ibfk1 foreign key (account_id) references account (account_id) on delete cascade;

drop table if exists account_timed_state;
create table account_timed_state (
  account_timed_state_id bigint(20) auto_increment not null,
  account_id bigint(20) not null,
  state_change_time datetime not null,
  account_state integer not null,
  primary key(account_timed_state_id)
);

drop table if exists account_time_task;
create table account_time_task (
  account_time_task_id bigint(20) auto_increment not null,
  scheduled_task_id bigint(20) not null,
  primary key(account_time_task_id),
  key scheduled_task_id (scheduled_task_id),
  constraint account_time_task_ibfk1 foreign key (scheduled_task_id) references scheduled_task (scheduled_task_id) on delete cascade
);

drop table if exists account_billing_task;
create table account_billing_task (
  account_billing_task_id bigint(20) auto_increment not null,
  scheduled_task_id bigint(20) not null,
  primary key(account_billing_task_id),
  key scheduled_task_id (scheduled_task_id),
  constraint account_billing_task_ibfk1 foreign key (scheduled_task_id) references scheduled_task (scheduled_task_id) on delete cascade
);

drop table if exists db_snapshot_task;
create table db_snapshot_task (
  db_snapshot_task_id bigint(20) auto_increment not null,
  scheduled_task_id bigint(20) not null,
  primary key(db_snapshot_task_id),
  key scheduled_task_id (scheduled_task_id),
  constraint db_snapshot_task_ibfk1 foreign key (scheduled_task_id) references scheduled_task (scheduled_task_id) on delete cascade
);

drop table if exists account_time_scheduler;
create table account_time_scheduler (
  account_time_scheduler_id bigint(20) auto_increment not null,
  task_generator_id bigint(20) not null,
  primary key(account_time_scheduler_id),
  key task_generator_id (task_generator_id),
  constraint account_time_scheduler_ibfk1 foreign key (task_generator_id) references task_generator (task_generator_id) on delete cascade
);

drop table if exists db_snapshot_scheduler;
create table db_snapshot_scheduler (
  db_snapshot_scheduler_id bigint(20) auto_increment not null,
  task_generator_id bigint(20) not null,
  primary key(db_snapshot_scheduler_id),
  key task_generator_id (task_generator_id),
  constraint db_snapshot_scheduler_ibfk1 foreign key (task_generator_id) references task_generator (task_generator_id) on delete cascade
);

update account set account_type = 6 where account_type = 5;
update account set account_type = 5 where account_type = 4;
update account set account_type = 4 where account_type = 3;
update account set account_state = 9;
