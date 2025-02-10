create table student_equivalence_request
(
    id                     varchar(255) not null,
    student_student_index  varchar(255),
    old_study_program_code varchar(255),
    new_study_program_code varchar(255),
    status                 varchar(255),
    constraint pk_studentequivalencerequest primary key (id)
);

alter table student_equivalence_request
    add constraint fk_studentequivalencerequest_on_new_study_program_code foreign key (new_study_program_code) references study_program (code);

alter table student_equivalence_request
    add constraint fk_studentequivalencerequest_on_old_study_program_code foreign key (old_study_program_code) references study_program (code);

alter table student_equivalence_request
    add constraint fk_studentequivalencerequest_on_student_student_index foreign key (student_student_index) references student (student_index);


create table student_grades
(
    id                    varchar(255) not null,
    student_student_index varchar(255),
    subject_id            varchar(255),
    grade_date            timestamp without time zone,
    grade                 smallint,
    ects_grade            varchar(255),
    constraint pk_studentgrades primary key (id)
);

alter table student_grades
    add constraint fk_studentgrades_on_student_student_index foreign key (student_student_index) references student (student_index);

alter table student_grades
    add constraint fk_studentgrades_on_subject foreign key (subject_id) references subject (id);