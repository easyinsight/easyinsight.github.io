
drop table if exists drill_through;

drop table if exists url_link;

drop table if exists link;

create table link (
  link_id bigint(20) auto_increment not null,
  label varchar(255) not null,
  primary key(link_id)
);


create table url_link (
  url_link_id bigint(20) auto_increment not null,
  link_id bigint(20) not null,
  url varchar(255) not null,
  primary key(url_link_id),
  constraint url_link_ibfk1 foreign key (link_id) references link (link_id) on delete cascade
);


create table drill_through (
  drill_through_id bigint(20) auto_increment not null,
  link_id bigint(20) not null,
  report_id bigint(20) not null,
  primary key(drill_through_id),
  constraint drill_through_ibfk1 foreign key (link_id) references link (link_id) on delete cascade,
  constraint drill_through_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade
);