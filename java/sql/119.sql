alter table user_to_data_source_notification drop foreign key user_to_data_source_notification_ibfk1;
alter table user_to_data_source_notification drop foreign key user_to_data_source_notification_ibfk2;
alter table user_to_data_source_notification add constraint user_to_data_source_notification_ibfk1 foreign key (notification_id) references notification_base(notification_id) on delete cascade;
alter table user_to_data_source_notification add constraint user_to_data_source_notification_ibfk2 foreign key (feed_id) references data_feed(data_feed_id) on delete cascade;

alter table user_to_report_notification drop foreign key user_to_report_notification_ibfk1;
alter table user_to_report_notification drop foreign key user_to_report_notification_ibfk2;
alter table user_to_report_notification add constraint user_to_report_notification_ibfk1 foreign key (notification_id) references notification_base(notification_id) on delete cascade;
alter table user_to_report_notification add constraint user_to_report_notification_ibfk2 foreign key (analysis_id) references analysis(analysis_id) on delete cascade;