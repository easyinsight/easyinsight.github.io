alter table USER_IMAGE add url_key varchar(50) default null;

create table mailchimp (
  mailchimp_id bigint(20) auto_increment not null,
  mailchimp_api_key varchar(100) default null,
  data_source_id bigint(20) not null,
  primary key (mailchimp_id),
  constraint mailchimp_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

create table mailchimp_list (
  mailchimp_list_id bigint(20) auto_increment not null,
  list_id varchar(100) default null,
  data_source_id bigint(20) not null,
  primary key (mailchimp_list_id),
  constraint mailchimp_list_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);