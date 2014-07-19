alter table analysis_item add default_date varchar(255) default null;


drop table if exists image_selenium_trigger;
create table image_selenium_trigger (
  image_selenium_trigger_id bigint(20) not null auto_increment,
  report_id bigint(20) not null,
  image_state tinyint(4) not null,
  image_response longblob default null,
  image_preferred_width integer not null,
  image_preferred_height integer not null,
  created_at datetime not null,
  updated_at datetime not null,
  selenium_username varchar(30) not null,
  selenium_password varchar(30) not null,
  user_id bigint(20) not null,
  primary key (image_selenium_trigger_id)
);

create table image_selenium_trigger_to_filter (
  image_selenium_trigger_to_filter_id bigint(20) not null auto_increment,
  image_selenium_trigger_id bigint(20) not null,
  filter_id bigint(20) not null,
  primary key (image_selenium_trigger_to_filter_id)
);

create table copy_template (
  copy_template_id bigint(20) not null auto_increment,
  dashboard_id bigint(20) not null,
  target_source_id bigint(20) not null,
  primary key (copy_template_id)
);

create table copy_template_to_field_assignment (
  copy_template_to_field_assignment_id bigint(20) not null auto_increment,
  copy_template_id bigint(20) not null,
  source_field varchar(255) not null,
  target_field varchar(255) default null,
  primary key (copy_template_to_field_assignment_id)
);

alter table date_level_wrapper add display varchar(25) default null;
alter table date_level_wrapper add short_display varchar(25) default null;
alter table multi_date_filter add level integer not null default 7;
alter table multi_date_filter add units_back integer not null default 20;
alter table multi_date_filter add units_forward integer not null default 0;
alter table multi_date_filter add include_relative integer not null default 0;
alter table multi_date_filter add all_option integer not null default 0;