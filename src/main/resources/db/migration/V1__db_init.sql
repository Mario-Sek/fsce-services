-- create table if not exists public.flyway_schema_history
-- (
--     installed_rank integer                 not null
--     constraint flyway_schema_history_pk
--     primary key,
--     version        varchar(50),
--     description    varchar(200)            not null,
--     type           varchar(20)             not null,
--     script         varchar(1000)           not null,
--     checksum       integer,
--     installed_by   varchar(100)            not null,
--     installed_on   timestamp default now() not null,
--     execution_time integer                 not null,
--     success        boolean                 not null
--     );
--
-- alter table public.flyway_schema_history
--     owner to finki_admin;
--
-- create index if not exists flyway_schema_history_s_idx
--     on public.flyway_schema_history (success);

create table if not exists public.professor
(
    id    varchar(255) not null
    primary key,
    email varchar(255) not null
    constraint uk_qjm28ojevoom770jyieljec3e
    unique,
    name  varchar(255) not null,
    title varchar(255)
    );

alter table public.professor
    owner to finki_admin;

create table if not exists public.room
(
    name                  varchar(255) not null
    primary key,
    capacity              bigint,
    equipment_description varchar(255),
    location_description  varchar(255),
    type                  smallint
    );

alter table public.room
    owner to finki_admin;

create table if not exists public.consultation
(
    id           bigint not null
    primary key,
    date         date,
    day_of_week  varchar(255),
    end_time     time,
    start_time   time,
    type         varchar(255),
    professor_id varchar(255)
    constraint fkcidmxf4i6xbgrtjgnp59o1yu3
    references public.professor,
    room_name    varchar(255)
    constraint fkcbmktcrggiuktgxl1k0qiymhd
    references public.room
    );

alter table public.consultation
    owner to finki_admin;

create table if not exists public.course_exam_part_rooms
(
    course_exam_part_id varchar(255) not null,
    rooms_name          varchar(255) not null
    constraint fkoiyy9i7kav0g82d4h4239jk9f
    references public.room,
    primary key (course_exam_part_id, rooms_name)
    );

alter table public.course_exam_part_rooms
    owner to finki_admin;

create table if not exists public.semester
(
    code          varchar(255) not null
    primary key,
    semester_type varchar(255),
    year          varchar(255)
    );

alter table public.semester
    owner to finki_admin;

create table if not exists public.request_session
(
    id            bigint not null
    primary key,
    approval_note oid,
    description   oid,
    request_type  varchar(255),
    time_from     timestamp(6),
    time_to       timestamp(6),
    semester_code varchar(255)
    constraint fk9doc8v53ublhrq04lsmw3n38y
    references public.semester
    );

alter table public.request_session
    owner to finki_admin;

create table if not exists public.study_program
(
    code varchar(255) not null
    primary key,
    name varchar(255)
    );

alter table public.study_program
    owner to finki_admin;

create table if not exists public.student
(
    student_index      varchar(255) not null
    primary key,
    email              varchar(255),
    last_name          varchar(255),
    name               varchar(255),
    parent_name        varchar(255),
    study_program_code varchar(255)
    constraint fk43esuweu3j4cjkbxrv9sev9x8
    references public.study_program
    );

alter table public.student
    owner to finki_admin;

create table if not exists public.change_study_program_student_request
(
    id                     bigint not null
    primary key,
    date_created           date,
    date_processed         date,
    description            oid,
    is_approved            boolean,
    is_processed           boolean,
    request_session_id     bigint
    constraint fk504h8oqgh89ujed3bmk8o3v5l
    references public.request_session,
    student_student_index  varchar(255)
    constraint fk9ttbo8ejnsrhpvbj2saqy5w3c
    references public.student,
    new_study_program_code varchar(255)
    constraint fkjvchu2vw3b91svp0o3axml130
    references public.study_program,
    old_study_program_code varchar(255)
    constraint fklph4cmikhhdrbptvhrr2jkrc9
    references public.study_program
    );

alter table public.change_study_program_student_request
    owner to finki_admin;

create table if not exists public.completed_student_semester_evaluation
(
    id                    bigint not null
    primary key,
    semester_code         varchar(255)
    constraint fkt7ujvi91eisvgaph768twx50s
    references public.semester,
    student_student_index varchar(255)
    constraint fk4u7wwd2ah3janopor0hymdbd7
    references public.student
    );

alter table public.completed_student_semester_evaluation
    owner to finki_admin;

