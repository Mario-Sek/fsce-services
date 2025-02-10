create table if not exists master_thesis_document
(
    id           bigint not null,
    thesis_id    bigint,
    type         varchar(255),
    document     oid,
    created_date date,
    constraint pk_masterthesisdocument primary key (id)
);

alter table if exists master_thesis_document
    add constraint fk_masterthesisdocument_on_diplomathesis foreign key (thesis_id) references master_thesis (id);


alter table if exists master_thesis_status_change
    add column if not exists note varchar(5000);


alter table if exists master_thesis_status_change
    rename column diploma_thesis_id to thesis_id;

create sequence if not exists master_thesis_document_seq start with 1 increment by 50;