alter table if exists consultation
    add column if not exists online boolean default false,
    add column if not exists student_instructions varchar(1000);