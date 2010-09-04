drop table if exists data_transaction;
create table data_transaction (
  data_transaction_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  data_source_name varchar(255) not null,
  replace_data tinyint(4) not null,
  change_data_source_to_match tinyint(4) not null,
  external_txn_id varchar(20) not null,
  txn_date datetime not null,
  txn_status tinyint not null,
  primary key(data_transaction_id),
  constraint data_transaction_ibfk1 foreign key (user_id) references user (user_id) on delete cascade
);

drop table if exists data_transaction_command;
create table data_transaction_command (
  data_transaction_command_id bigint(20) auto_increment not null,
  command_blob blob not null,
  data_transaction_id bigint(20) not null,
  primary key (data_transaction_command_id),
  constraint data_transaction_command_ibfk1 foreign key (data_transaction_id) references data_transaction (data_transaction_id) on delete cascade
);

# alter table user add guest_user tinyint(4) not null default 0;