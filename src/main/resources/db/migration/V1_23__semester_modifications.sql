alter table semester
    add column if not exists start_date            date,
    add column if not exists end_date              date,
    add column if not exists enrollment_start_date date,
    add column if not exists enrollment_end_date   date;

create table if not exists semester_cycle
(
    semester_code varchar(255) not null
        constraint fk_semester_cycle_on_semester references semester (code),
    cycle         varchar(255)
);

alter table semester_exam_session
    add column if not exists enrollment_start_date date,
    add column if not exists enrollment_end_date   date,
    add column if not exists year                  varchar(255);

create table if not exists semester_exam_session_cycle
(
    semester_exam_session_name varchar(255) not null
        constraint fk_semester_exam_session_cycle_on_semester_exam_session references semester_exam_session (name),
    cycle                               varchar(255)
);
