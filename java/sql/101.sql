drop table if exists buy_our_stuff_todo;
create table buy_our_stuff_todo (
  buy_our_stuff_id bigint(20) auto_increment not null,
  todo_id bigint(20) not null,
  primary key (buy_our_stuff_id),
  constraint buy_our_stuff_todo_ibfk1 foreign key (todo_id) references todo_base(todo_id) on delete cascade
);