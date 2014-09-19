drop table if exists reactivation_data;
create table reactivation_data (
  reactivation_data_id bigint(20) auto_increment not null,
  email_address varchar(255) not null,
  user_id bigint(20) not null,
  activation_key varchar(255) not null,
  first_name varchar(255) not null,
  last_name varchar(255) not null,
  date_generated date not null,
  became_user_id bigint(20) not null,
  primary key (reactivation_data_id)
);

alter table user add consultant tinyint(4) not null default 0;