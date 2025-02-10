alter table if exists student_subject_enrollment
    add column if not exists course_id bigint
        constraint fk_student_subject_enrollment_course
            references course (id);