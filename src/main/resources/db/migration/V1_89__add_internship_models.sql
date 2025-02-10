create table internship_posting
(
    id            bigint not null
        primary key,
    description   varchar(255),
    planned_weeks integer,
    position      varchar(255),
    company_id    varchar(255)
        constraint should_be_company_id
            references company
);

alter table internship_posting
    owner to finki_admin;

create table internship_supervisor
(
    id         varchar(255) not null
        primary key,
    email      varchar(255),
    full_name  varchar(255),
    company_id varchar(255)
        constraint should_be_company_id
            references company
);

alter table internship_supervisor
    owner to finki_admin;

create table internship
(
    id                    bigint not null
        primary key,
    status                varchar(255)
        constraint internship_status_check
            check ((status)::text = ANY
        ((ARRAY ['ONGOING'::character varying, 'PENDING_COMPANY_REVIEW'::character varying, 'PENDING_PROFFESSOR_REVIEW'::character varying, 'DEPOSITED'::character varying])::text[])),
    posting_id            bigint
        constraint should_be_posting
            references internship_posting,
    professor_id          varchar(255)
        constraint should_be_professor_id
            references professor,
    student_index varchar(255)
        constraint should_be_student_index
            references student,
    supervisor_id         varchar(255)
        constraint should_be_supervisor_id
            references internship_supervisor
);

alter table internship
    owner to finki_admin;

create table internship_week
(
    id            bigint not null
        primary key,
    description   varchar(255),
    end_date      date,
    start_date    date,
    internship_id bigint
        constraint should_be_internship_id
            references internship
);

alter table internship_week
    owner to finki_admin;
