DROP TABLE IF EXISTS filter;
CREATE TABLE filter (
  filter_id int(11) NOT NULL auto_increment,
  filter_type varchar(40) default NULL,
  analysis_id int(11) default NULL,
  inclusive tinyint(4) default NULL,
  optional tinyint(4) default NULL,
  analysis_item_id int(11) default NULL,
  PRIMARY KEY  (filter_id)
);

drop table if exists value_based_filter;
create table value_based_filter (
    value_based_filter_id integer auto_increment not null,
    filter_id integer,
    inclusive tinyint(4) NOT NULL,
    primary key(value_based_filter_id)
);

DROP TABLE IF EXISTS filter_value;
CREATE TABLE filter_value (
  filter_value_id int(11) NOT NULL auto_increment,
  value_based_filter_id int(11) default NULL,
  filter_value varchar(100) default NULL,
  PRIMARY KEY  (filter_value_id)
);

drop table if exists range_filter;
create table range_filter (
    range_filter_id integer auto_increment not null,
    filter_id integer,
    low_value double not null,
    high_value double not null,
    primary key(range_filter_id)
);

drop table if exists date_range_filter;
create table date_range_filter (
    date_range_filter_id integer auto_increment not null,
    filter_id integer,
    low_value datetime not null,
    high_value datetime not null,
    primary key(date_range_filter_id)
);