create table academic_title
(
    id                       varchar(255) not null,
    area                     varchar(255),
    decision_document_number smallint,
    election_year            smallint,
    institution              varchar(255),
    title                    varchar(255),
    CONSTRAINT PK_ACADEMICTITLE_ID primary key (id)
);

create table education
(
    id             varchar(255) not null,
    area           varchar(255),
    degree         varchar(255),
    discipline     varchar(255),
    field          varchar(255),
    finishing_year smallint,
    institution    varchar(255),
    CONSTRAINT PK_EDUCATION_ID primary key (id)
);

create table professor_academic_titles
(
    id                varchar(255) not null,
    academic_title_id varchar(255),
    professor_id      varchar(255),
    CONSTRAINT PK_PROFESSORACCADEMICTITLES_ID primary key (id),
    constraint FK_PROFESSORACADEMICTITLES_ON_ACADEMICTITLE foreign key (academic_title_id) references academic_title,
    constraint FK_PROFESSORACADEMICTITLES_ON_PROFESSORDETAILS foreign key (professor_id) references professor_details
);

create table professor_education
(
    id            varchar(255) not null,
    display_order float,
    education_id  varchar(255),
    professor_id  varchar(255),
    CONSTRAINT PK_PROFESSOREDUCATION_ID   primary key (id),
    constraint FK_PROFESSOREDUCATION_ON_EDUCATION foreign key (education_id) references education,
    constraint FK_PROFESSOREDUCATION_ON_PROFESSORDETAILS foreign key (professor_id) references professor_details
);

alter table if exists professor_details
    add column birth_day        date,
    add column degree           varchar(255),
    add column degree_title     varchar(255),
    add column current_title_id varchar(255),
    add constraint FK_PROFESSORDETAILS_ON_PROFESSORACADEMICTITLES foreign key (current_title_id) references professor_academic_titles;