drop table if exists server_refresh_task;
create table server_refresh_task (
  server_refresh_task_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  user_id bigint(20) not null,
  scheduled_task_id bigint(20) not null,
  primary key(server_refresh_task_id),
  key scheduled_task_id (scheduled_task_id),
  key data_source_id (data_source_id),
  constraint server_refresh_task_ibfk1 foreign key (scheduled_task_id) references scheduled_task (scheduled_task_id) on delete cascade,
  constraint server_refresh_task_ibfk2 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade,
  constraint server_refresh_task_ibfk3 foreign key (user_id) references user(user_id) on delete cascade
);
