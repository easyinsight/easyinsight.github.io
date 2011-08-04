drop table if exists analysis_item_filter;
create table analysis_item_filter (
  analysis_item_filter_id bigint(20) auto_increment not null,
  target_item_id bigint(20) not null,
  filter_id bigint(20) not null,
  primary key (analysis_item_filter_id),
  constraint analysis_item_filter_ibfk1 foreign key (target_item_id) references analysis_item (analysis_item_id) on delete cascade,
  constraint analysis_item_filter_ibfk2 foreign key (filter_id) references filter (filter_id) on delete cascade
);

drop table if exists analysis_item_filter_to_analysis_item;
create table analysis_item_filter_to_analysis_item (
  analysis_item_filter_to_analysis_item_id bigint(20) auto_increment not null,
  filter_id bigint(20) not null,
  analysis_item_id bigint(20) not null,
  primary key (analysis_item_filter_to_analysis_item_id),
  constraint analysis_item_filter_to_analysis_item_ibfk1 foreign key (filter_id) references filter (filter_id) on delete cascade,
  constraint analysis_item_filter_to_analysis_item_ibfk2 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade
);

drop table if exists dls;
create table dls (
  dls_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  persona_id bigint(20) not null,
  primary key (dls_id),
  constraint dls_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade,
  constraint dls_ibfk2 foreign key (persona_id) references persona (persona_id) on delete cascade
);

drop table if exists dls_to_filter;
create table dls_to_filter (
  dls_to_filter_id bigint(20) auto_increment not null,
  dls_id bigint(20) not null,
  filter_id bigint(20) not null,
  primary key (dls_to_filter_id),
  constraint dls_to_filter_ibfk1 foreign key (dls_id) references dls (dls_id) on delete cascade,
  constraint dls_to_filter_ibfk2 foreign key (filter_id) references filter (filter_id) on delete cascade
);

drop table if exists user_dls;
create table user_dls (
  user_dls_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  dls_id bigint(20) not null,
  primary key (user_dls_id),
  constraint user_dls_ibfk1 foreign key (user_id) references user (user_id) on delete cascade,
  constraint user_dls_ibfk2 foreign key (dls_id) references dls (dls_id) on delete cascade
);

drop table if exists user_dls_to_filter;
create table user_dls_to_filter (
  user_dls_to_filter_id bigint(20) auto_increment not null,
  user_dls_id bigint(20) not null,
  filter_id bigint(20) not null,
  original_filter_id bigint(20) not null,
  primary key (user_dls_to_filter_id),
  constraint user_dls_to_filter_ibfk1 foreign key (user_dls_id) references user_dls (user_dls_id) on delete cascade,
  constraint user_dls_to_filter_ibfk2 foreign key (filter_id) references filter (filter_id) on delete cascade,
  constraint user_dls_to_filter_ibfk3 foreign key (original_filter_id) references filter (filter_id) on delete cascade
);