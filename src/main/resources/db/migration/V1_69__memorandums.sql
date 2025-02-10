create table company
(
    id                  varchar(255) not null,
    name                varchar(255),
    phone               varchar(255),
    email               varchar(255),
    memorandum_package  smallint,
    company_description varchar(10000),
    website_url         varchar(255),
    logo_image          bytea,
    banner              bytea,
    active              boolean,
    constraint pk_company primary key (id)
);

create table company_subscription
(
    id                 bigint not null,
    company_id         varchar(255),
    year               smallint,
    memorandum_package varchar(255),
    status             varchar(255),
    contract           bytea,
    branded_room_name  varchar(255),
    note               varchar(20000),
    constraint pk_companysubscription primary key (id)
);

alter table company_subscription
    add constraint fk_companysubscription_on_branded_room_name foreign key (branded_room_name) references room (name);

alter table company_subscription
    add constraint fk_companysubscription_on_company foreign key (company_id) references company (id);

create sequence company_subscription_seq start with 1 increment by 50;


create table memorandum_payment_info
(
    id                      bigint not null,
    company_subscription_id bigint,
    payment_date            date,
    amount_mkd              double precision,
    note                    varchar(1000),
    constraint pk_memorandumpaymentinfo primary key (id)
);

alter table memorandum_payment_info
    add constraint fk_memorandumpaymentinfo_on_company_subscription foreign key (company_subscription_id) references company_subscription (id);

create sequence memorandum_payment_info_seq start with 1 increment by 50;

create table website_posting
(
    id         bigint not null,
    company_id varchar(255),
    title      varchar(255),
    body       varchar(20000),
    image      bytea,
    constraint pk_websiteposting primary key (id)
);

alter table website_posting
    add constraint fk_websiteposting_on_company foreign key (company_id) references company (id);

create sequence website_posting_seq start with 1 increment by 50;