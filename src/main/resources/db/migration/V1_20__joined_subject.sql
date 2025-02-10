create table if not exists joined_subject
(
    abbreviation              varchar(255),
    name                      varchar(1000),
    codes                     varchar(255),
    semester_type             varchar(255),
    main_subject_id           varchar(255),
    weekly_lectures_classes   integer,
    weekly_auditorium_classes integer,
    weekly_lab_classes        integer,
    CONSTRAINT pk_abbreviation PRIMARY KEY (abbreviation),
    CONSTRAINT FK_JOINEDSUBJECT_ON_SUBJECTID FOREIGN KEY (main_subject_id) REFERENCES subject (id)
);

alter table teacher_subject_requests
    drop constraint if exists fk51lw0dpg9tf7deubmkux4ke6k,
    add constraint FK_TEACHERSUBJECTREQUESTS_ON_JOINEDSUBJECT FOREIGN KEY (subject_id) REFERENCES joined_subject (abbreviation);


alter table teacher_subject_allocations
    drop constraint if exists fkqpuv6ufrn4m5oyh5cv1ajkwt5,
    add constraint FK_TEACHERSUBJECTALOCATIONS_ON_JOINEDSUBJECT FOREIGN KEY (subject_id) REFERENCES joined_subject (abbreviation);

alter table subject_allocation_stats
    drop constraint if exists fkjt2qgwu3q8yf7amy2071l5gci,
    add constraint FK_SUBJECTALOCATIONSTATS_ON_JOINEDSUBJECT FOREIGN KEY (subject_id) REFERENCES joined_subject (abbreviation);

alter table course_preference
    drop constraint if exists fkfkgtlwvs0j9cn44typgygi537,
    add constraint FK_COURSEPREFERENCE_ON_JOINEDSUBJECT FOREIGN KEY (subject_id) REFERENCES joined_subject (abbreviation);

