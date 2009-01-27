drop table if exists png_export;
create table png_export (
    png_export_id bigint auto_increment not null,
    user_id bigint not null,
    png_image blob not null,
    primary key(png_export_id)
);

#alter table solution add solution_archive_name varchar(255);
alter table solution add copy_data tinyint;
alter table solution add industry varchar(255);
alter table solution add author varchar(255);