drop table if exists batchbook_supertag;
create table batchbook_supertag (
  batchbook_supertag_id bigint(20) auto_increment not null,
  data_source_id bigint(20) not null,
  supertag_id bigint(20) not null,
  primary key (batchbook_supertag_id),
  constraint batchbook_supertag_ibfk1 foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade
);