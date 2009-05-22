alter table analysis_step change correlation_dimension_id correlation_dimension_id bigint(20) default null;
alter table analysis_step change end_date_dimension_id end_date_dimension_id bigint(20) default null;
alter table analysis_step change start_date_dimension_id start_date_dimension_id bigint(20) default null;