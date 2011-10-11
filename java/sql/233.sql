drop table if exists dashboard_text;
create table dashboard_text (
  dashboard_text_id bigint(20) auto_increment not null,
  dashboard_element_id bigint(20) not null,
  dashboard_text text not null,
  primary key (dashboard_text_id),
  constraint dashboard_text_ibfk1 foreign key (dashboard_element_id) references dashboard_element (dashboard_element_id)
);