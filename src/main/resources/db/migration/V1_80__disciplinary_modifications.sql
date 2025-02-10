alter table if exists disciplinary_record
    add column if not exists decision_id bigint
        constraint fk_record_deceision_id
            references disciplinary_decision (id);

alter table if exists disciplinary_record
    drop constraint if exists fk_disciplinary_record_on_course_id;

alter table if exists disciplinary_record
    drop column if exists course_id;

alter table if exists disciplinary_record
    add column if not exists joined_subject_abbreviation varchar(255)
        constraint fk_record_subject_abbreviation
            references joined_subject (abbreviation);


delete
from disciplinary_type
where length(id) > 0;

INSERT INTO disciplinary_type (id, name)
VALUES ('1', 'Препишување'),
       ('2', 'Навреда на професор'),
       ('3', 'Напад на професор');
