drop table if exists solution_tag;
create table solution_tag (
    solution_tag_id bigint(20) auto_increment not null,
    solution_id bigint(20) not null,
    tag_name varchar(255) not null,
    primary key(solution_tag_id),
    key (solution_id),
    constraint solution_tag_ibfk1 foreign key (solution_id) references solution (solution_id)
);

drop table if exists excel_export;
create table excel_export (
    excel_export_id bigint(20) auto_increment not null,
    excel_file longblob not null,
    primary key(excel_export_id)  
);

alter table user add account_admin tinyint(4) not null default 1;
alter table user add data_source_creator tinyint(4) not null default 0;
alter table user add insight_creator tinyint(4) not null default 0;