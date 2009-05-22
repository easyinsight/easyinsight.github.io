alter table time_based_analysis_measure add constraint time_based_analysis_measure_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade;
alter table time_based_analysis_measure add constraint time_based_analysis_measure_ibfk2 foreign key (date_dimension) references analysis_item (analysis_item_id) on delete cascade;

alter table upload_policy_groups add constraint upload_policy_groups_ibfk1 foreign key (feed_id) references data_feed (data_feed_id) on delete cascade;
alter table upload_policy_groups add constraint upload_policy_groups_ibfk2 foreign key (group_id) references community_group (community_group_id) on delete cascade;

delete upload_policy_users from upload_policy_users left join data_feed on upload_policy_users.feed_id = data_feed.data_feed_id where data_feed.data_feed_id is null;
delete upload_policy_users from upload_policy_users left join user on upload_policy_users.user_id = user.user_id where user.user_id is null;

alter table upload_policy_users add constraint upload_policy_users_ibfk1 foreign key (feed_id) references data_feed (data_feed_id) on delete cascade;
alter table upload_policy_users add constraint upload_policy_users_ibfk2 foreign key (user_id) references user (user_id) on delete cascade;

alter table analysis_to_data_scrub change analysis_id analysis_id bigint(20) not null;
alter table analysis_to_data_scrub change data_scrub_id data_scrub_id bigint(20) not null;

alter table analysis_to_data_scrub add constraint analysis_to_data_scrub_ibfk1 foreign key (analysis_id) references analysis_item (analysis_item_id) on delete cascade;
alter table analysis_to_data_scrub add constraint analysis_to_data_scrub_ibfk2 foreign key (data_scrub_id) references data_scrub (data_scrub_id) on delete cascade;

truncate data_scrub;
truncate lookup_table_scrub;
truncate lookup_table_scrub_pair;
truncate text_replace_scrub;

alter table lookup_table_scrub change data_scrub_id data_scrub_id bigint(20) not null;
alter table lookup_table_scrub change source_key source_key bigint(20) not null;
alter table lookup_table_scrub change target_key target_key bigint(20) not null;

alter table lookup_table_scrub add constraint lookup_table_scrub_ibfk1 foreign key (data_scrub_id) references data_scrub (data_scrub_id) on delete cascade;
alter table lookup_table_scrub add constraint lookup_table_scrub_ibfk2 foreign key (source_key) references item_key (item_key_id) on delete cascade;
alter table lookup_table_scrub add constraint lookup_table_scrub_ibfk3 foreign key (target_key) references item_key (item_key_id) on delete cascade; 

alter table lookup_table_scrub_pair change data_scrub_id data_scrub_id bigint(20) not null;
alter table lookup_table_scrub_pair change source_value source_value bigint(20) not null;
alter table lookup_table_scrub_pair change target_value target_value bigint(20) not null;

alter table lookup_table_scrub_pair add constraint lookup_table_scrub_pair_ibfk1 foreign key (data_scrub_id) references data_scrub (data_scrub_id) on delete cascade;
alter table lookup_table_scrub_pair add constraint lookup_table_scrub_pair_ibfk2 foreign key (source_value) references value (value_id) on delete cascade;
alter table lookup_table_scrub_pair add constraint lookup_table_scrub_pair_ibfk3 foreign key (target_value) references value (value_id) on delete cascade;

alter table text_replace_scrub change data_scrub_id data_scrub_id bigint(20) not null;
alter table text_replace_scrub add constraint text_replace_scrub_ibfk1 foreign key (data_scrub_id) references data_scrub (data_scrub_id) on delete cascade;