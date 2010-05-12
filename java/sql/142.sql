create index analysis_idx7 on analysis (temporary_report);

create index data_feed_idx1 on data_feed (publicly_visible);
create index data_feed_idx2 on data_feed (marketplace_visible);
create index data_feed_idx3 on data_feed (visible);
create index data_feed_idx4 on data_feed (parent_source_id);

alter table user add initial_setup_done tinyint(4) not null default 1;
alter table user drop permissions;

alter table basecamp add include_archived tinyint(4) not null default 0;
alter table basecamp add include_inactive tinyint(4) not null default 0;
alter table basecamp add include_comments tinyint(4) not null default 0;

alter table account add renewal_option_available tinyint(4) not null default 0;

alter table user add opt_in_email tinyint(4) not null default 0;

drop table if exists user_unsubscribe_key;
create table user_unsubscribe_key (
  user_unsubscribe_key_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  unsubscribe_key varchar(255) not null,
  primary key(user_unsubscribe_key_id),
  constraint user_ibkf1 foreign key (user_id) references user (user_id) on delete cascade
);

update user, account set user.opt_in_email = 1 where user.account_id = account.account_id and account.opt_in_email = 1;