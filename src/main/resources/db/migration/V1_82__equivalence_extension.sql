create table if not exists equivalent_subject_mapping
(
    target_subject_code      varchar(255) not null
        constraint equivalent_subject_mapping_pkey
            primary key,
    equivalent_subject_codes varchar(255) not null
);


alter table if exists student_grades
    rename to student_grade;


alter table if exists student_grade
    add equivalented             boolean default false,
    add equivalent_grade_codes   varchar(500),
    add study_program_subject_id varchar(255)
        constraint student_grade_study_program_subject_id_fk
            references study_program_subject (id);