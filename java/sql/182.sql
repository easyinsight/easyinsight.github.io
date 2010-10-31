alter table highrise change include_notes include_contact_notes tinyint(4) not null default 0;
alter table highrise add include_company_notes tinyint(4) not null default 0;
alter table highrise add include_deal_notes tinyint(4) not null default 0;
alter table highrise add include_case_notes tinyint(4) not null default 0;