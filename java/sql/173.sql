drop table if exists first_value_filter;
create table first_value_filter (
  first_value_filter_id bigint(20) auto_increment not null,
  filter_id bigint(20) not null,
  primary key (first_value_filter_id),
  constraint first_value_filter_ibfk1 foreign key (filter_id) references filter (filter_id) on delete cascade
);

drop table if exists ordered_filter;
create table ordered_filter (
  ordered_filter_id bigint(20) auto_increment not null,
  filter_id bigint(20) not null,
  primary key (ordered_filter_id),
  constraint ordered_filter_ibfk1 foreign key (filter_id) references filter (filter_id) on delete cascade
);

drop table if exists ordered_filter_to_filter;
create table ordered_filter_to_filter (
  ordered_filter_to_filter_id bigint(20) auto_increment not null,
  ordered_filter_id bigint(20) not null,
  filter_id bigint(20) not null,
  order_value integer not null,
  primary key(ordered_filter_to_filter_id),
  constraint ordered_filter_to_filter_ibfk1 foreign key (ordered_filter_id) references ordered_filter (ordered_filter_id) on delete cascade,
  constraint ordered_filter_id_filter_ibfk2 foreign key (filter_id) references filter (filter_id) on delete cascade
);

drop table if exists or_filter;
create table or_filter (
  or_filter_id bigint(20) auto_increment not null,
  filter_id bigint(20) not null,
  primary key (or_filter_id),
  constraint or_filter_ibfk1 foreign key (filter_id) references filter (filter_id) on delete cascade
);

drop table if exists or_filter_to_filter;
create table or_filter_to_filter (
  or_filter_to_filter_id bigint(20) auto_increment not null,
  or_filter_id bigint(20) not null,
  filter_id bigint(20) not null,
  primary key(or_filter_to_filter_id),
  constraint or_filter_to_filter_ibfk1 foreign key (or_filter_id) references or_filter (or_filter_id) on delete cascade,
  constraint or_filter_id_filter_ibfk2 foreign key (filter_id) references filter (filter_id) on delete cascade
);

drop table if exists null_filter;
create table null_filter (
  null_filter_id bigint(20) auto_increment not null,
  filter_id bigint(20) not null,
  primary key (null_filter_id),
  constraint null_filter_ibfk1 foreign key (filter_id) references filter (filter_id) on delete cascade
);