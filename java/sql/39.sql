drop table if exists analysis_to_data_scrub;
create table analysis_to_data_scrub (
  analysis_to_data_scrub_id bigint(11) auto_increment not null,
  analysis_id bigint(11),
  data_scrub_id bigint(11),
  primary key(analysis_to_data_scrub_id)
);