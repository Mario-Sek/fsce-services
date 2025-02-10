create table disciplinary_decision
(
    id          bigint not null,
    description varchar(3000),
    name        varchar(255),
    primary key (id)
);

create table disciplinary_record
(
    id                    bigint not null,
    decision_description  varchar(10000),
    description           varchar(10000),
    reporting_date        date,
    severity              float4,
    course_id             varchar(255),
    decision_id           bigint,
    reporter_id           varchar(255),
    student_student_index varchar(255),
    type_id               varchar(255),
    status                varchar(255),
    primary key (id)
);

create table disciplinary_type
(
    id   varchar(255) not null,
    name varchar(255),
    primary key (id)
);

create sequence disciplinary_decision_seq start with 1 increment by 50;

create sequence disciplinary_record_seq start with 1 increment by 50;

alter table if exists disciplinary_record
    add constraint FK_DISCIPLINARYRECORD_ON_COURSEID
        foreign key (course_id) references course;

alter table if exists disciplinary_record
    add constraint FK_DISCIPLINARYRECORD_ON_DECISIONID
        foreign key (decision_id) references disciplinary_decision;

alter table if exists disciplinary_record
    add constraint FK_DISCIPLINARYRECORD_ON_REPORTERID
        foreign key (reporter_id) references professor;

alter table if exists disciplinary_record
    add constraint FK_DISCIPLINARYRECORD_ON_STUDENTINDEX
        foreign key (student_student_index) references student;

alter table if exists disciplinary_record
    add constraint FK_DISCIPLINARYRECORD_ON_TYPEID
        foreign key (type_id) references disciplinary_type;

alter table if exists study_program_subject
    add constraint FK_STUDYPROGRAMSUBJECT_ON_SUBJECTID
        foreign key (subject_id) references subject;