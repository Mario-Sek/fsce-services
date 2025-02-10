alter table results
    drop constraint if exists fkg30pq3ucxw909sr3u1o8e7klg,
    drop column if exists course_id,
    add column if not exists course_group_id bigint
        constraint fk_results_on_course_group references course_group (id);

alter table accreditation
    drop column if exists accreditation_decision_document;

create table if not exists study_program_accreditation_document
(
    id               bigint not null
        constraint pk_study_program_accreditation_document primary key,
    name             varchar(255),
    type             varchar(255),
    study_program_id varchar(255)
        constraint fk_accreditation_document_on_study_program_details references study_program_details (id),
    creation_date    timestamp(6),
    document         oid
);


create sequence if not exists study_program_accreditation_document_seq
    increment by 50;


