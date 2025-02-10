create table if not exists holiday
(
    date date primary key,
    name varchar(255)
);

create table if not exists scheduled_class_session
(
    id          bigint primary key,
    course_id   bigint
        constraint fk_scs_course references course (id),
    room_name   varchar(255)
        constraint fk_scs_room references room (name),
    type        varchar(255),
    start_time  time,
    end_time    time,
    day_of_week smallint
);


create sequence if not exists scheduled_class_session_seq start with 1 increment by 50;

create table if not exists professor_class_session
(
    id                         bigint primary key,
    professor_id               varchar(255)
        constraint fk_pcs_professor references professor (id),
    scheduled_class_session_id bigint
        constraint fk_pcs_scs references scheduled_class_session (id),
    date                       date,
    professor_arrival_time     timestamp,
    attendance_token           varchar(255)
);


create sequence if not exists professor_class_session_seq start with 1 increment by 50;

create table if not exists student_attendance
(
    id                         bigint primary key,
    student_student_index      varchar(255)
        constraint fk_sa_student references student (student_index),
    professor_class_session_id bigint
        constraint fk_sa_pcs references professor_class_session (id),
    arrival_time               timestamp
);


create sequence if not exists student_attendance_seq start with 1 increment by 50;
