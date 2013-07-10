

create table dashboard_element_filter_setting (
  dashboard_element_filter_setting_id bigint(20) auto_increment not null,
  filter_id bigint(20) not null,
  dashboard_element_id bigint(20) not null,
  hide_filter tinyint(4) not null default 0,
  primary key (dashboard_element_filter_setting_id),
  constraint dashboard_element_filter_setting_ibfk1 foreign key (filter_id) references filter (filter_id) on delete cascade,
  constraint dashboard_element_filter_setting_ibfk2 foreign key (dashboard_element_id) references dashboard_element (dashboard_element_id) on delete cascade
);


create table upload_bytes (
  upload_bytes_id bigint(20) auto_increment not null,
  upload_time datetime not null,
  upload_key varchar(100) not null,
  bytes longblob default null,
  user_id bigint(20) not null,
  primary key (upload_bytes_id),
  constraint upload_bytes_ibfk1 foreign key (user_id) references user (user_id) on delete cascade
);