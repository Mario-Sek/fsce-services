CREATE SEQUENCE IF NOT EXISTS master_thesis_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS master_thesis_status_change_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS study_program_subject_professor_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS weekly_subject_topic_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE accreditation
(
    year VARCHAR(255) NOT NULL,
    CONSTRAINT pk_accreditation PRIMARY KEY (year)
);

CREATE TABLE accreditation_study_program_fields
(
    accreditation_year   VARCHAR(255) NOT NULL,
    study_program_fields VARCHAR(255)
);

CREATE TABLE master_thesis
(
    id                      BIGINT NOT NULL,
    status                  VARCHAR(255),
    student_student_index   VARCHAR(255),
    title                   VARCHAR(255),
    area                    VARCHAR(255),
    description             VARCHAR(5000),
    mentor_id               VARCHAR(255),
    first_member_id         VARCHAR(255),
    second_member_id        VARCHAR(255),
    thesis_text             OID,
    grade                   INTEGER,
    location                VARCHAR(255),
    presentation_start_time TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_masterthesis PRIMARY KEY (id)
);

CREATE TABLE master_thesis_status_change
(
    id                   BIGINT NOT NULL,
    diploma_thesis_id    BIGINT,
    status_change_date   date,
    next_status          VARCHAR(255),
    status_changed_by_id VARCHAR(255),
    CONSTRAINT pk_masterthesisstatuschange PRIMARY KEY (id)
);

CREATE TABLE study_program_subject_professor
(
    id                       BIGINT NOT NULL,
    study_program_subject_id BIGINT,
    professor_id             VARCHAR(255),
    "order"                  FLOAT,
    CONSTRAINT pk_studyprogramsubjectprofessor PRIMARY KEY (id)
);

CREATE TABLE subject_details_books
(
    subject_details_id VARCHAR(255) NOT NULL,
    author             VARCHAR(255),
    title              VARCHAR(255),
    publisher          VARCHAR(255),
    year               SMALLINT
);

CREATE TABLE subject_details_notes
(
    subject_details_id VARCHAR(255) NOT NULL,
    notes              VARCHAR(255)
);

CREATE TABLE weekly_subject_topic
(
    id                 BIGINT NOT NULL,
    subject_details_id VARCHAR(255),
    week_number        SMALLINT,
    lecture_topic      VARCHAR(255),
    exercise_topic     VARCHAR(255),
    CONSTRAINT pk_weeklysubjecttopic PRIMARY KEY (id)
);

ALTER TABLE subject_details
    ADD accreditation_year VARCHAR(255);

ALTER TABLE subject_details
    ADD activity_points SMALLINT;

ALTER TABLE subject_details
    ADD content VARCHAR(8000);

ALTER TABLE subject_details
    ADD content_en VARCHAR(8000);

ALTER TABLE subject_details
    ADD copy_of_subject_details_id VARCHAR(255);

ALTER TABLE subject_details
    ADD credits FLOAT;

ALTER TABLE subject_details
    ADD cycle INTEGER;

ALTER TABLE subject_details
    ADD dependencies VARCHAR(5000);

ALTER TABLE subject_details
    ADD exam_points SMALLINT;

ALTER TABLE subject_details
    ADD exercise_hours VARCHAR(255);

ALTER TABLE subject_details
    ADD goals_description VARCHAR(8000);

ALTER TABLE subject_details
    ADD goals_description_en VARCHAR(8000);

ALTER TABLE subject_details
    ADD homework_hours VARCHAR(255);

ALTER TABLE subject_details
    ADD language VARCHAR(255);

ALTER TABLE subject_details
    ADD learning_methods VARCHAR(8000);

ALTER TABLE subject_details
    ADD lecture_hours VARCHAR(255);

ALTER TABLE subject_details
    ADD name_en VARCHAR(255);

ALTER TABLE subject_details
    ADD project_hours VARCHAR(255);

ALTER TABLE subject_details
    ADD project_points SMALLINT;

