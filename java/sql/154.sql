drop table if exists delivery_schedule;
create table delivery_schedule (
  delivery_schedule_id bigint(20) auto_increment not null,
  scheduled_account_activity_id bigint(20) not null,
  primary key(delivery_schedule_id),
  constraint delivery_schedule_ibfk1 foreign key (scheduled_account_activity_id) references scheduled_account_activity (scheduled_account_activity_id)
);

drop table if exists delivery_to_user;
create table delivery_to_user (
  delivery_to_user_id bigint(20) auto_increment not null,
  scheduled_account_activity_id bigint(20) not null,
  user_id bigint(20) not null,
  primary key (delivery_to_user_id),
  constraint delivery_to_user_ibfk1 foreign key (scheduled_account_activity_id) references scheduled_account_activity (scheduled_account_activity_id) on delete cascade,
  constraint delivery_to_user_ibfk2 foreign key (user_id) references user (user_id) on delete cascade
);

drop table if exists report_delivery;
create table report_delivery (
  report_delivery_id bigint(20) auto_increment not null,
  report_id bigint(20) not null,
  delivery_format integer not null,
  body text not null,
  subject varchar(255) not null,
  scheduled_account_activity_id bigint(20) not null,
  primary key (report_delivery_id),
  constraint report_delivery_ibfk1 foreign key (report_id) references analysis (analysis_id) on delete cascade,
  constraint report_delivery_ibfk2 foreign key (scheduled_account_activity_id) references scheduled_account_activity (scheduled_account_activity_id) on delete cascade
);

drop table if exists delivery_task_generator;
create table delivery_task_generator (
  delivery_task_generator_id bigint(20) auto_increment not null,
  task_generator_id bigint(20) not null,
  scheduled_account_activity_id bigint(20) not null,
  primary key(delivery_task_generator_id),
  constraint delivery_task_generator_ibfk1 foreign key (task_generator_id) references task_generator (task_generator_id) on delete cascade,
  constraint delivery_task_generator_ibfk2 foreign key (scheduled_account_activity_id) references scheduled_account_activity (scheduled_account_activity_id) on delete cascade
);

drop table if exists delivery_scheduled_task;
create table delivery_scheduled_task (
  delivery_scheduled_task_id bigint(20) auto_increment not null,
  scheduled_account_activity_id bigint(20) not null,
  scheduled_task_id bigint(20) not null,
  primary key(delivery_scheduled_task_id),
  constraint delivery_scheduled_task_ibfk1 foreign key (scheduled_account_activity_id) references scheduled_account_activity (scheduled_account_activity_id) on delete cascade,
  constraint delivery_scheduled_task_ibfk2 foreign key (scheduled_task_id) references scheduled_task (scheduled_task_id) on delete cascade 
);