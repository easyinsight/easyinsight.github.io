drop table if exists user_permission;
create table user_permission (
    user_permission_id integer auto_increment not null,
    permission_name varchar(100),
    accounts_id integer not null,
    primary key(user_permission_id)
)