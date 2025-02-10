alter table if exists teacher_subject_allocations
    add column if not exists mentorship_course boolean default false,
    drop column if exists total_lecture_students,
    drop column if exists total_exercise_students,
    drop column if exists total_lab_students;