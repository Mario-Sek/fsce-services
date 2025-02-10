create table if not exists professor_engagement
(
    id                        varchar(255) not null
        constraint pk_professor_engagement primary key,
    semester_code             varchar(255) not null
        constraint fk_peng_semester references semester (code),
    subject_abbreviation      varchar(255) not null
        constraint fk_peng_subject references joined_subject (abbreviation),
    class_type                varchar(255),
    number_of_classes         smallint,
    shared_with_other_teacher bool,
    language                  varchar(255),
    number_of_students        smallint,
    consultative              bool,
    note                      varchar(2000),
    professor_id              varchar(255) not null
        constraint fk_peng_professor references professor (id)
)