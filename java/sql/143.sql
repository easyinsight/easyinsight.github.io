# create unique index feed_pers_cidx1 on feed_persistence_metadata (feed_id, version);

# drop table group_to_feed_join;

alter table account add upgraded tinyint(4) not null default 0;

# alter table distributed_lock add lock_time bigint(20) not null default 0;