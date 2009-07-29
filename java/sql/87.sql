alter table account add column (activated tinyint(4));
update account set activated = true where account_state != 1;
update account set activated = false where account_state = 1;
update account set account_state = 2 where account_state = 1;
alter table account modify activated tinyint(4) not null; 