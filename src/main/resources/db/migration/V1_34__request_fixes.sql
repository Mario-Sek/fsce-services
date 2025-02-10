alter table if exists change_study_program_student_request
    add column if not exists rejection_reason varchar(3000);

alter table if exists general_student_request
    add column if not exists rejection_reason varchar(3000);
alter table if exists installment_payment_student_request
    add column if not exists rejection_reason varchar(3000);
alter table if exists late_course_enrollment_student_request
    add column if not exists rejection_reason varchar(3000);

alter table if exists course_enrollment_without_requirementsl_student_request
    add column if not exists rejection_reason varchar(3000);
alter table if exists course_group_changel_student_request
    add column if not exists rejection_reason varchar(3000);

alter table if exists course_enrollment_without_requirementsl_student_request
rename to course_enrollment_without_requirements_student_request;

alter table if exists course_group_changel_student_request
    add column if not exists rejection_reason varchar(3000);



alter sequence if exists course_enrollment_without_requirementsl_student_request_seq
    rename to course_enrollment_without_requirements_student_request_seq;

alter sequence if exists course_group_changel_student_request_seq
    rename to course_group_change_student_request_seq;