drop table if exists configure_data_feed_todo;
drop table if exists todo_base;
create table todo_base (
  todo_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  todo_type int(11),
  primary key(todo_id),
  constraint todo_base_ibfk1 foreign key (user_id) references user(user_id) on delete cascade
);

create table configure_data_feed_todo (
  configure_data_feed_todo_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  todo_id bigint(20) not null,
  primary key(configure_data_feed_todo_id),
  key todo_id (todo_id),
  key data_source_id (data_source_id),
  constraint configure_data_feed_todo_ibfk1 foreign key (todo_id) references todo_base(todo_id) on delete cascade,
  constraint configure_data_feed_todo_ibfk2 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);
