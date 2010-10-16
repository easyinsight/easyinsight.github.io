create index data_feed_idx5 on data_feed (account_visible);

alter table token drop foreign key token_ibfk1;

create index token_idx5 on token (token_type);

# alter table highrise add include_notes tinyint(4) not null default 0;