alter table if exists master_thesis
    add column if not exists archive_number varchar(255);
