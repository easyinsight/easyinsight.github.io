alter table dashboard_grid add width integer not null default 0;
alter table dashboard_grid add background_color integer not null default 0;
alter table dashboard_grid add background_alpha double not null default 1;

alter table dashboard_stack add stack_control integer not null default 0;

drop table if exists dashboard_image;
create table dashboard_image (
  dashboard_image_id bigint(20) auto_increment not null,
  dashboard_element_id bigint(20) not null,
  user_image_id bigint(20) not null,
  primary key (dashboard_image_id),
  constraint dashboard_image_ibfk1 foreign key (dashboard_element_id) references dashboard_element (dashboard_element_id),
  constraint dashboard_image_ibfk2 foreign key (user_image_id) references user_image (user_image_id)
);