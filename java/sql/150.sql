# alter table rolling_range_filter add before_or_after tinyint(4) not null default 0;
# alter table rolling_range_filter add interval_type integer null default 0;
# alter table rolling_range_filter add interval_amount integer not null default 0;

alter table account add date_format integer not null default 0;