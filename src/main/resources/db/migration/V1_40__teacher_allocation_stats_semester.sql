alter table if exists teacher_allocation_stats
    add column semester_code varchar(255)
        constraint fk_sas_semester_code references semester (code);