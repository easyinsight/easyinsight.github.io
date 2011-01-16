alter table user modify column password varchar(255);
alter table user add column hash_salt varchar(255);
alter table user add column hash_type varchar(255) default 'SHA';
