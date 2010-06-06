update account set account_type = 7 where account_type = 6;
update account set account_type = 6 where account_type = 5;
update account set account_type = 5 where account_type = 4;
update account set account_type = 4 where account_type = 3;

update account set max_size = 5000000 where account_type = 1;
update account set max_size = 35000000 where account_type = 2;
update account set max_size = 90000000 where account_type = 3;
update account set max_size = 250000000 where account_type = 4;

alter table analysis_item add sort_sequence tinyint(4) not null default 0;