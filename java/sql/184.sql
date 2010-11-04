drop table if exists sendgrid;
create table sendgrid (
  sendgrid_id bigint(20) auto_increment not null,
  sg_username varchar(255) default null,
  sg_password varchar(255) default null,
  data_source_id bigint(20) not null,
  primary key (sendgrid_id),
  constraint sendgrid_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

drop table if exists cloudwatch;
create table cloudwatch (
  cloudwatch_id bigint(20) auto_increment not null,
  cg_username varchar(255) default null,
  cg_password varchar(255) default null,
  data_source_id bigint(20) not null,
  primary key (cloudwatch_id),
  constraint cloudwatch_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);