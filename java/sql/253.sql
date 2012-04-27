alter table analysis_item add key_column tinyint(4) not null default 0;
alter table analysis_item add label_column tinyint(4) not null default 0;

alter table analysis_calculation add cached_calculation tinyint(4) not null default 0;

drop table if exists archive_task_generator;
create table archive_task_generator (
  archive_task_generator_id bigint(20) auto_increment not null,
  task_generator_id bigint(20) not null,
  primary key (archive_task_generator_id),
  constraint archive_task_generator_ibfk1 foreign key (task_generator_id) references task_generator (task_generator_id) on delete cascade
);

drop table if exists archive_scheduled_task;
create table archive_scheduled_task (
  archive_scheduled_task_id bigint(20) auto_increment not null,
  scheduled_task_id bigint(20) not null,
  primary key (archive_scheduled_task_id),
  constraint archive_scheduled_task_ibfk1 foreign key (scheduled_task_id) references scheduled_task (scheduled_task_id) on delete cascade
);

alter table data_feed add archive tinyint(4) not null default 0;