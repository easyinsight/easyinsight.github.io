drop table if exists file_process_create_scheduled_task;
create table file_process_create_scheduled_task (
  file_process_id bigint(20) auto_increment not null,
  task_id bigint(20) not null,
  upload_id bigint(20) not null,
  user_id bigint(20) not null,
  account_id bigint(20) not null,
  upload_name varchar(255) not null,
  primary key(file_process_id),
  constraint file_process_create_scheduled_task_ibfk1 foreign key (task_id) references scheduled_task(scheduled_task_id) on delete cascade,
  constraint file_process_create_scheduled_task_ibfk2 foreign key (upload_id) references user_upload(user_upload_id) on delete cascade,
  constraint file_process_create_scheduled_task_ibfk3 foreign key (user_id) references user(user_id) on delete cascade,
  constraint file_process_create_scheduled_task_ibfk4 foreign key (account_id) references account(account_id) on delete cascade
);

drop table if exists file_process_update_scheduled_task;
create table file_process_update_scheduled_task (
  file_process_id bigint(20) auto_increment not null,
  task_id bigint(20) not null,
  upload_id bigint(20) not null,
  user_id bigint(20) not null,
  account_id bigint(20) not null,
  update_flag boolean not null,
  feed_id bigint(20) not null,
  primary key(file_process_id),
  constraint file_process_update_scheduled_task_ibfk1 foreign key (task_id) references scheduled_task(scheduled_task_id) on delete cascade,
  constraint file_process_update_scheduled_task_ibfk2 foreign key (upload_id) references user_upload(user_upload_id) on delete cascade,
  constraint file_process_update_scheduled_task_ibfk3 foreign key (feed_id) references data_feed (data_feed_id) on delete cascade,
  constraint file_process_update_scheduled_task_ibfk4 foreign key (user_id) references user(user_id) on delete cascade,
  constraint file_process_update_scheduled_task_ibfk5 foreign key (account_id) references account(account_id) on delete cascade
);
