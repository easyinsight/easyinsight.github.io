alter table lookup_table change target_item_id target_item_id bigint(20) default null;

alter table analysis_calculation add recalculate_summary tinyint(4) not null default 0;

alter table delivery_to_report add delivery_format integer not null default 1;
alter table delivery_to_scorecard add delivery_format integer not null default 1;
alter table general_delivery drop delivery_format;

alter table scheduled_account_activity add user_id bigint(20) default null;
alter table scheduled_account_activity add constraint scheduled_account_activity_ibfk2 foreign key (user_id) references user (user_id) on delete cascade;