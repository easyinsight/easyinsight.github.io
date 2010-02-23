truncate benchmark;
alter table benchmark add benchmark_date datetime not null;

alter table text_replace_scrub add analysis_item_id bigint(20) default null;
alter table text_replace_scrub add constraint text_replace_scrub_ibfk2 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade;