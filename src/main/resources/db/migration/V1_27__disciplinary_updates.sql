update professor
set title='PROFESSOR'
WHERE id in ('sasho.panov', 'vlado.spiridonov');
update professor
set title='ASSOCIATE_PROFESSOR'
WHERE id = 'tome.eftimov';


create table if not exists disciplinary_sanction
(
    id          bigint not null
        constraint pk_disciplinary_sanction primary key,
    name        varchar(255),
    description varchar(3000)

);

create sequence if not exists disciplinary_sanction_seq
    increment by 50;

create table if not exists disciplinary_meeting
(
    id                        bigint not null
        constraint pk_disciplinary_meeting primary key,
    disciplinary_meeting_date date
);

create sequence if not exists disciplinary_meeting_seq
    increment by 50;

create table if not exists disciplinary_meeting_participant
(
    id           bigint not null
        constraint pk_disciplinary_meeting_participant primary key,
    professor_id varchar(255)
        constraint fk_disciplinary_meeting_participant_professor references professor (id),
    meeting_id   bigint
        constraint fk_disciplinary_meeting_participant_meeting references disciplinary_meeting (id)
);

create sequence if not exists disciplinary_meeting_participant_seq
    increment by 50;

alter table if exists disciplinary_decision
    drop column if exists name,
    add column record_id   bigint
        constraint fk_disciplinary_decision_on_disciplinary_record references disciplinary_record (id),
    add column sanction_id bigint
        constraint fk_disciplinary_decision_on_disciplinary_sanction references disciplinary_sanction (id);



alter table if exists disciplinary_record
    drop column if exists decision_description,
    drop constraint if exists fk_disciplinaryrecord_on_decisionid,
    drop column if exists decision_id,
    add column if not exists notified_student                   boolean,
    add column if not exists student_last_access                timestamp,
    add column if not exists admitted_by_student                boolean,
    add column if not exists student_note                       varchar(10000),
    add column if not exists student_last_note                  timestamp,
    add column if not exists suggested_disciplinary_sanction_id bigint
        constraint fk_disciplinary_record_on_sanction references disciplinary_sanction (id),
    add column if not exists meeting_id                         bigint
        constraint fk_disciplinary_record_on_meeting references disciplinary_meeting (id);


