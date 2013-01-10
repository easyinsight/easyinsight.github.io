drop table if exists delivery_to_group;
create table delivery_to_group (
  delivery_to_group_id bigint(20) auto_increment not null,
  scheduled_account_activity_id bigint(20) not null,
  group_id bigint(20) not null,
  primary key (delivery_to_group_id),
  constraint delivery_to_group_ibfk1 foreign key (scheduled_account_activity_id) references scheduled_account_activity (scheduled_account_activity_id) on delete cascade,
  constraint delivery_to_group_ibfk2 foreign key (group_id) references community_group (community_group_id) on delete cascade
);

