drop table if exists scheduled_account_activity;
create table scheduled_account_activity (
  scheduled_account_activity_id bigint(20) auto_increment not null,
  account_id bigint(20) not null,
  activity_type integer not null;
  primary key(scheduled_account_activity_id),
  constraint scheduled_user_activity_ibfk1 foreign key (account_id) references account (account_id) on delete cascade
);

drop table if exists scheduled_data_source_refresh;
create table scheduled_data_source_refresh (
  scheduled_data_source_refresh_id bigint(20) auto_increment not null,
  scheduled_account_activity_id bigint(20) not null,
  data_source_id bigint(20) not null,
  primary key(scheduled_data_source_refresh_id),
  constraint scheduled_data_source_refresh_ibfk1 foreign key (scheduled_account_activity_id) references scheduled_account_activity (scheduled_account_activity_id) on delete cascade,
  constraint scheduled_data_source_refresh_ibfk2 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists schedule;
create table schedule (
  schedule_id bigint(20) auto_increment not null,
  scheduled_account_activity_id bigint(20) not null,
  schedule_type integer not null,
  schedule_hour integer not null,
  schedule_minute integer not null,
  primary key(schedule_id),
  constraint schedule_ibfk1 foreign key (scheduled_account_activity_id) references scheduled_account_activity (scheduled_account_activity_id) on delete cascade
);

drop table if exists weekly_schedule;
create table weekly_schedule (
  weekly_schedule_id bigint(20) auto_increment not null,
  schedule_id bigint(20) not null,
  day_of_week integer not null,
  primary key(weekly_schedule_id),
  constraint weekly_schedule_ibfk1 foreign key (schedule_id) references schedule (schedule_id) on delete cascade
);

drop table if exists monthly_schedule;
create table monthly_schedule (
  monthly_schedule_id bigint(20) auto_increment not null,
  schedule_id bigint(20) not null,
  day_of_month integer not null,
  primary key(monthly_schedule_id),
  constraint monthly_schedule_ibfk1 foreign key (schedule_id) references schedule (schedule_id) on delete cascade
);

drop table if exists data_activity_task_generator;
create table data_activity_task_generator (
  data_activity_task_generator_id bigint(20) auto_increment not null,
  task_generator_id bigint(20) not null,
  scheduled_activity_id bigint(20) not null,
  primary key (data_activity_task_generator_id),
  constraint data_activity_task_generator_ibfk1 foreign key (task_generator_id) references task_generator (task_generator_id) on delete cascade,
  constraint data_activity_task_generator_ibfk2 foreign key (scheduled_activity_id) references scheduled_activity (scheduled_activity_id) on delete cascade
);