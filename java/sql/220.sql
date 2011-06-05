drop table if exists action_scorecard_log;
create table action_scorecard_log (
  action_scorecard_log_id bigint(20) auto_increment not null,
  scorecard_id bigint(20) not null,
  action_log_id bigint(20) not null,
  primary key (action_scorecard_log_id),
  constraint action_scorecard_log_ibfk1 foreign key (action_log_id) references action_log (action_log_id) on delete cascade,
  constraint action_scorecard_log_ibfk2 foreign key (scorecard_id) references scorecard (scorecard_id) on delete cascade
);

drop table if exists vertical_list;
create table vertical_list (
  vertical_list_id bigint(20) auto_increment not null,
  report_state_id bigint(20) not null,
  primary key(vertical_list_id),
  constraint vertical_list_ibfk1 foreign key (report_state_id) references report_state (report_state_id) on delete cascade
);

drop table if exists group_to_dashboard;
create table group_to_dashboard (
    group_to_dashboard_id bigint auto_increment not null,
    dashboard_id bigint not null,
    group_id bigint not null,
    role integer not null,
    primary key(group_to_dashboard_id),
    constraint group_to_dashboard_ibfk1 foreign key (dashboard_id) references dashboard (dashboard_id) on delete cascade,
    constraint group_to_dashboard_ibfk2 foreign key (group_id) references community_group (community_group_id) on delete cascade
);

drop table if exists group_to_scorecard;
create table group_to_scorecard (
    group_to_scorecard_id bigint auto_increment not null,
    scorecard_id bigint not null,
    group_id bigint not null,
    role integer not null,
    primary key(group_to_scorecard_id),
    constraint group_to_scorecard_ibfk1 foreign key (scorecard_id) references scorecard (scorecard_id) on delete cascade,
    constraint group_to_scorecard_ibfk2 foreign key (group_id) references community_group (community_group_id) on delete cascade
);

CREATE TABLE dashboard_element_to_filter (
  dashboard_element_to_filter_id bigint(20) NOT NULL AUTO_INCREMENT,
  dashboard_element_id bigint(20) NOT NULL,
  filter_id bigint(20) NOT NULL,
  PRIMARY KEY (dashboard_element_to_filter_id),
  CONSTRAINT dashboard_element_to_filter_ibfk1 FOREIGN KEY (dashboard_element_id) REFERENCES dashboard_element (dashboard_element_id) ON DELETE CASCADE,
  CONSTRAINT dashboard_element_to_filter_ibfk2 FOREIGN KEY (filter_id) REFERENCES filter (filter_id) ON DELETE CASCADE
) ENGINE=InnoDB;

alter table excel_export add anonymous_id varchar(255) default null;
alter table png_export add anonymous_id varchar(255) default null;

alter table excel_export change user_id user_id bigint(20) default null;
alter table png_export change user_id user_id bigint(20) default null;

drop table if exists general_delivery;
create table general_delivery (
  general_delivery_id bigint(20) auto_increment not null,
  body text not null,
  delivery_format integer not null,
  html_email tinyint(4) not null,
  scheduled_account_activity_id bigint(20) not null,
  sender_user_id bigint(20) not null,
  subject varchar(255) not null,
  timezone_offset integer not null,
  primary key (general_delivery_id),
  constraint general_delivery_ibfk1 foreign key (scheduled_account_activity_id) references scheduled_account_activity (scheduled_account_activity_id) on delete cascade,
  constraint general_delivery_ibfk2 foreign key (sender_user_id) references user (user_id) on delete cascade
);

drop table if exists delivery_to_report;
create table delivery_to_report (
  delivery_to_report_id bigint(20) auto_increment not null,
  general_delivery_id bigint(20) not null,
  report_id bigint(20) not null,
  delivery_index integer not null,
  primary key (delivery_to_report_id),
  constraint delivery_to_report_ibfk1 foreign key (general_delivery_id) references general_delivery (general_delivery_id) on delete cascade,
  constraint delivery_to_report_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade
);

drop table if exists delivery_to_scorecard;
create table delivery_to_scorecard (
  delivery_to_scorecard_id bigint(20) auto_increment not null,
  general_delivery_id bigint(20) not null,
  scorecard_id bigint(20) not null,
  delivery_index integer not null,
  primary key (delivery_to_scorecard_id),
  constraint delivery_to_scorecard_ibfk1 foreign key (general_delivery_id) references general_delivery (general_delivery_id) on delete cascade,
  constraint delivery_to_scorecard_ibfk2 foreign key (scorecard_id) references scorecard (scorecard_id) on delete cascade
);