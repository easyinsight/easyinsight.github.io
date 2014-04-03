alter table analysis_item_filter add use_fully_qualified_names tinyint(4) not null default 0;

drop table if exists SURVEYGIZMO_MULTI_QUESTION_SOURCE;
create table SURVEYGIZMO_MULTI_QUESTION_SOURCE (
  SURVEYGIZMO_MULTI_QUESTION_SOURCE_ID BIGINT(20) AUTO_INCREMENT NOT NULL,
  QUESTION_ID VARCHAR(255),
  DATA_SOURCE_ID BIGINT(20),
  PRIMARY KEY (SURVEYGIZMO_MULTI_QUESTION_SOURCE_ID),
  CONSTRAINT SURVEYGIZMO_MULTI_QUESTION_SOURCE_IBFK1 FOREIGN KEY (DATA_SOURCE_ID) REFERENCES DATA_FEED (DATA_FEED_ID) ON DELETE CASCADE
);

drop table if exists linkedin_new;
create table linkedin_new (
  linkedin_new_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  token_key varchar(255) default null,
  token_secret_key varchar(255) default null,
  primary key (linkedin_new_id),
  constraint linkedin_new_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

alter table dashboard add report_header_background_color integer not null default 14540253;
alter table dashboard add report_header_text_color integer not null default 0;

alter table freshbooks add start_date datetime default null;

alter table composite_connection add source_cardinality tinyint(4) not null default 0;
alter table composite_connection add target_cardinality tinyint(4) not null default 0;
alter table composite_connection add force_outer_join tinyint(4) not null default 0;

alter table filter add no_dashboard_override tinyint(4) not null default 0;

alter table drill_through drop pass_through_field_id;