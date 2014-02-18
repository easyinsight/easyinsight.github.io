alter table dashboard add color_set varchar(255) default 'None';

alter table data_feed add visible_within_parent_configuration tinyint(4) not null default 0;