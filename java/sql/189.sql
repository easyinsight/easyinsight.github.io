create table highrise_additional_token (
  highrise_additional_token_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  user_id bigint(20) default null,
  token varchar(255) not null,
  token_valid tinyint(4) not null,
  primary key(highrise_additional_token_id),
  constraint highrise_additional_token_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade,
  constraint highrise_additional_token_ibfk2 foreign key (user_id) references user (user_id) on delete cascade
);

alter table account add currency_symbol varchar(4) not null default '$';

drop table if exists user_image;
create table user_image (
  user_image_id bigint(20) auto_increment not null,
  image_bytes longblob not null,
  user_id bigint(20) not null,
  image_name varchar(255) not null,
  primary key(user_image_id),
  constraint user_image_ibfk1 foreign key (user_id) references user (user_id) on delete cascade
);

drop table if exists application_skin;
create table application_skin (
  application_skin_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  primary key(application_skin_id)
);

drop table if exists application_skin_to_report_property;
create table application_skin_to_report_property (
  application_skin_to_report_property_id  bigint(20) auto_increment  not null,
  application_skin_id bigint(20) not null,
  report_property_id bigint(20) not null,
  primary key(application_skin_to_report_property_id),
  constraint application_skin_to_report_property_ibfk1 foreign key (application_skin_id) references application_skin (application_skin_id) on delete cascade,
  constraint application_skin_to_report_property_ibfk2 foreign key (report_property_id) references report_property (report_property_id) on delete cascade
);

drop table if exists report_image_property;
create table report_image_property (
  report_image_property_id bigint(20) auto_increment not null,
  report_property_id bigint(20) not null,
  user_image_id bigint(20) not null,
  image_name varchar(255) not null,
  primary key(report_image_property_id),
  constraint report_image_property_ibfk1 foreign key (report_property_id) references report_property (report_property_id) on delete cascade,
  constraint report_image_property_ibfk2 foreign key (user_image_id) references user_image (user_image_id) on delete cascade
);