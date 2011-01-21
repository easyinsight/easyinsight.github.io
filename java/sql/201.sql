delete from goal_tree;
alter table goal_tree add data_source_id bigint(20) not null;
alter table goal_tree add constraint goal_tree_ibfkx foreign key (data_source_id) references data_feed (data_feed_id) on delete cascade;

alter table goal_tree add exchange_visible tinyint(4) not null default 0;
alter table goal_tree add account_visible tinyint(4) not null default 0;