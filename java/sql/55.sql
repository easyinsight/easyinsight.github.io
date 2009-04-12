alter table goal_tree change root_node root_node bigint(20) default null;

alter table data_feed add refresh_interval bigint(20) default null;

update data_feed set refresh_interval = 0;

alter table feed_persistence_metadata add last_data_time datetime default null;

drop table if exists account_activity;
create table account_activity (
  account_activity_id bigint(20) auto_increment not null,
  account_id bigint(20) not null,
  activity_date datetime not null,
  account_type integer not null,
  activity_type integer not null,
  activity_notes text default null,
  user_licenses integer not null,
  primary key(account_activity_id),
  key account_id (account_id),
  constraint account_activity_ibfk1 foreign key (account_id) references account (account_id)
);

drop table if exists account_payment;
create table account_payment (
  account_payment_id bigint(20) auto_increment not null,
  payment_required double not null,
  billing_date datetime not null,
  payment_made tinyint not null,
  payment_amount double default null,
  payment_date datetime default null,
  transaction_id varchar(100) default null,
  account_id bigint(20) not null,
  primary key(account_payment_id),
  key account_id (account_id),
  constraint account_payment_ibfk1 foreign key (account_id) references account (account_id)
);