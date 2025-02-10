alter table if exists professor
    add column if not exists ordering_rank smallint;


alter table if exists teacher_subject_requests
    add column if not exists started_teaching_from_code varchar(255);

alter table if exists teacher_subject_requests
    add column if not exists started_exercise_from_code varchar(255);



alter table if exists teacher_subject_requests
    add constraint fk_teacher_subject_requests_started_teaching_from
        foreign key (started_teaching_from_code)
            references semester (code);

alter table if exists teacher_subject_requests
    add constraint fk_teacher_subject_requests_started_exercise_from
        foreign key (started_exercise_from_code)
            references semester (code);



alter table if exists student_subject_enrollment
    add column if not exists professor_id varchar(255);


alter table if exists student_subject_enrollment
    add constraint fk_student_subject_enrollment_professor
        foreign key (professor_id)
            references professor (id);



create table if not exists professor_resume
(
    biography text,
    image     bytea
);


alter table if exists subject_details
    add column if not exists dependency_type varchar(255);