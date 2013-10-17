

create table cached_addon_report_source (
  cached_addon_report_source_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  report_id bigint(20) not null,
  primary key (cached_addon_report_source_id),
  constraint cached_addon_report_source_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade,
  constraint cached_addon_report_source_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade
);

create table cache_to_rebuild (
  cache_to_rebuild_id bigint(20) auto_increment not null,
  cache_time datetime not null,
  data_source_id bigint(20) not null,
  primary key (cache_to_rebuild_id),
  constraint cache_to_rebuild_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);

create table report_cache (
  report_cache_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  report_id bigint(20) not null,
  cache_key text not null,
  primary key (report_cache_id),
  constraint report_cache_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade,
  constraint report_cache_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade
);