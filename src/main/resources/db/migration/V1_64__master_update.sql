alter table if exists master_thesis_status_change
    add column if not exists approved boolean;


alter table if exists master_thesis_status_change
    alter column status_change_date type timestamp without time zone;