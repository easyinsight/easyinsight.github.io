alter table account add opt_in_email tinyint(4) not null default 0;

alter table analysis_item_to_link drop foreign key analysis_item_to_link_ibfk1;
alter table analysis_item_to_link add constraint analysis_item_to_link_ibfk1 foreign key (analysis_item_id) references analysis_item (analysis_item_id) on delete cascade;

alter table analysis_item_to_link drop foreign key analysis_item_to_link_ibfk2;
alter table analysis_item_to_link add constraint analysis_item_to_link_ibfk2 foreign key (link_id) references link (link_id) on delete cascade;