DROP TABLE IF EXISTS `user_to_license_subscription`;
CREATE TABLE `user_to_license_subscription` (
  `user_to_license_subscription_id` int(11) NOT NULL auto_increment,
  `user_id` int(11) NOT NULL,
  `license_subscription_id` int(11) NOT NULL,
  PRIMARY KEY  (`user_to_license_subscription_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `license_subscription`;
CREATE TABLE `license_subscription` (
  `license_subscription_id` int(11) NOT NULL auto_increment,
  `feed_id` int(11) NOT NULL,
  user_id integer not null,
  `account_id` int(11) NOT NULL,
  PRIMARY KEY  (`license_subscription_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table if exists analysis_calculation;
create table analysis_calculation(
    analysis_calculation_id integer not null auto_increment,
    analysis_item_id integer,
    calculation_string text not null,
    primary key(analysis_calculation_id)
);

drop table if exists flat_file_upload_format;
create table flat_file_upload_format (
    flat_file_upload_format_id integer not null auto_increment,
    feed_id integer not null,
    delimiter_escape varchar(5) not null,
    delimiter_pattern varchar(5) not null,
    primary key(flat_file_upload_format_id)
);

drop table if exists excel_upload_format;
create table excel_upload_format (
    excel_upload_format_id integer not null auto_increment,
    feed_id integer not null,
    primary key(excel_upload_format_id)
);