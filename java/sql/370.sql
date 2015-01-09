alter table basecamp_next add skip_calendar tinyint(4) not null default 0;

drop table if exists sequence_to_scheduled_activity;
create table sequence_to_scheduled_activity (
  sequence_to_scheduled_activity_id bigint(20) auto_increment not null,
  parent_activity_id bigint(20) not null,
  child_activity_id bigint(20) not null,
  activity_index tinyint(4) not null,
  primary key (sequence_to_scheduled_activity_id),
  constraint sequence_to_scheduled_activity_ibfk1 foreign key (parent_activity_id) references scheduled_account_activity (scheduled_account_activity_id) on delete cascade,
  constraint sequence_to_scheduled_activity_ibfk2 foreign key (child_activity_id) references scheduled_account_activity (scheduled_account_activity_id) on delete cascade
);

alter table async_report_request add task_id bigint(20) default null;
alter table async_report_request add cache_source_id bigint(20) default null;

create table activity_sequence_task (
  activity_sequence_task_id bigint(20) auto_increment not null,
  scheduled_task_id bigint(20) not null,
  scheduled_activity_id bigint(20) not null,
  primary key (activity_sequence_task_id),
  constraint activity_sequence_task_ibfk1 foreign key (scheduled_task_id) references scheduled_task (scheduled_task_id) on delete  cascade
);

create table activity_sequence_task_generator (
  activity_sequence_task_generator_id bigint(20) auto_increment not null,
  task_generator_id bigint(20) not null,
  scheduled_activity_id bigint(20) not null,
  primary key (activity_sequence_task_generator_id),
  constraint activity_sequence_task_generator_ibfk1 foreign key (task_generator_id) references task_generator (task_generator_id) on delete cascade
);