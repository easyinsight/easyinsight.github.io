drop table if exists scenario;
create table scenario (
  scenario_id bigint(20) auto_increment not null,
  scenario_name varchar(255) not null,
  scenario_summary varchar(255) not null,
  scenario_key varchar(255) not null,
  scenario_image varchar(255) not null,
  scenario_description varchar(255) not null,
  account_id bigint(20) not null,
  user_id bigint(20) not null,
  primary key(scenario_id),
  constraint scenario_ibfk1 foreign key (account_id) references account (account_id) on delete cascade,
  constraint scenario_ibfk2 foreign key (user_id) references user (user_id) on delete cascade
);

alter table drill_through add mini_window tinyint(4) not null default 0;

alter table link add code_generated tinyint(4) not null default 0;

alter table excel_export add report_name varchar(255) not null;
alter table png_export add report_name varchar(255) not null;

drop table if exists form;
create table form (
  form_id bigint(20) auto_increment not null,
  report_state_id bigint(20) not null,
  primary key (form_id),
  constraint form_ibfk1 foreign key (report_state_id) references report_state (report_state_id) on delete cascade
);