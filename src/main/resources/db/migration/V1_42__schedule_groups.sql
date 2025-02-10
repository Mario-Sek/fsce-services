alter table if exists joined_subject_group
    add column if not exists group_portion float default 1,
    add column if not exists professors    varchar(1000),
    add column if not exists assistants    varchar(1000);

