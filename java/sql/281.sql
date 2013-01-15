DROP TABLE IF EXISTS account_sync_task_generator;
CREATE TABLE account_sync_task_generator(
  account_sync_generator_id bigint(20) auto_increment not null,
  task_generator_id bigint(20) not null,
  primary key(account_sync_generator_id),
  key (task_generator_id),
  constraint account_sync_generator_ibfk1 foreign key (task_generator_id) references task_generator (task_generator_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS account_sync_scheduled_task;
CREATE TABLE account_sync_scheduled_task (
  billing_scheduled_task_id bigint(20) auto_increment not null,
  scheduled_task_id bigint(20) not null,
  primary key(billing_scheduled_task_id),
  constraint account_sync_task_ibfk1 foreign key (scheduled_task_id) references scheduled_task (scheduled_task_id) ON DELETE CASCADE
);
