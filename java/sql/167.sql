alter table analysis_text drop foreign key analysis_text_ibfk1;
alter table analysis_text add constraint analysis_text_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade;

alter table report_delivery add timezone_offset integer not null default 0;

drop table if exists delivery_to_email;
create table delivery_to_email (
  delivery_to_email_id bigint(20) auto_increment not null,
  scheduled_account_activity_id bigint(20) not null,
  email_address varchar(255) not null,
  primary key(delivery_to_email_id),
  constraint delivery_to_email_ibfk1 foreign key (scheduled_account_activity_id) references scheduled_account_activity (scheduled_account_activity_id) on delete cascade 
);

drop table if exists report_delivery_audit;
create table report_delivery_audit (
  report_delivery_audit_id bigint(20) auto_increment not null,
  account_id bigint(20) not null,
  report_delivery_id bigint(20) not null,
  successful tinyint(4) not null,
  message text default null,
  target_email varchar(255) not null,
  send_date datetime not null,
  primary key (report_delivery_audit_id),
  constraint report_delivery_audit_ibfk1 foreign key (account_id) references account (account_id) on delete cascade,
  constraint report_delivery_audit_ibfk2 foreign key (report_delivery_id) references report_delivery (report_delivery_id) on delete cascade
);