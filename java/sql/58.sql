drop table if exists scheduled_task;
create table scheduled_task (
  scheduled_task_id bigint(20) auto_increment not null,
  status integer not null,
  scheduled_time datetime not null,
  started_time datetime default null,
  stopped_time datetime default null,
  task_generator_id bigint(20) not null,
  primary key(scheduled_task_id)
);

drop table if exists data_source_query_task;
create table data_source_query (
  data_source_query_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  scheduled_task_id bigint(20) not null,
  primary key(data_source_query_id),
  key scheduled_task_id (scheduled_task_id),
  key data_source_id (data_source_id),
  constraint data_source_query_task_ibfk1 foreign key (scheduled_task_id) references scheduled_task (scheduled_task_id) on delete cascade,
  constraint data_source_query_task_ibfk2 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists task_generator;
create table task_generator (
  task_generator_id bigint(20) auto_increment not null,
  task_interval integer not null,
  last_task_date datetime default null,
  start_task_date datetime not null,
  primary key (task_generator_id)
);

drop table if exists data_source_task_generator;
create table data_source_task_generator (
  data_source_task_generator_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  task_generator_id bigint(20) not null,
  primary key(data_source_task_generator_id),
  key data_source_id (data_source_id),
  key task_generator_id (task_generator_id),
  constraint data_source_task_generator_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade,
  constraint data_source_task_generator_ibfk2 foreign key (task_generator_id) references task_generator (task_generator_id) on delete cascade
);

drop table if exists distributed_lock;
create table distributed_lock (
  distributed_lock_id bigint(20) auto_increment not null,
  lock_name varchar(100),
  primary key(distributed_lock_id),
  unique (lock_name)
);