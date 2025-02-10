SELECT setval('teacher_subject_requests_seq', 500, true);

SELECT setval('teacher_subject_allocations_seq', 500, true);

alter table course_preference
    drop constraint if exists course_preference_pkey,
    drop column if exists id,
    add constraint course_preference_pkey primary key (subject_id);

alter table joined_subject_group
    add column if not exists number_of_first_enrollments integer,
    add column if not exists number_of_re_enrollments    integer;