ALTER TABLE subject_details
    ADD self_learning_hours VARCHAR(255);

ALTER TABLE subject_details
    ADD signature_condition VARCHAR(255);

ALTER TABLE subject_details
    ADD tests_points SMALLINT;

ALTER TABLE subject_details
    ADD total_hours VARCHAR(255);

ALTER TABLE master_thesis_status_change
    ADD CONSTRAINT FK_MASTERTHESISSTATUSCHANGE_ON_DIPLOMATHESIS FOREIGN KEY (diploma_thesis_id) REFERENCES master_thesis (id);

ALTER TABLE master_thesis_status_change
    ADD CONSTRAINT FK_MASTERTHESISSTATUSCHANGE_ON_STATUSCHANGEDBY FOREIGN KEY (status_changed_by_id) REFERENCES "user" (id);

ALTER TABLE master_thesis
    ADD CONSTRAINT FK_MASTERTHESIS_ON_FIRSTMEMBER FOREIGN KEY (first_member_id) REFERENCES professor (id);

ALTER TABLE master_thesis
    ADD CONSTRAINT FK_MASTERTHESIS_ON_MENTOR FOREIGN KEY (mentor_id) REFERENCES professor (id);

ALTER TABLE master_thesis
    ADD CONSTRAINT FK_MASTERTHESIS_ON_SECONDMEMBER FOREIGN KEY (second_member_id) REFERENCES professor (id);

ALTER TABLE master_thesis
    ADD CONSTRAINT FK_MASTERTHESIS_ON_STUDENT_STUDENT_INDEX FOREIGN KEY (student_student_index) REFERENCES student (student_index);

ALTER TABLE study_program_subject_professor
    ADD CONSTRAINT FK_STUDYPROGRAMSUBJECTPROFESSOR_ON_PROFESSOR FOREIGN KEY (professor_id) REFERENCES professor (id);

ALTER TABLE study_program_subject_professor
    ADD CONSTRAINT FK_STUDYPROGRAMSUBJECTPROFESSOR_ON_STUDYPROGRAMSUBJECT FOREIGN KEY (study_program_subject_id) REFERENCES study_program_subject (id);

ALTER TABLE subject_details
    ADD CONSTRAINT FK_SUBJECTDETAILS_ON_ACCREDITATION_YEAR FOREIGN KEY (accreditation_year) REFERENCES accreditation (year);

ALTER TABLE weekly_subject_topic
    ADD CONSTRAINT FK_WEEKLYSUBJECTTOPIC_ON_SUBJECTDETAILS FOREIGN KEY (subject_details_id) REFERENCES subject_details (id);

ALTER TABLE accreditation_study_program_fields
    ADD CONSTRAINT fk_accreditation_studyprogramfields_on_accreditation FOREIGN KEY (accreditation_year) REFERENCES accreditation (year);

ALTER TABLE subject_details_books
    ADD CONSTRAINT fk_subjectdetails_books_on_subject_details FOREIGN KEY (subject_details_id) REFERENCES subject_details (id);

ALTER TABLE subject_details_notes
    ADD CONSTRAINT fk_subjectdetails_notes_on_subject_details FOREIGN KEY (subject_details_id) REFERENCES subject_details (id);

ALTER TABLE subject_details
DROP
COLUMN "order";

ALTER TABLE request_session
DROP
COLUMN approval_note;

ALTER TABLE request_session
DROP
COLUMN description;

ALTER TABLE request_session
    ADD approval_note VARCHAR(5000);

ALTER TABLE course_exam_part
DROP
COLUMN comment;

ALTER TABLE course_exam_part
    ADD comment VARCHAR(5000);

ALTER TABLE course_professor_evaluation
DROP
COLUMN comment;

ALTER TABLE course_professor_evaluation
    ADD comment VARCHAR(5000);

ALTER TABLE request_session
    ADD description VARCHAR(5000);

ALTER TABLE study_program_details_fields
DROP
COLUMN value;

ALTER TABLE study_program_details_fields
    ADD value VARCHAR(5000);