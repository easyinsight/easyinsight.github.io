alter table salesforce_definition add access_token varchar(255) default null;
alter table salesforce_definition add instance_name varchar(255) default null;
alter table salesforce_definition add refresh_token varchar(255) default null;

drop table if exists salesforce_sub_definition;
create table salesforce_sub_definition (
  salesforce_sub_definition_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  sobject_name varchar(255) default null,
  primary key (salesforce_sub_definition_id),
  constraint salesforce_sub_definition_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);