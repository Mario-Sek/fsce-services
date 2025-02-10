alter table if exists master_thesis
    add column if not exists last_update    timestamp without time zone,
    add column if not exists coordinator_id varchar(255)
        constraint master_thesis_coordinator references professor (id);


