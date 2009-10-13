drop table if exists user_to_report_notification;
drop table if exists user_to_data_source_notification;
drop table if exists group_comment_notification;
drop table if exists report_to_group_notification;
drop table if exists data_source_to_group_notification;
drop table if exists user_to_group_notification;
drop table if exists group_notification;
drop table if exists user_notification;
drop table if exists notification_base;

create table notification_base (
  notification_id bigint(20) auto_increment not null,
  notification_date datetime not null,
  notification_type int(11),
  acting_user_id bigint(20) not null,
  primary key(notification_id),
  key (acting_user_id),
  constraint user_notification_ibfk3 foreign key (acting_user_id) references user(user_id) on delete cascade
);

create table user_notification (
  user_notification_id bigint(20) auto_increment not null,
  notification_id bigint(20) not null,
  user_id bigint(20) not null,
  primary key(user_notification_id),
  key(user_id),
  key(notification_id),
  constraint user_notification_ibfk1 foreign key (notification_id) references notification_base(notification_id) on delete cascade,
  constraint user_notification_ibfk2 foreign key (user_id) references user(user_id) on delete cascade
);

create table group_notification (
  group_notification_id bigint(20) auto_increment not null,
  notification_id bigint(20) not null,
  group_id bigint(20) not null,
  primary key(group_notification_id),
  key(group_id),
  key(notification_id),
  key(acting_user_id),
  constraint group_notification_ibfk1 foreign key (notification_id) references notification_base(notification_id) on delete cascade,
  constraint group_notification_ibfk2 foreign key (group_id) references community_group(community_group_id) on delete cascade
);

create table user_to_group_notification (
  user_to_group_notification_id bigint(20) auto_increment not null,
  group_notification_id bigint(20) not null,
  user_id bigint(20) not null,
  user_action int(11) not null,
  primary key(user_to_group_notification_id),
  key(user_id),
  key(group_notification_id),
  constraint user_to_group_notification_ibfk1 foreign key (group_notification_id) references group_notification(group_notification_id) on delete cascade,
  constraint user_to_group_notification_ibfk2 foreign key (user_id) references user(user_id) on delete cascade
);

create table data_source_to_group_notification (
  data_source_to_group_notification_id bigint(20) auto_increment not null,
  group_notification_id bigint(20) not null,
  feed_id bigint(20) not null,
  feed_role int(11) not null,
  feed_action int(11) not null,
  primary key(data_source_to_group_notification_id),
  key(feed_id),
  key(group_notification_id),
  constraint data_source_to_group_notification_ibfk1 foreign key (group_notification_id) references group_notification(group_notification_id) on delete cascade,
  constraint data_source_to_group_notification_ibfk2 foreign key (feed_id) references data_feed(data_feed_id) on delete cascade
);

create table report_to_group_notification (
  report_to_group_notification_id bigint(20) auto_increment not null,
  group_notification_id bigint(20) not null,
  analysis_id bigint(20) not null,
  analysis_role int(11) not null,
  analysis_action int(11) not null,
  primary key(report_to_group_notification_id),
  key(analysis_id),
  key(group_notification_id),
  constraint report_to_group_notification_ibfk1 foreign key (group_notification_id) references group_notification(group_notification_id) on delete cascade,
  constraint report_to_group_notification_ibfk2 foreign key (analysis_id) references analysis(analysis_id) on delete cascade
);

create table group_comment_notification (
  group_comment_notification_id bigint(20) auto_increment not null,
  group_notification_id bigint(20) not null,
  comment varchar(255) not null,
  primary key(group_comment_notification_id),
  key(group_notification_id),
  constraint group_comment_notification_ibfk1 foreign key (group_notification_id) references group_notification(group_notification_id) on delete cascade
);

create table user_to_data_source_notification (
  user_to_data_source_notification_id bigint(20) auto_increment not null,
  user_notification_id bigint(20) not null,
  feed_action int(11) not null,
  feed_role int(11) not null,
  feed_id bigint(20) not null,
  primary key(user_to_data_source_notification_id),
  key(feed_id),
  constraint user_to_data_source_notification_ibfk1 foreign key (user_notification_id) references user_notification(user_notification_id),
  constraint user_to_data_source_notification_ibfk2 foreign key (feed_id) references data_feed(data_feed_id)
);

create table user_to_report_notification (
  user_to_report_notification_id bigint(20) auto_increment not null,
  user_notification_id bigint(20) not null,
  analysis_action int(11) not null,
  analysis_role int(11) not null,
  analysis_id bigint(20) not null,
  primary key (user_to_report_notification_id),
  key(analysis_id),
  constraint user_to_report_notification_ibfk1 foreign key (user_notification_id) references user_notification(user_notification_id),
  constraint user_to_report_notification_ibfk2 foreign key (analysis_id) references analysis(analysis_id)
);