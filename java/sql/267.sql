alter table account add days_over_size_boundary integer not null default 0;
alter table account add max_days_over_size_boundary integer not null default -1;
alter table account_credit_card_billing_info add days integer not null default 0;
alter table account_credit_card_billing_info add against_credit tinyint(4) not null default 0;