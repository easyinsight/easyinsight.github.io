drop table if exists user_preferences;
create table user_preferences (
  user_preferences_id bigint(20) auto_increment not null,
  intro_option tinyint(4) not null,
  mydata_option tinyint(4) not null,
  marketplace_option tinyint(4) not null,
  groups_option tinyint(4) not null,
  kpi_option tinyint(4) not null,
  solutions_option tinyint(4) not null,
  api_option tinyint(4) not null,
  accounts_options tinyint(4) not null,
  user_id bigint(20) not null,
  primary key(user_preferences_id),
  constraint user_preferences_ibfk1 foreign key (user_id) references user (user_id) on delete cascade
);