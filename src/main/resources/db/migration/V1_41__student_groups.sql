alter table if exists student_group
    drop constraint if exists fk_student_group_on_program,
    drop column if exists program_code,
    add column if not exists programs varchar(255);

alter table if exists student_subject_enrollment
    add column if not exists group_name                  varchar(255),
    add column if not exists group_id                    bigint
        constraint fk_sse_on_student_groups references student_group (id),
    add column if not exists joined_subject_abbreviation varchar(255)
        constraint fk_sse_on_joined_subject references joined_subject (abbreviation);


insert into subject(id, name, semester)
values ('F18L3W134', 'Мултимедиски мрежи', 'WINTER');