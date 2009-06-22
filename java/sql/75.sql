alter table goal_tree_node_to_solution drop foreign key goal_tree_node_to_solution_ibfk2;
alter table goal_tree_node_to_solution add constraint goal_tree_node_to_solution_ibfk2 foreign key (solution_id) references solution (solution_id) on delete cascade;

alter table solution add solution_image longblob;

alter table solution add category integer not null default 1;

alter table solution add screencast_directory varchar(40);
alter table solution add screencast_mp4_name varchar(40);

drop table if exists report_image;
create table report_image (
  report_image_id bigint(20) auto_increment not null,
  report_id bigint(20) not null,
  report_image longblob not null,
  primary key (report_image_id),
  key report_id (report_id),
  constraint report_image_ibfk1 foreign key (report_id) references analysis (analysis_id) on delete cascade
);

alter table analysis add author_name varchar(255);