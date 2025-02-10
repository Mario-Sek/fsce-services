alter table if exists master_thesis
    rename column archive_number to application_archive_number;

alter table if exists master_thesis
    add column if not exists deadline_extension_archive_number               varchar(255),
    add column if not exists decision_for_commission_election_archive_number varchar(255),
    add column if not exists master_thesis_approval_archive_number           varchar(255),
    add column if not exists master_thesis_due_date                          date;