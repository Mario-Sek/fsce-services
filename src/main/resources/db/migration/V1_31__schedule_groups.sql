create table if not exists student_group
(
    id              bigint not null
        constraint pk_student_group primary key,
    name            varchar(255),
    study_year      smallint,
    last_name_regex varchar(255),
    semester_code   varchar(255)
        constraint fk_student_group_on_semester references semester (code),
    program_code    varchar(255)
        constraint fk_student_group_on_program references study_program (code)
);

create sequence if not exists student_group_seq
    increment by 50;

create table if not exists joined_subject_group
(
    id                          bigint not null
        constraint pk_joined_subject_group primary key,

    study_year                  smallint,
    last_name_regex             varchar(255),
    semester_code               varchar(255)
        constraint fk_joined_subject_group_on_semester references semester (code),
    joined_subject_abbreviation varchar(255)
        constraint fk_joined_subject_group_on_joined_subject references joined_subject (abbreviation),
    professor_id                varchar(255)
        constraint fk_joined_subject_group_on_professor references professor (id),
    assistant_id                varchar(255)
        constraint fk_joined_subject_group_on_assistant references professor (id)
);

create sequence if not exists joined_subject_group_seq
    increment by 50;

create table if not exists joined_subject_group_rooms
(
    joined_subject_group_id bigint       not null
        constraint fk_joined_subject_group_room_on_jsg references joined_subject_group (id),
    rooms_name              varchar(255) not null
        constraint fk_joined_subject_group_rooms_on_rooms references room (name)
);

create table if not exists joined_subject_group_student_groups
(
    joined_subject_group_id bigint not null
        constraint fk_joined_subject_group_student_groups_on_jsg references joined_subject_group (id),
    student_groups_id       bigint not null
        constraint fk_joined_subject_group_student_groups_on_sg references student_group (id)
);
