alter table analysis_to_data_scrub drop foreign key analysis_to_data_scrub_ibfk1;
alter table analysis_to_data_scrub add constraint analysis_to_data_scrub_ibfk1 foreign key (analysis_id) references analysis (analysis_id) on delete cascade;

drop table if exists zip_code_geocode;
create table zip_code_geocode (
  zip_code_geocode_id bigint(20) auto_increment not null,
  zip_code varchar(20) not null,
  latitude double not null,
  longitude double not null,
  primary key(zip_code_geocode_id),
  index (zip_code)
);

drop table if exists analysis_zip;
create table analysis_zip (
  analysis_zip_id bigint(20) auto_increment not null,
  analysis_item_id bigint(20) not null,
  primary key(analysis_zip_id),
  constraint analysis_zip_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade
);

alter table analysis_coordinate add analysis_zip_id bigint(20) default null;
alter table analysis_coordinate add constraint analysis_coordinate_ibfk2 foreign key (analysis_zip_id) references analysis_item (analysis_item_id) on delete set null;