alter table account add account_state integer not null default 2;

drop table if exists account_user_license;
create table account_user_license (
  account_user_license_id bigint(11) auto_increment not null,
  quantity integer not null,
  creation_date datetime not null,
  account_id bigint(11) not null,
  primary key(account_user_license_id),
  key account_id (account_id),
  constraint account_user_license_ibfk1 foreign key (account_id) references account (account_id)
);

drop table if exists benchmark;
create table benchmark (
  category varchar(40) not null,
  elapsed_time integer not null,
  index (category)
);

alter table solution add solution_tier integer not null default 2;

alter table goal_tree_node add sub_tree_id bigint(11) default null;