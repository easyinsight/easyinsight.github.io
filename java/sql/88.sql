drop table if exists solution_install;
create table solution_install (
  solution_install_id bigint(20) auto_increment not null,
  solution_id bigint(20) not null,
  original_data_source_id bigint(20) not null,
  installed_data_source_id bigint(20) not null,
  primary key(solution_install_id),
  constraint solution_install_ibfk1 foreign key (solution_id) references solution (solution_id) on delete cascade,
  constraint solution_install_ibfk2 foreign key (original_data_source_id) references data_feed (data_feed_id) on delete cascade,
  constraint solution_install_ibfk3 foreign key (installed_data_source_id) references data_feed (data_feed_id) on delete cascade
);