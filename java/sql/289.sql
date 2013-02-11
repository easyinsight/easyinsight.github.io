drop table if exists PERSONALIZED_SALES_EMAIL;
create table PERSONALIZED_SALES_EMAIL (
  PERSONALIZED_SALES_EMAIL_ID bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  welcome_type integer not null,
  primary key (PERSONALIZED_SALES_EMAIL_ID),
  constraint PERSONALIZED_SALES_EMAIL_ibfk1 foreign key (user_id) references user (user_id) on delete cascade
);

drop table if exists account_to_report;
create table account_to_report (
  account_to_report_id bigint(20) auto_increment not null,
  account_id bigint(20) not null,
  report_id bigint(20) not null,
  primary key (account_to_report_id),
  constraint account_to_report_ibfk1 foreign key (account_id) references account (account_id) on delete cascade,
  constraint account_to_report_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade
);

drop table if exists account_to_dashboard;
create table account_to_dashboard (
  account_to_dashboard_id bigint(20) auto_increment not null,
  account_id bigint(20) not null,
  dashboard_id bigint(20) not null,
  primary key (account_to_dashboard_id),
  constraint account_to_dashboard_ibfk1 foreign key (account_id) references account (account_id) on delete cascade,
  constraint account_to_dashboard_ibfk2 foreign key (dashboard_id) references dashboard (dashboard_id) on delete cascade
);

drop table if exists sales_email_scheduler;
create table sales_email_scheduler (
  sales_email_scheduler_id bigint(20) auto_increment not null,
  task_generator_id bigint(20) not null,
  primary key (sales_email_scheduler_id),
  constraint sales_email_scheduler_ibfk1 foreign key (task_generator_id) references task_generator (task_generator_id) on delete cascade
);

drop table if exists sales_email_task;
create table sales_email_task (
  sales_email_task_id bigint(20) auto_increment not null,
  scheduled_task_id bigint(20) not null,
  primary key (sales_email_task_id),
  constraint sales_email_task_ibfk1 foreign key (scheduled_task_id) references scheduled_task (scheduled_task_id) on delete cascade
);