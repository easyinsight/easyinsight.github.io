delete from analysis_range;

drop table if exists range_option;
create table range_option (
  range_option_id bigint(20) auto_increment not null,
  display_name varchar(255) not null,
  low_value double not null,
  high_value double not null,
  primary key(range_option_id)
) ENGINE=InnoDB;

drop table if exists analysis_range_to_range_option;
create table analysis_range_to_range_option (
  analysis_range_to_range_option_id bigint(20) auto_increment not null,
  analysis_item_id bigint(20) not null,
  range_option_id bigint(20) not null,
  primary key(analysis_range_to_range_option_id),
  constraint analysis_range_to_range_option_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade,
  constraint analysis_range_to_range_option_ibfk2 foreign key (range_option_id) references range_option (range_option_id) on delete cascade
) ENGINE=InnoDB;

alter table analysis_range add lower_range_option_id bigint(20) not null;
alter table analysis_range add higher_range_option_id bigint(20) not null;
alter table analysis_range add constraint analysis_range_ibfk2 foreign key (lower_range_option_id) references range_option (range_option_id) on delete cascade;
alter table analysis_range add constraint analysis_range_ibfk3 foreign key (higher_range_option_id) references range_option (range_option_id) on delete cascade;

alter table analysis_range add aggregation_type integer not null;

alter table analysis_item add concrete tinyint(4) not null default 1;

alter table list_report add summarize_all tinyint(4) not null default 0;

alter table range_filter add low_value_defined tinyint(4) not null default 1;
alter table range_filter add high_value_defined tinyint(4) not null default 1;