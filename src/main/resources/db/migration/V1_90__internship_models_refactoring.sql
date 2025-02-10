drop table if exists internship_week;

drop table if exists internship;

drop table if exists internship_posting;

drop table if exists internship_supervisor;


create table internship
(
    id            bigint not null
        primary key,
    status        varchar(255),
    professor_id  varchar(255)
        constraint should_be_professor_id
            references professor,
    student_index varchar(255)
        constraint should_be_student_index
            references student,
    description   varchar(255)
);

alter table internship
    owner to finki_admin;


create table internship_week
(
    id                  bigint not null
        primary key,
    description         varchar(255),
    end_date            date,
    start_date          date,
    internship_id       bigint
        constraint should_be_internship_id
            references internship,
    company_comment     varchar(255),
    coordinator_comment varchar(255)
);

alter table internship_week
    owner to finki_admin;
