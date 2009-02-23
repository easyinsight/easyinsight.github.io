drop table if exists account_activation;
create table account_activation (
  account_activation_id bigint auto_increment not null,
  account_id bigint(11) not null,
  activation_key varchar(20) not null,
  creation_date datetime not null,
  primary key(account_activation_id),
  key account_id (account_id),
  constraint account_activation_ibfk1 foreign key (account_id) references account (account_id) on delete cascade
);

drop table if exists jira;
create table jira (
  jira_id bigint auto_increment not null,
  data_feed_id bigint(11) not null,
  url varchar(255) not null,
  primary key(jira_id),
  key data_feed_id (data_feed_id),
  constraint jira_ibfk1 foreign key (data_feed_id) references data_feed (data_feed_id) on delete cascade
);