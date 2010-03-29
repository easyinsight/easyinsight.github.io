update account set max_users = 1 where account_type = 1;
update account set max_users = 5 where account_type = 2;
update account set max_users = 50 where account_type = 3;

update account set max_size = 1000000 where account_type = 1;
update account set max_size = 50000000 where account_type = 2;
update account set max_size = 500000000 where account_type = 3;