create index analysis_idx3 on analysis (marketplace_visible);

alter table CONSTANT_CONTACT add access_token varchar(255) default null;
alter table CONSTANT_CONTACT add refresh_token varchar(255) default null;

alter table youtrack_source add yt_username varchar(255) default null;
alter table youtrack_source add yt_password varchar(255) default null;



alter table dashboard add default_link bigint(20) default null;
alter table dashboard add constraint dashboard_ibfkx FOREIGN KEY (default_link) REFERENCES link (link_id) ON DELETE SET NULL;

drop table if exists infusionsoft;
create table infusionsoft (
  infusionsoft_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  api_key varchar(255) default null,
  url varchar(255) default null,
  primary key (infusionsoft_id),
  constraint infusionsoft_ibkf1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);



alter table data_source_refresh_audit add refresh_date datetime default null;
alter table data_source_refresh_audit add elapsed integer not null default 0;
alter table data_source_refresh_audit drop end_time;
alter table data_source_refresh_audit drop start_time;

