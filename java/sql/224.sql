alter table account add(subdomain varchar(255) default null, subdomain_enabled tinyint(4) not null default 0, login_image bigint(20) default null);
alter table account add constraint account_ibfk_7 foreign key (login_image) references user_image(user_image_id);
create unique index account_subdomain_idx on account(subdomain);