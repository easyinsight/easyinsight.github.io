drop table if exists link;
create table link (
  link_id bigint(20) auto_increment not null,
  label varchar(255) not null,
  primary key(link_id)
);

drop table if exists url_link;
create table url_link (
  url_link_id bigint(20) auto_increment not null,
  link_id bigint(20) not null,
  url varchar(255) not null,
  primary key(url_link_id),
  constraint url_link_ibfk1 foreign key (link_id) references link (link_id) on delete cascade
);

drop table if exists drill_through;
create table drill_through (
  drill_through_id bigint(20) auto_increment not null,
  link_id bigint(20) not null,
  report_id bigint(20) not null,
  primary key(drill_through_id),
  constraint drill_through_ibfk1 foreign key (link_id) references link (link_id) on delete cascade,
  constraint drill_through_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade
);

drop table if exists drill_through_to_drill_through_pair;
create table drill_through_to_drill_through_pair (
  drill_through_to_drill_through_pair_id bigint(20) auto_increment not null,
  link_id bigint(20) not null,
  drill_through_pair_id bigint(20) not null,
  primary key(drill_through_to_drill_through_pair_id),
  constraint drill_through_to_drill_through_pair_ibfk1 foreign key (link_id) references link (link_id),
  constraint drill_through_to_drill_through_pair_ibfk2 foreign key (drill_through_pair_id) references drill_through_pair (drill_through_pair_id)
);

drop table if exists drill_through_pair;
create table drill_through_pair (
  drill_through_pair_id bigint(20) auto_increment not null,
  analysis_item_id bigint(20) not null,
  filter_id bigint(20) not null,
  primary key(drill_through_pair_id),
  constraint drill_through_pair_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id),
  constraint drill_through_pair_ibfk2 foreign key (filter_id) references filter (filter_id)
);