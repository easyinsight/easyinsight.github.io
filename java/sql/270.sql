alter table filter add not_condition tinyint(4) not null default 0;

drop table if exists currency_exchange_rates;
create table currency_exchange_rates (
  currency_exchange_rates_id bigint(20) auto_increment not null,
  currency_code varchar(255) not null,
  currency_amount double not null,
  primary key (currency_exchange_rates_id)
);

alter table dashboard_element add force_scrolling_off tinyint(4) not null default 0;

