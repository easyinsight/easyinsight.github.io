alter table dashboard add stack_fill1_start integer not null default 0;
alter table dashboard add stack_fill1_end integer not null default 0;
alter table dashboard add stack_fill2_start integer not null default 0;
alter table dashboard add stack_fill2_end integer not null default 0;
alter table dashboard add stack_fill_enabled tinyint(4) not null default 0;