# alter table json_source add live_source tinyint(4) not null default 0;

drop table if exists report_stub;
create table report_stub (
  report_stub_id bigint(20) auto_increment not null,
  report_id bigint(20) not null,
  primary key (report_stub_id),
  constraint report_stub_ibfk1 foreign key (report_id) references analysis (analysis_id) on delete cascade
);

drop table if exists report_to_report_stub;
create table report_to_report_stub (
  report_to_report_stub_id bigint(20) auto_increment not null,
  report_id bigint(20) not null,
  report_stub_id bigint(20) not null,
  primary key (report_to_report_stub_id),
  constraint report_to_report_stub_ibfk1 foreign key (report_id) references analysis (analysis_id) on delete cascade,
  constraint report_to_report_stub_ibfk2 foreign key (report_stub_id) references report_stub (report_stub_id) on delete cascade
);



alter table composite_connection add source_report_id bigint(20) default null;
alter table composite_connection add target_report_id bigint(20) default null;



create table report_key (
  report_key_id bigint(20) auto_increment not null,
  report_id bigint(20) not null,
  item_key_id bigint(20) not null,
  parent_item_key_id bigint(20) not null,
  primary key (report_key_id),
  constraint report_key_ibfk1 foreign key (item_key_id) references item_key (item_key_id) on delete cascade,
  constraint report_key_ibfk2 foreign key (parent_item_key_id) references item_key (item_key_id) on delete cascade,
  constraint report_key_ibfk3 foreign key (report_id) references analysis (analysis_id) on delete cascade
);

drop table if exists selenium_request_page;
create table selenium_request_page (
  selenium_request_page_id bigint(20) auto_increment not null,
  page_bytes longblob not null,
  page_number integer not null,
  selenium_request_id bigint(20) not null,
  primary key (selenium_request_page_id),
  constraint selenium_request_page_ibfk1 foreign key (selenium_request_id) references selenium_request (selenium_request_id) on delete cascade
);

drop table if exists trello_composite_source;
create table trello_composite_source (
  trello_composite_source_id bigint(20) auto_increment not null,
  token_key varchar(255) default null,
  token_secret_key varchar(255) default null,
  data_source_id bigint(20) not null,
  primary key (trello_composite_source_id),
  constraint trello_composite_source_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists text_report;
create table text_report (
  text_report_id bigint(20) NOT NULL auto_increment,
  report_state_id bigint(20) default NULL,
  PRIMARY KEY  (text_report_id),
  KEY report_state_id (report_state_id),
  CONSTRAINT text_report_ibfk1 FOREIGN KEY (report_state_id) REFERENCES report_state (report_state_id) ON DELETE CASCADE
);