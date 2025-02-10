alter table if exists teacher_subject_allocations
    add column if not exists validation_message varchar(4000);

create table if not exists exam_definition
(
    id                   varchar(255) NOT NULL
        constraint pk_examdefinition primary key,
    subject_abbreviation varchar(255)
        constraint FK_EXAMDEFINITION_ON_SUBJECT_ABBREVIATION references joined_subject (abbreviation),
    exam_session         varchar(255),
    duration_minutes     bigint,
    type                 varchar(255),
    note                 varchar(255)

);

alter table if exists course_exam_part
    rename to subject_exam;


alter table if exists subject_exam
    drop constraint if exists fk_courseexampart_on_course,
    drop column if exists course_id,
    drop column if exists name,
    drop column if exists type,
    add column if not exists definition_id                varchar(255)
        constraint fk_se_examdef_id references exam_definition (id),
    add column if not exists previous_year_total_students BIGINT,
    add column if not exists total_students               BIGINT,
    add column if not exists expected_number              BIGINT;

alter table if exists semester
    add column if not exists state varchar(255);

alter table if exists course_exam_part_rooms
    rename to subject_exam_rooms;


DO
$$
    BEGIN

        alter table if exists subject_exam_rooms
            rename column course_exam_part_id to subject_exam_id;

    EXCEPTION
        WHEN undefined_column THEN raise notice 'course_exam_part_id not exist';
    END;
$$;

DO
$$
    BEGIN

        alter table if exists subject_exam
            rename column "to" to to_time;

    EXCEPTION
        WHEN undefined_column THEN raise notice 'from not exist';
    END ;
$$;


DO
$$
    BEGIN

        alter table if exists subject_exam
            rename column "from" to from_time;
    EXCEPTION
        WHEN undefined_column THEN raise notice 'to not exist';
    END;
$$;

alter table if exists study_program_details
    add column if not exists coordinator_id varchar(255)
        constraint fk_spd_coordinator_id references professor (id);

