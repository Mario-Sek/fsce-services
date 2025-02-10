create table if not exists course_preference_history_json
(
    id                           serial primary key,
    course_preference_subject_id varchar(255),
    history_json                 text,
    foreign key (course_preference_subject_id) references course_preference (subject_id)
);

alter table if exists course_preference
    add column if not exists last_update_time timestamp(6),
    add column if not exists last_update_user varchar(255);

create table if not exists joined_subject_history_json
(
    id                          serial primary key,
    joined_subject_abbreviation varchar(255),
    history_json                text,
    foreign key (joined_subject_abbreviation) references joined_subject (abbreviation)
);

alter table if exists joined_subject
    add column if not exists last_update_time timestamp(6),
    add column if not exists last_update_user varchar(255);

create table if not exists professor_engagement_history_json
(
    id                      serial primary key,
    professor_engagement_id varchar(255),
    history_json            text,
    foreign key (professor_engagement_id) references professor_engagement (id)
);

alter table if exists professor_engagement
    add column if not exists last_update_time timestamp(6),
    add column if not exists last_update_user varchar(255);

create table if not exists subject_details_history_json
(
    id                 serial primary key,
    subject_details_id varchar(255),
    history_json       text,
    foreign key (subject_details_id) references subject_details (id)
);

alter table if exists subject_details
    add column if not exists last_update_time timestamp(6),
    add column if not exists last_update_user varchar(255);

create table if not exists exam_definition_history_json
(
    id                 serial primary key,
    exam_definition_id varchar(255),
    history_json       text,
    foreign key (exam_definition_id) references exam_definition (id)
);

alter table if exists exam_definition
    add column if not exists last_update_time timestamp(6),
    add column if not exists last_update_user varchar(255);
