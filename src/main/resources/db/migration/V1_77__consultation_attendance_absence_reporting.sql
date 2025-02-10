alter table if exists consultation_attendance
    add column if not exists report_absent_professor  boolean,
    add column if not exists report_absent_student    boolean,
    add column if not exists absent_professor_comment varchar(2000),
    add column if not exists absent_student_comment   varchar(2000);