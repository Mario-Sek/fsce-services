alter table if exists master_thesis_status_change
    add column if not exists next_status_processing_due_date date;