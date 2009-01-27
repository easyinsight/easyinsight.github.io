DROP TABLE IF EXISTS account;
CREATE TABLE account (
    account_id integer auto_increment not null,
    account_type int(11) not null,    
    primary key(account_id)
);

DROP TABLE IF EXISTS user;
CREATE TABLE user (
    user_id int(11) NOT NULL auto_increment not null,
    account_id int(11) default NULL,
    password varchar(40) NOT NULL,
    name varchar(60) NOT NULL,
    email varchar(60) NOT NULL,
    username varchar(40) NOT NULL,
    permissions int(11) NOT NULL,    
  PRIMARY KEY  (user_id)
);

DROP TABLE IF EXISTS user_to_license_subscription;
CREATE TABLE user_to_license_subscription (
    user_to_license_subscription_id int(11) NOT NULL,
    user_id int(11) NOT NULL,
    license_subscription_id int(11) NOT NULL,
    primary key(user_to_license_subscription_id)
);

DROP TABLE IF EXISTS license_subscription;
CREATE TABLE license_subscription (
    license_subscription_id int(11) NOT NULL,
    feed_id int(11) NOT NULL,
    account_id int(11) NOT NULL,
    primary key(license_subscription_id)
);