update data_feed set refresh_behavior = 2 where feed_type = 6;

alter table drill_through add add_all_filters tinyint(4) not null default 0;

drop table if exists wufoo_composite_source;
create table wufoo_composite_source (
  wufoo_composite_source_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  api_key varchar(255) default null,
  url varchar(255) default null,
  primary key (wufoo_composite_source_id),
  constraint wufoo_composite_source_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists wufoo_form_source;
create table wufoo_form_source (
  wufoo_form_source_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  form_id varchar(255) default null,
  primary key (wufoo_form_source_id),
  constraint wufoo_form_source_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists insightly_source;
create table insightly_source (
  insightly_source_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  api_key varchar(255) default null,
  primary key (insightly_source_id),
  constraint insightly_source_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

alter table account add google_domain_name varchar(255) default null;

alter table scheduled_account_activity drop foreign key scheduled_account_activity_ibfk2;
alter table scheduled_account_activity add constraint scheduled_account_activity_ibfk2 foreign key (user_id) references user (user_id) on delete set null;

alter table scorecard drop foreign key scorecard_ibfk1;
alter table scorecard add constraint scorecard_ibfk1 foreign key (user_id) references user (user_id) on delete set null;

alter table general_delivery drop foreign key general_delivery_ibfk2;
alter table general_delivery add constraint general_delivery_ibfk2 foreign key (sender_user_id) references user (user_id) on delete set null;