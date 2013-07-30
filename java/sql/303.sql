create table account_tag (
  account_tag_id bigint(20) auto_increment not null,
  account_id bigint(20) not null,
  tag_name varchar(255) not null,
  primary key (account_tag_id),
  constraint account_tag_ibfk1 foreign key (account_id) references account (account_id) on delete cascade
);

create table data_source_to_tag (
  data_source_to_tag_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  account_tag_id bigint(20) not null,
  primary key (data_source_to_tag_id),
  constraint data_source_to_tag_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade,
  constraint data_source_to_tag_ibfk2 foreign key (account_tag_id) references account_tag (account_tag_id) on delete cascade
);