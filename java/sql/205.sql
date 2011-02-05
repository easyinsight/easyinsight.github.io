alter table scorecard add account_visible tinyint(4) not null default 0;
alter table scorecard add url_key varchar(255) default null;
alter table scorecard add description varchar(255) default null;
alter table scorecard add exchange_visible tinyint(4) not null default 0;

create index account_idx1 on account (opt_in_email);
create index account_idx2 on account (account_type);
create index account_idx3 on account (account_state);

create index user_idx1 on user (account_admin);

create index user_sales_email_schedule_idx1 on user_sales_email_schedule (send_date);

alter table account_timed_state add constraint account_timed_state_ibfk1 foreign key (account_id) references account (account_id) on delete cascade;

create index account_timed_state_idx1 on account_timed_state (state_change_time);