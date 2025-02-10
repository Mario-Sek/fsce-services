alter table if exists student_subject_enrollment
    add column if not exists professors varchar(1000),
    add column if not exists assistants varchar(1000);


