alter table if exists semester_exam_session
    rename column "end" to session_end;

alter table if exists semester_exam_session
    rename column "start" to session_start;


alter table if exists semester_exam_session rename to year_exam_session;

alter table if exists semester_exam_session_cycle rename to year_exam_session_cycle;

alter table if exists year_exam_session_cycle
rename column semester_exam_session_name to year_exam_session_name;