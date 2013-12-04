alter table dashboard_report add preferred_tag bigint(20) default null;
alter table dashboard_report add constraint dashboard_report_ibfk3 foreign key (preferred_tag) references account_tag (account_tag_id) on delete set null;

alter table dashboard add embed_with_key tinyint(4) not null default 0;
alter table dashboard add tablet_dashboard_id bigint(20) default null;
alter table dashboard add constraint dashboard_ibfk6 foreign key (tablet_dashboard_id) references dashboard (dashboard_id) on delete set null;

alter table dashboard add phone_dashboard_id bigint(20) default null;
alter table dashboard add constraint dashboard_ibfk7 foreign key (phone_dashboard_id) references dashboard (dashboard_id) on delete set null;

