#alter table analysis_dimension add url_pattern varchar(255) default null;
create index goal_outcome_eval_idx on goal_outcome (evaluation_date);

alter table analysis_item add url_pattern varchar(255) default null;