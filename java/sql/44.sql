drop table if exists last_n_filter;
create table last_n_filter (
  last_n_filter_id bigint(11) auto_increment not null,
  filter_id bigint(11) not null,
  result_limit integer not null,
  primary key(last_n_filter_id),
  key filter_id (filter_id),
  constraint last_n_filter_ibfk1 foreign key (filter_id) references filter (filter_id) on delete cascade 
);

drop table if exists report_schedule;
create table report_schedule (
  report_schedule_id bigint(11) auto_increment not null,
  analysis_id bigint(11) not null,
  run_interval integer not null,
  primary key(report_schedule_id),
  key analysis_id (analysis_id),
  constraint report_schedule_ibfk1 foreign key (analysis_id) references analysis (analysis_id) on delete cascade
);

drop table if exists report_deliveree;
create table report_deliveree (
  report_deliveree_id bigint(11) auto_increment not null,
  report_schedule_id bigint(11) not null,
  user_id bigint(11) not null,
  primary key(report_deliveree_id),
  key user_id (user_id),
  key report_schedule_id (report_schedule_id),
  constraint report_deliveree_ibfk1 foreign key (user_id) references user (user_id) on delete cascade,
  constraint report_deliveree_ibfk2 foreign key (report_schedule_id) references report_schedule (report_schedule_id) on delete cascade
);

drop table if exists guest_user;
create table guest_user (
  guest_user_id bigint(11) auto_increment not null,
  user_id bigint(11) not null,
  primary key(guest_user_id),
  key user_id (user_id),
  constraint guest_user_ibfk1 foreign key (user_id) references user (user_id) on delete cascade
);

drop table if exists account_to_guest_user;
create table account_to_guest_user (
  account_to_guest_user_id bigint auto_increment not null,
  account_id bigint(11) not null,
  guest_user_id bigint(11) not null,
  primary key(account_to_guest_user_id),
  key account_id (account_id),
  key guest_user_id (guest_user_id),
  constraint account_to_guest_user_ibfk1 foreign key (account_id) references account (account_id) on delete cascade,
  constraint account_to_guest_user_ibfk2 foreign key (guest_user_id) references guest_user (guest_user_id) on delete cascade
);