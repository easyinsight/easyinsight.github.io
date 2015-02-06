alter table solution add summary text default null;

alter table dashboard add include_header_in_html tinyint(4) not null default 0;

alter table async_report_request change report report mediumblob default null;
alter table async_report_request change metadata metadata mediumblob default null;