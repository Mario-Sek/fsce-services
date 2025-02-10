CREATE SEQUENCE IF NOT EXISTS topic_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE teaching_and_scientific_meeting
(
    meeting_number  VARCHAR(255) NOT NULL,
    date            date,
    present_members INTEGER,
    CONSTRAINT pk_teachingandscientificmeeting PRIMARY KEY (meeting_number)
);

CREATE TABLE topic
(
    id                              BIGINT NOT NULL,
    serial_number                   VARCHAR(255),
    category_name                   VARCHAR(255),
    sub_category_name               VARCHAR(255),
    description                     VARCHAR(6000),
    is_accepted                     BOOLEAN,
    accept_number                   INTEGER,
    against_number                  INTEGER,
    sustained_number                INTEGER,
    discussion                      VARCHAR(255),
    meeting_meeting_number          VARCHAR(255),
    mentioned_student_student_index VARCHAR(255),
    professor_id                    VARCHAR(255),
    CONSTRAINT pk_topic PRIMARY KEY (id)
);

CREATE TABLE topic_mentioned_professors
(
    topic_id                BIGINT       NOT NULL,
    mentioned_professors_id VARCHAR(255) NOT NULL
);

ALTER TABLE subject
    ADD abbreviation VARCHAR(255);

ALTER TABLE study_program_details
    ADD accreditation_year VARCHAR(255);

ALTER TABLE accreditation
    ADD active_from date;

ALTER TABLE accreditation
    ADD active_to date;

ALTER TABLE accreditation
    ADD is_active BOOLEAN;

ALTER TABLE results
    ADD uploaded_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE study_program_details
    ADD CONSTRAINT FK_STUDYPROGRAMDETAILS_ON_ACCREDITATION_YEAR FOREIGN KEY (accreditation_year) REFERENCES accreditation (year);

ALTER TABLE topic
    ADD CONSTRAINT FK_TOPIC_ON_MEETING_MEETINGNUMBER FOREIGN KEY (meeting_meeting_number) REFERENCES teaching_and_scientific_meeting (meeting_number);

ALTER TABLE topic
    ADD CONSTRAINT FK_TOPIC_ON_MENTIONEDSTUDENT_STUDENT_INDEX FOREIGN KEY (mentioned_student_student_index) REFERENCES student (student_index);

ALTER TABLE topic
    ADD CONSTRAINT FK_TOPIC_ON_PROFESSOR FOREIGN KEY (professor_id) REFERENCES professor (id);

ALTER TABLE topic_mentioned_professors
    ADD CONSTRAINT fk_topmenpro_on_professor FOREIGN KEY (mentioned_professors_id) REFERENCES professor (id);

ALTER TABLE topic_mentioned_professors
    ADD CONSTRAINT fk_topmenpro_on_topic FOREIGN KEY (topic_id) REFERENCES topic (id);