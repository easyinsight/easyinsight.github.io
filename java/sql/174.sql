drop table if exists selenium_processor;
create table selenium_processor (
  selenium_processor_id bigint(20) auto_increment not null,
  processor_type integer not null,
  primary key (selenium_processor_id)
);

drop table if exists email_selenium_processor;
create table email_selenium_processor (
  email_selenium_processor_id bigint(20) auto_increment not null,
  selenium_processor_id bigint(20) not null,
  report_delivery_id bigint(20) not null,
  primary key(email_selenium_processor_id),
  constraint email_selenium_processor_ibfk1 foreign key (selenium_processor_id) references selenium_processor (selenium_processor_id) on delete cascade,
  constraint email_selenium_processor_ibfk2 foreign key (report_delivery_id) references report_delivery (report_delivery_id) on delete cascade
);

drop table if exists selenium_request;
create table selenium_request (
  selenium_request_id bigint(20) auto_increment not null,
  result_bytes longblob default null,
  selenium_processor_id bigint(20) not null,
  username varchar(255) not null,
  user_id bigint(20) not null,
  password varchar(255) not null,
  request_time timestamp not null,
  primary key(selenium_request_id),
  constraint selenium_request_ibfk1 foreign key (selenium_processor_id)  references selenium_processor (selenium_processor_id) on delete cascade,
  constraint selenium_request_ibfk2 foreign key (user_id)  references user (user_id) on delete cascade
);