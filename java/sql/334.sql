

alter table analysis_item add custom_flag tinyint(4) not null default 0;

drop table if exists custom_flag_to_tag;
create table custom_flag_to_tag (
  custom_flag_to_tag_id bigint(20) auto_increment not null,
  tag_id bigint(20) not null,
  custom_flag integer not null,
  data_source_id bigint(20) not null,
  primary key (custom_flag_to_tag_id),
  constraint custom_flag_to_tag_ibfk1 foreign key (tag_id) references account_tag (account_tag_id) on delete cascade,
  constraint custom_flag_to_tag_ibfk2 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);



create index report_folder_idx on report_folder (account_id, folder_name, data_source_id);

