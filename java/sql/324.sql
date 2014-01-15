alter table community_group add group_account_id bigint(20) default null;
alter table community_group add constraint community_group_ibfk3 foreign key (group_account_id) references account (account_id) on delete cascade;

alter table flat_date_filter add start_year integer not null default 0;