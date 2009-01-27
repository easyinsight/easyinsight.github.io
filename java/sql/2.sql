drop table if exists feed_commercial_policy;
create table feed_commercial_policy(
      feed_commercial_policy_id integer auto_increment not null,
      price_id integer not null,
      feed_id integer not null,
      merchant_id integer not null,
      primary key(feed_commercial_policy_id)
);

drop table if exists price;
create table price (
      price_id integer auto_increment not null,
      cost double not null,
      primary key(price_id)
);

drop table if exists merchant;
create table merchant (
      merchant_id integer auto_increment not null,
      merchant_name varchar(100) not null,
      primary key(merchant_id)
);

drop table if exists account_to_merchant;
create table account_to_merchant (
      account_to_merchant_id integer auto_increment not null,
      merchant_id integer not null,
      accounts_id integer not null,
      binding_type integer not null,
      primary key(account_to_merchant_id)
);

drop table if exists purchase;
create table purchase (
      purchase_id integer auto_increment not null,
      feed_id integer not null,
      buyer_account_id integer not null,
      merchant_id integer not null,
      purchase_date datetime not null,
      canceled tinyint null,
      primary key(purchase_id)
);

drop table if exists payment;
create table payment (
      payment_id integer auto_increment not null,
      paid_amount double not null,
      payment_date datetime not null,
      purchase_id integer not null,
      primary key(payment_id)
);

drop table if exists store;
create table store (
      store_id integer auto_increment not null,
      store_name varchar(100) not null,
      store_description text,
      merchant_id integer not null,
      primary key(store_id)
);