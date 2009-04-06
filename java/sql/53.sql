drop table if exists milestone;
create table milestone (
  milestone_id bigint(20) auto_increment not null,
  milestone_date date not null,
  milestone_name varchar(100) not null,
  account_id bigint(20) not null,
  primary key(milestone_id),
  key account_id (account_id),
  constraint milestone_ibfk1 foreign key (account_id) references account (account_id)
);

alter table goal_tree_node add goal_milestone_id bigint(20) default null;