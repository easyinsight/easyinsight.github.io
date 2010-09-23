drop table if exists html_selenium_processor;
create table html_selenium_processor (
  html_selenium_processor_id bigint(20) auto_increment not null,
  selenium_processor_id bigint(20) not null,
  report_id bigint(20) not null,
  primary key(html_selenium_processor_id),
  constraint html_selenium_processor_ibfk1 foreign key (selenium_processor_id) references selenium_processor (selenium_processor_id) on delete cascade,
  constraint html_selenium_processor_ibfk2 foreign key (report_id) references analysis (analysis_id) on delete cascade
);