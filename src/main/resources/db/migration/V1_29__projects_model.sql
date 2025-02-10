create table if not exists grant_holder
(
    id          varchar(255) not null
        constraint pk_grant_holder primary key,
    name        varchar(255),
    description varchar(3000)

);

create table if not exists scientific_project_programme
(
    id              bigint not null
        constraint pk_scientific_project_programme primary key,
    name            varchar(255),
    international   boolean,
    grant_holder_id varchar(255)
        constraint fk_scientific_project_programme_on_grant_holder references grant_holder (id)
);

create sequence if not exists scientific_project_programme_seq
    increment by 50;

create table if not exists scientific_project_call
(
    id                   bigint not null
        constraint pk_scientific_project_call primary key,
    name                 varchar(255),
    created_at           timestamp,
    application_deadline timestamp,
    status               varchar(255),
    programme_id         bigint
        constraint fk_scientific_project_call_on_programme references scientific_project_programme (id)
);

create sequence if not exists scientific_project_call_seq
    increment by 50;



create table if not exists scientific_project
(
    id                               bigint not null
        constraint pk_scientific_project primary key,
    name                             varchar(255),
    status                           varchar(255),
    keywords                         varchar(255),
    goals_description                varchar(10000),
    related_publications_or_projects varchar(5000),
    expected_results                 varchar(10000),
    report                           varchar(10000),
    coordinator_id                   varchar(255)
        constraint fk_scientific_project_on_professor references professor (id),
    project_call_id                  bigint
        constraint fk_scientific_project_on_call references scientific_project_call (id),
    programme_id                     bigint
        constraint fk_scientific_project_on_programme references scientific_project_programme (id)

);

create sequence if not exists scientific_project_seq
    increment by 50;



create table if not exists scientific_project_member
(
    id         bigint not null
        constraint pk_scientific_project_member primary key,
    member_id  varchar(255)
        constraint fk_scientific_project_member_on_user references auth_user (id),
    project_id bigint
        constraint fk_scientific_project_member_on_project references scientific_project (id)

);

create sequence if not exists scientific_project_member_seq
    increment by 50;

