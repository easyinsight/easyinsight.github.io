alter table dashboard_grid add show_label tinyint(4) not null default 0;

alter table dashboard_text add color integer not null default 0;
alter table dashboard_text add font_size integer not null default 12;



alter table ytd_field_extension add always_show tinyint(4) not null default 0;
alter table vertical_list_field_extension add always_show tinyint(4) not null default 0;