create table filter_set (
  filter_set_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  filter_set_name varchar(255) not null,
  filter_set_description text default null,
  url_key varchar(255) not null,
  data_source_id bigint(20) not null,
  primary key (filter_set_id),
  constraint filter_set_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

create table filter_set_to_filter (
  filter_set_to_filter_id bigint(20) auto_increment not null,
  filter_set_id bigint(20) not null,
  filter_id bigint(20) not null,
  primary key (filter_set_to_filter_id),
  constraint filter_set_to_filter_ibfk1 foreign key (filter_set_id) references filter_set (filter_set_id) on delete cascade,
  constraint filter_set_to_filter_ibfk2 foreign key (filter_id) references filter (filter_id) on delete cascade
);

alter table filter add parent_child_label varchar(255) default null;
alter table filter add child_to_parent_label varchar(255) default null;
alter table filter drop flexible_date_filter;
alter table filter drop default_date_filter;
alter table data_feed add manual_report_run tinyint(4) not null default 0;
alter table data_feed add show_tags tinyint(4) not null default 0;
alter table data_feed add default_tag_id bigint(20) default null;

create table filter_set_stub (
  filter_set_stub_id bigint(20) auto_increment not null,
  filter_set_id bigint(20) not null,
  primary key (filter_set_stub_id),
  constraint filter_set_stub_ibfk1 foreign key (filter_set_id) references filter_set (filter_set_id) on delete cascade
);

drop table report_to_filter_set_stub;
create table report_to_filter_set_stub (
  report_to_filter_set_stub_id bigint(20) auto_increment not null,
  filter_set_stub_id bigint(20) not null,
  report_id bigint(20) not null,
  primary key (report_to_filter_set_stub_id),
  constraint report_to_filter_set_stub_ibfk1 foreign key (filter_set_stub_id) references filter_set_stub (filter_set_stub_id) on delete cascade,
  constraint report_to_filter_set_stub_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade
);