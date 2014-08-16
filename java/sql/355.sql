

create table happyfox (
  happyfox_id bigint(20) auto_increment not null,
  api_key varchar(255) default null,
  auth_token varchar(255) default null,
  url varchar(255) default null,
  data_source_id bigint(20) not null,
  primary key (happyfox_id),
  constraint happyfox_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

alter table account add default_font_family varchar(50) default null;
alter table account add timezone varchar(50) default null;