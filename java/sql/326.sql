alter table account drop default_reporting_sharing;
alter table analysis drop feed_visibility;
alter table analysis drop views;
alter table analysis drop rating_count;
alter table analysis drop rating_average;



drop table if exists github;
create table github (
  github_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  access_token varchar(255) default null,
  refresh_token varchar(255) default null,
  primary key (github_id),
  constraint github_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists freshdesk;
create table freshdesk (
  freshdesk_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  url varchar(255) default null,
  api_token varchar(255) default null,
  primary key (freshdesk_id),
  constraint freshdesk_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

alter table field_to_tag add data_source_id bigint(20) default null;
alter table field_to_tag add constraint field_to_tag_ibfk3 foreign key (data_source_id) references data_feed (data_feed_id) on delete set null;
alter table field_to_tag change analysis_item_id analysis_item_id bigint(20) default null;

alter table analysis_item_to_report_field_extension add data_source_id bigint(20);
alter table analysis_item_to_report_field_extension add constraint analysis_item_to_report_field_extension_ibfk3 foreign key (data_source_id) references data_feed (data_feed_id) on delete set null;
alter table analysis_item_to_report_field_extension change analysis_item_id analysis_item_id bigint(20) default null;

alter table analysis_item_to_report_field_extension add display_name varchar(255) default null;

