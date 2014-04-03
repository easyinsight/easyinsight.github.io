alter table analysis_item_filter add expand_dates integer not null default 0;
alter table multi_analysis_item_filter add expand_dates integer not null default 0;
alter table multi_analysis_item_filter add use_fully_qualified_names integer not null default 0;