CREATE SEQUENCE IF NOT EXISTS study_program_subject_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE professor_details
(
    id      VARCHAR(255) NOT NULL,
    "order" FLOAT,
    CONSTRAINT pk_professordetails PRIMARY KEY (id)
);

CREATE TABLE study_program_details
(
    id                 VARCHAR(255) NOT NULL,
    "order"            FLOAT,
    duration_years     SMALLINT,
    duration_semesters SMALLINT,
    study_cycle        VARCHAR(255),
    CONSTRAINT pk_studyprogramdetails PRIMARY KEY (id)
);

CREATE TABLE study_program_details_fields
(
    study_program_details_id VARCHAR(255) NOT NULL,
    name                     VARCHAR(255),
    category                 VARCHAR(255),
    value                    OID
);

CREATE TABLE study_program_subject
(
    id               BIGINT NOT NULL,
    subject_id       VARCHAR(255),
    study_program_id VARCHAR(255),
    mandatory        BOOLEAN,
    semester         SMALLINT,
    "order"          FLOAT,
    CONSTRAINT pk_studyprogramsubject PRIMARY KEY (id)
);

CREATE TABLE subject_details
(
    id               VARCHAR(255) NOT NULL,
    "order"          FLOAT,
    default_semester SMALLINT,
    CONSTRAINT pk_subjectdetails PRIMARY KEY (id)
);

ALTER TABLE professor_details
    ADD CONSTRAINT FK_PROFESSORDETAILS_ON_ID FOREIGN KEY (id) REFERENCES professor (id);

ALTER TABLE study_program_details
    ADD CONSTRAINT FK_STUDYPROGRAMDETAILS_ON_ID FOREIGN KEY (id) REFERENCES study_program (code);

ALTER TABLE study_program_subject
    ADD CONSTRAINT FK_STUDYPROGRAMSUBJECT_ON_STUDYPROGRAM FOREIGN KEY (study_program_id) REFERENCES study_program_details (id);

ALTER TABLE study_program_subject
    ADD CONSTRAINT FK_STUDYPROGRAMSUBJECT_ON_SUBJECT FOREIGN KEY (subject_id) REFERENCES subject_details (id);

ALTER TABLE subject_details
    ADD CONSTRAINT FK_SUBJECTDETAILS_ON_ID FOREIGN KEY (id) REFERENCES subject (id);

ALTER TABLE study_program_details_fields
    ADD CONSTRAINT fk_studyprogramdetails_fields_on_study_program_details FOREIGN KEY (study_program_details_id) REFERENCES study_program_details (id);