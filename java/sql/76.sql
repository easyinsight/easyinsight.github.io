alter table account add column (billing_information_given tinyint);
alter table account add column (billing_day_of_month int);

DROP TABLE IF EXISTS billing_task_generator;
CREATE TABLE billing_task_generator(
  billing_generator_id bigint(20) auto_increment not null,
  task_generator_id bigint(20) not null,
  primary key(billing_generator_id),
  key (task_generator_id),
  constraint billing_task_generator_ibfk1 foreign key (task_generator_id) references task_generator (task_generator_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS billing_scheduled_task;
CREATE TABLE billing_scheduled_task (
  billing_scheduled_task_id bigint(20) auto_increment not null,
  scheduled_task_id bigint(20) not null,
  primary key(billing_scheduled_task_id),
  constraint billing_scheduled_task_ibfk1 foreign key (scheduled_task_id) references scheduled_task (scheduled_task_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS account_credit_card_billing_info;
CREATE TABLE account_credit_card_billing_info (
  account_credit_card_billing_info_id bigint(20) auto_increment not null,
  account_id bigint(20) not null,
  transaction_id varchar(255),
  amount varchar(255),
  response varchar(255),
  transaction_time timestamp,
  response_string varchar(255),
  response_code varchar(255),
  primary key(account_credit_card_billing_info_id),
  key (account_id),
  constraint account_credit_card_billing_info_ibfk1 foreign key (account_id) references account(account_id)
);