create table if not exists public.diploma_thesis
(
    id                      bigint not null
    primary key,
    area                    varchar(255),
    description             oid,
    grade                   integer,
    location                varchar(255),
    presentation_start_time timestamp(6),
    status                  varchar(255),
    thesis_text             oid,
    title                   varchar(255),
    first_member_id         varchar(255)
    constraint fkllpc20eohf4wdq8rcsb7f1fhh
    references public.professor,
    mentor_id               varchar(255)
    constraint fk8qie50iwirtddp87hyxfk87ke
    references public.professor,
    second_member_id        varchar(255)
    constraint fklg5n8hp55k0iqa9kc2vyvning
    references public.professor,
    student_student_index   varchar(255)
    constraint fkh8oh7mbegohopmuvxwvd1v2kf
    references public.student
    );

alter table public.diploma_thesis
    owner to finki_admin;

create table if not exists public.diploma_thesis_status_change
(
    id                   bigint not null
    primary key,
    next_status          varchar(255),
    status_change_date   date,
    diploma_thesis_id    bigint
    constraint fkp5jwt0rdv923ybtuvbyo2v5pv
    references public.diploma_thesis,
    status_changed_by_id varchar(255)
    );

alter table public.diploma_thesis_status_change
    owner to finki_admin;

create table if not exists public.general_student_request
(
    id                    bigint not null
    primary key,
    date_created          date,
    date_processed        date,
    description           oid,
    is_approved           boolean,
    is_processed          boolean,
    request_session_id    bigint
    constraint fkku0r1h53t32gle23tkpas2rk7
    references public.request_session,
    student_student_index varchar(255)
    constraint fkpaqd0sdun5bes7ve91aktipco
    references public.student
    );

alter table public.general_student_request
    owner to finki_admin;

create table if not exists public.installment_payment_student_request
(
    id                    bigint not null
    primary key,
    date_created          date,
    date_processed        date,
    description           oid,
    is_approved           boolean,
    is_processed          boolean,
    installments_num      integer,
    request_session_id    bigint
    constraint fk3odnsv3brgsi6ydf7rab3tf4b
    references public.request_session,
    student_student_index varchar(255)
    constraint fkosxifs1cxlbbn9uhsa88qmuyp
    references public.student
    );

alter table public.installment_payment_student_request
    owner to finki_admin;

create table if not exists public.subject
(
    id                        varchar(255) not null
    primary key,
    name                      varchar(255) not null,
    semester                  varchar(255),
    weekly_auditorium_classes integer,
    weekly_lab_classes        integer,
    weekly_lectures_classes   integer
    );

alter table public.subject
    owner to finki_admin;

create table if not exists public.course
(
    id                   varchar(255) not null
    primary key,
    total_students       bigint,
    total_teaching_staff bigint,
    semester_code        varchar(255)
    constraint fkjyy9lqlqj4udroainv4ih92pm
    references public.semester,
    subject_id           varchar(255)
    constraint fkm1expnaas0onmafqpktmjixnx
    references public.subject
    );

alter table public.course
    owner to finki_admin;

create table if not exists public.course_enrollment_without_requirementsl_student_request
(
    id                    bigint not null
    primary key,
    date_created          date,
    date_processed        date,
    description           oid,
    is_approved           boolean,
    is_processed          boolean,
    request_session_id    bigint
    constraint fkdcbdkfgjpnk5s6fjox1qc9yhp
    references public.request_session,
    student_student_index varchar(255)
    constraint fkdbww4j28i8e247b0esvodcocw
    references public.student,
    course_id             varchar(255)
    constraint fkskcy5qqe0pa2kfpmo578pew8s
    references public.course
    );

alter table public.course_enrollment_without_requirementsl_student_request
    owner to finki_admin;

create table if not exists public.course_group_changel_student_request
(
    id                    bigint not null
    primary key,
    date_created          date,
    date_processed        date,
    description           oid,
    is_approved           boolean,
    is_processed          boolean,
    request_session_id    bigint
    constraint fkjyewgyc5ii1m59elo2nxxvdou
    references public.request_session,
    student_student_index varchar(255)
    constraint fkfsh6k919qdavjs4o096qda9q1
    references public.student,
    course_id             varchar(255)
    constraint fkbh23g6f06r4tfsh4b3e9e7fed
    references public.course,
    current_professor_id  varchar(255)
    constraint fknk5oyut7uo7wejknvsy3gd621
    references public.professor,
    new_professor_id      varchar(255)
    constraint fk1ixhdnumxd6kmwdrq1ridlxel
    references public.professor
    );

