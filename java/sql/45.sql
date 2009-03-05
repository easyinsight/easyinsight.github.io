alter table account add group_id bigint(11) default null;
alter table guest_user add state integer not null default 1; 