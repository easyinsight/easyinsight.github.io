create table system_settings (
  system_settings_id bigint(20) auto_increment not null,
  user_activity_semaphore_limit integer not null default 2,
  primary key (system_settings_id)
);

alter table distributed_lock add lock_time datetime default null;