alter table multi_analysis_item_filter add alpha_sort tinyint(4) not null default 0;

alter table field_to_tag add display_name varchar(255) default null;