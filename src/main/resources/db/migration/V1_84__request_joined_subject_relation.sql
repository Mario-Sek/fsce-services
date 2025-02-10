alter table if exists late_course_enrollment_student_request
    drop constraint if exists fk_late_course_enrollment_student_request_on_course_id,
    drop column if exists course_id,
    add column if not exists joined_subject_abbreviation varchar(255),
    add constraint fk_lcesr_js foreign key (joined_subject_abbreviation) references joined_subject (abbreviation);

alter table if exists course_group_change_student_request
    drop constraint if exists fk_course_group_change_student_request_on_course_id,
    drop column if exists course_id,
    add column if not exists joined_subject_abbreviation varchar(255),
    add constraint fk_cgcsr_js foreign key (joined_subject_abbreviation) references joined_subject (abbreviation);



alter table if exists course_enrollment_without_requirements_student_request
    drop constraint if exists fk_course_enrollment_without_requirements_student_request_on_co,
    drop column if exists course_id,
    add column if not exists joined_subject_abbreviation varchar(255),
    add constraint fk_cgcsr_js foreign key (joined_subject_abbreviation) references joined_subject (abbreviation);