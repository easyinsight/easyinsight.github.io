

create table new_user_link (
  new_user_link_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  date_issued datetime not null,
  token varchar(255) not null,
  primary key (new_user_link_id),
  constraint new_user_link_ibfk1 foreign key (user_id) references user (user_id) on delete cascade
);

create unique index new_user_link_idx1 on new_user_link (token);

alter table account add send_emails_to_new_users tinyint(4) not null default 1;

alter table task_generator add disabled_generator tinyint(4) not null default 0;
create index task_generator_idx5 on task_generator (disabled_generator);