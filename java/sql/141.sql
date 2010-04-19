drop table if exists user_scorecard_ordering;
create table user_scorecard_ordering (
  user_scorecard_ordering_id bigint(20) auto_increment not null,
  user_id bigint(20) not null,
  scorecard_id bigint(20) not null,
  scorecard_order bigint(20) not null,
  primary key(user_scorecard_ordering_id),
  constraint user_scorecard_ordering_ibfk1 foreign key (user_id) references user (user_id) on delete cascade,
  constraint user_scorecard_ordering_ibfk2 foreign key (scorecard_id) references scorecard (scorecard_id) on delete cascade
);