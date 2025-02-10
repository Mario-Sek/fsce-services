alter table if exists student_group
    add column if not exists default_size integer;