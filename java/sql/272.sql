# create unique index report_url_unique_idx on analysis (url_key);
# create unique index dashboard_url_unique_idx on dashboard (url_key);
# create unique index data_feed_url_unique_idx on data_feed (api_key);
# create unique index scorecard_url_unique_idx on scorecard (url_key);

drop table if exists report_folder;
create table report_folder (
  report_folder_id bigint(20) auto_increment not null,
  account_id bigint(20) not null,
  data_source_id bigint(20) not null,
  folder_name varchar(255) not null,
  folder_sequence integer not null,
  primary key (report_folder_id),
  constraint report_folder_ibfk1 foreign key (account_id) references account (account_id) on delete cascade,
  constraint report_folder_ibfk2 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
) AUTO_INCREMENT=10;

drop table if exists report_delivery_audit;
create table report_delivery_audit (
  report_delivery_audit_id bigint(20) auto_increment not null,
  scheduled_account_activity_id bigint(20) not null,
  target_email varchar(255) not null,
  successful tinyint(4) not null,
  send_date datetime not null,
  message text default null,
  primary key (report_delivery_audit_id),
  constraint report_delivery_audit_ibfk1 foreign key (scheduled_account_activity_id) references scheduled_account_activity (scheduled_account_activity_id) on delete cascade
);