alter table public.course_group_changel_student_request
    owner to finki_admin;

create table if not exists public.course_preference
(
    id                             varchar(255) not null
    primary key,
    auditorium_exercises_sharing   varchar(255),
    lab_exercises_as_consultations boolean      not null,
    lecture_sharing                varchar(255),
    prefer_online_exercises        boolean      not null,
    prefer_online_lectures         boolean      not null,
    subject_id                     varchar(255)
    constraint fkfkgtlwvs0j9cn44typgygi537
    references public.subject
    );

alter table public.course_preference
    owner to finki_admin;

create table if not exists public.course_professor_evaluation
(
    id           bigint   not null
    primary key,
    comment      oid,
    grade        smallint not null,
    course_id    varchar(255)
    constraint fkt05dm8g18bgphhbd6aamt5jb9
    references public.course,
    professor_id varchar(255)
    constraint fkatm36akd8gfwm2mgh6vejckw4
    references public.professor
    );

alter table public.course_professor_evaluation
    owner to finki_admin;

create table if not exists public.late_course_enrollment_student_request
(
    id                    bigint not null
    primary key,
    date_created          date,
    date_processed        date,
    description           oid,
    is_approved           boolean,
    is_processed          boolean,
    professor_approved    boolean,
    request_session_id    bigint
    constraint fkejufrerc8cyni4q2456xlrt0x
    references public.request_session,
    student_student_index varchar(255)
    constraint fkquvbw6j346bb7v0ja1ox2yqhs
    references public.student,
    course_id             varchar(255)
    constraint fk3pbt4gv245o4nchx6gwlhi1yu
    references public.course,
    professor_id          varchar(255)
    constraint fkns7jlha09b7ydeb2g7cd4cvx
    references public.professor
    );

alter table public.late_course_enrollment_student_request
    owner to finki_admin;

create table if not exists public.results
(
    id           bigint not null
    primary key,
    pdf_bytes    oid,
    course_id    varchar(255)
    constraint fkg30pq3ucxw909sr3u1o8e7klg
    references public.course,
    session_name varchar(255)
    );

alter table public.results
    owner to finki_admin;

create table if not exists public.student_courses
(
    id                    bigint not null
    primary key,
    course_id             varchar(255)
    constraint fkc614in0kdhj9sih7vw304qxgj
    references public.course,
    student_student_index varchar(255)
    constraint fkb3ip19m0bcg43g2vx1j74nog2
    references public.student
    );

alter table public.student_courses
    owner to finki_admin;

create table if not exists public.subject_allocation_stats
(
    id                               varchar(255) not null
    primary key,
    average_students_per_group       integer,
    calculated_number_of_groups      integer,
    covered_exercise_groups          real,
    covered_lab_groups               real,
    covered_lecture_groups           real,
    number_of_first_time_students    integer,
    number_of_groups                 integer,
    number_of_re_enrollment_students integer,
    number_of_teachers               integer,
    subject_id                       varchar(255)
    constraint fkjt2qgwu3q8yf7amy2071l5gci
    references public.subject
    );

alter table public.subject_allocation_stats
    owner to finki_admin;

create table if not exists public.teacher_allocation_stats
(
    id                           varchar(255) not null
    primary key,
    number_of_exercise_en_groups real,
    number_of_exercise_groups    real,
    number_of_exercise_subjects  real,
    number_of_lab_groups         real,
    number_of_lab_subjects       real,
    number_of_lecture_en_groups  real,
    number_of_lecture_groups     real,
    number_of_lecture_subjects   real,
    total_exercise_students      integer,
    total_lab_students           integer,
    total_lecture_students       integer,
    professor_id                 varchar(255)
    constraint fk458o59l11uimriau7u5scokh3
    references public.professor
    );

alter table public.teacher_allocation_stats
    owner to finki_admin;

create table if not exists public.teacher_courses
(
    id           bigint not null
    primary key,
    course_id    varchar(255)
    constraint fk9rq50frjy5ol4a1wleykjaqv2
    references public.course,
    professor_id varchar(255)
    constraint fka81mirlsq3f0nenmb2ayic9wc
    references public.professor
    );

alter table public.teacher_courses
    owner to finki_admin;

