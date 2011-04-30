drop table if exists scorecard_delivery;
create table scorecard_delivery (
  scorecard_delivery_id bigint(20) auto_increment not null,
  body text not null,
  delivery_format integer not null,
  html_email tinyint(4) not null,
  scheduled_account_activity_id bigint(20) not null,
  sender_user_id bigint(20) not null,
  subject varchar(255) not null,
  timezone_offset integer not null,
  scorecard_id bigint(20) not null,
  primary key (scorecard_delivery_id),
  constraint scorecard_delivery_ibfk1 foreign key (scorecard_id) references scorecard (scorecard_id) on delete cascade,
  constraint scorecard_delivery_ibfk2 foreign key (scheduled_account_activity_id) references scheduled_account_activity (scheduled_account_activity_id) on delete cascade,
  constraint scorecard_delivery_ibfk3 foreign key (sender_user_id) references user (user_id) on delete cascade
);

