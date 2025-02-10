alter table if exists subject
    add column if not exists default_semester     smallint default null,
    add column if not exists accreditation_year   varchar(255),
    add column if not exists activity_points      smallint,
    add column if not exists content              varchar(8000),
    add column if not exists content_en           varchar(8000),
    add column if not exists credits              double precision,
    add column if not exists cycle                varchar(255),
    add column if not exists dependencies         varchar(5000),
    add column if not exists exam_points          smallint,
    add column if not exists exercise_hours       varchar(255),
    add column if not exists goals_description    varchar(8000),
    add column if not exists goals_description_en varchar(8000),
    add column if not exists homework_hours       varchar(255),
    add column if not exists language             varchar(255),
    add column if not exists learning_methods     varchar(8000),
    add column if not exists lecture_hours        varchar(255),
    add column if not exists name_en              varchar(255),
    add column if not exists project_hours        varchar(255),
    add column if not exists project_points       smallint,
    add column if not exists self_learning_hours  varchar(255),
    add column if not exists signature_condition  varchar(255),
    add column if not exists tests_points         smallint,
    add column if not exists total_hours          varchar(255),
    add column if not exists quality_control      varchar(4000),
    add column if not exists placeholder          boolean  default false,
    add column if not exists dependency_type      varchar(255),
    add column if not exists last_update_time     timestamp(6),
    add column if not exists last_update_user     varchar(255);

-- update the subject's new fields from subject_details
update subject
set default_semester     = subject_details.default_semester,
    accreditation_year   = subject_details.accreditation_year,
    activity_points      = subject_details.activity_points,
    content              = subject_details.content,
    content_en           = subject_details.content_en,
    credits              = subject_details.credits,
    cycle                = subject_details.cycle,
    dependencies         = subject_details.dependencies,
    exam_points          = subject_details.exam_points,
    exercise_hours       = subject_details.exercise_hours,
    goals_description    = subject_details.goals_description,
    goals_description_en = subject_details.goals_description_en,
    homework_hours       = subject_details.homework_hours,
    language             = subject_details.language,
    learning_methods     = subject_details.learning_methods,
    lecture_hours        = subject_details.lecture_hours,
    name_en              = subject_details.name_en,
    project_hours        = subject_details.project_hours,
    project_points       = subject_details.project_points,
    self_learning_hours  = subject_details.self_learning_hours,
    signature_condition  = subject_details.signature_condition,
    tests_points         = subject_details.tests_points,
    total_hours          = subject_details.total_hours,
    quality_control      = subject_details.quality_control,
    placeholder          = subject_details.placeholder,
    dependency_type      = subject_details.dependency_type,
    last_update_time     = subject_details.last_update_time,
    last_update_user     = subject_details.last_update_user
from subject_details
where subject_details.id = subject.id;

create table if not exists subject_history_json
(
    id           serial
        primary key,
    subject_id   varchar(255)
        references subject,
    history_json text
);

delete
from subject_history_json
where id > 0;
-- insert all data from subject_details_history_json into subject_history_json
insert into subject_history_json (subject_id, history_json)
select subject_details_id, history_json
from subject_details_history_json;

alter table weekly_subject_topic
    drop constraint if exists fk_weeklysubjecttopic_on_subjectdetails,
    add constraint fk_weeklysubjecttopic_on_subjectdetails foreign key (subject_details_id) references subject (id);