create table if not exists public.teacher_subject_allocations
(
    id                        bigint not null
    primary key,
    number_of_exercise_groups real,
    number_of_lab_groups      real,
    number_of_lecture_groups  real,
    total_exercise_classes    real,
    total_exercise_students   integer,
    total_lab_classes         real,
    total_lab_students        integer,
    total_lecture_classes     real,
    total_lecture_students    integer,
    professor_id              varchar(255)
    constraint fkmttbk1pjavwwh773g1isf0t4m
    references public.professor,
    semester_code             varchar(255)
    constraint fk8x5hnrat2qedlefb492ppibq3
    references public.semester,
    subject_id                varchar(255)
    constraint fkqpuv6ufrn4m5oyh5cv1ajkwt5
    references public.subject
    );

alter table public.teacher_subject_allocations
    owner to finki_admin;

create table if not exists public.teacher_subject_requests
(
    id                          bigint not null
    primary key,
    note                        varchar(255),
    prefer_auditorium_exercises boolean,
    prefer_lab_exercises        boolean,
    prefer_multiple_groups      boolean,
    priority                    double precision,
    schedule_preferences        varchar(255),
    professor_id                varchar(255)
    constraint fk9f5twelated1y8ljq9chnlu1h
    references public.professor,
    subject_id                  varchar(255)
    constraint fk51lw0dpg9tf7deubmkux4ke6k
    references public.subject
    );

alter table public.teacher_subject_requests
    owner to finki_admin;

create table if not exists public.teacher_subjects
(
    id           bigint not null
    primary key,
    professor_id varchar(255)
    constraint fkn7wxup9twrg8sniabj2p8liqe
    references public.professor,
    subject_id   varchar(255)
    constraint fkn680sbj5cc0wo9yeajo62gfwb
    references public.subject
    );

alter table public.teacher_subjects
    owner to finki_admin;


CREATE SEQUENCE IF NOT EXISTS change_study_program_student_request_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS completed_student_semester_evaluation_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS consultation_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS course_enrollment_without_requirementsl_student_request_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS course_group_changel_student_request_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS course_professor_evaluation_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS diploma_thesis_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS diploma_thesis_status_change_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS general_student_request_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS installment_payment_student_request_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS late_course_enrollment_student_request_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS request_session_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS results_sequence START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS student_courses_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS teacher_courses_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS teacher_subject_allocations_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS teacher_subject_requests_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS teacher_subjects_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE course_exam_part
(
    id                              VARCHAR(255) NOT NULL,
    course_id                       VARCHAR(255),
    session_name                    VARCHAR(255),
    name                            VARCHAR(255),
    type                            VARCHAR(255),
    duration_minutes                BIGINT,
    previous_year_attendants_number BIGINT,
    attendants_number               BIGINT,
    num_repetitions                 BIGINT,
    "from"                          TIMESTAMP WITHOUT TIME ZONE,
    "to"                            TIMESTAMP WITHOUT TIME ZONE,
    comment                         OID,
    CONSTRAINT pk_courseexampart PRIMARY KEY (id)
);

CREATE TABLE semester_exam_session
(
    name          VARCHAR(255) NOT NULL,
    session       VARCHAR(255),
    semester_code VARCHAR(255),
    start         date,
    "end"         date,
    CONSTRAINT pk_semesterexamsession PRIMARY KEY (name)
);

CREATE TABLE "user"
(
    id    VARCHAR(255) NOT NULL,
    name  VARCHAR(255),
    email VARCHAR(255),
    role  VARCHAR(255),
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE course_exam_part
    ADD CONSTRAINT FK_COURSEEXAMPART_ON_COURSE FOREIGN KEY (course_id) REFERENCES course (id);

ALTER TABLE course_exam_part
    ADD CONSTRAINT FK_COURSEEXAMPART_ON_SESSION_NAME FOREIGN KEY (session_name) REFERENCES semester_exam_session (name);

ALTER TABLE diploma_thesis_status_change
    ADD CONSTRAINT FK_DIPLOMATHESISSTATUSCHANGE_ON_STATUSCHANGEDBY FOREIGN KEY (status_changed_by_id) REFERENCES "user" (id);

ALTER TABLE results
    ADD CONSTRAINT FK_RESULTS_ON_SESSION_NAME FOREIGN KEY (session_name) REFERENCES semester_exam_session (name);

ALTER TABLE semester_exam_session
    ADD CONSTRAINT FK_SEMESTEREXAMSESSION_ON_SEMESTER_CODE FOREIGN KEY (semester_code) REFERENCES semester (code);

ALTER TABLE course_exam_part_rooms
    ADD CONSTRAINT fk_couexaparroo_on_course_exam_part FOREIGN KEY (course_exam_part_id) REFERENCES course_exam_part (id);