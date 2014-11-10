alter table feed_persistence_metadata drop foreign key feed_persistence_metadata_ibfk1;

alter table DRILLTHROUGH_SAVE add PHANTOMJS tinyint(4) not null default 0;

alter table infusionsoft add skip_tags tinyint(4) not null default 1;

create table teamwork (
  teamwork_id bigint(20) auto_increment not null,
  teamwork_api_key varchar(100) default null,
  teamwork_url varchar(100) default null,
  data_source_id bigint(20) not null,
  primary key (teamwork_id),
  constraint teamwork_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

alter table user add assigned_dashboard_is_fixed tinyint(4) not null default 0;