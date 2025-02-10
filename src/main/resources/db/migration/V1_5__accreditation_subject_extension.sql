ALTER TABLE study_program_subject_professor
    Drop CONSTRAINT FK_STUDYPROGRAMSUBJECTPROFESSOR_ON_PROFESSOR;-- FOREIGN KEY (professor_id) REFERENCES professor (id);

ALTER TABLE study_program_subject_professor
    Drop CONSTRAINT FK_STUDYPROGRAMSUBJECTPROFESSOR_ON_STUDYPROGRAMSUBJECT; --FOREIGN KEY (study_program_subject_id) REFERENCES study_program_subject (id);


ALTER TABLE study_program_subject
    drop CONSTRAINT FK_STUDYPROGRAMSUBJECT_ON_STUDYPROGRAM;-- FOREIGN KEY (study_program_id) REFERENCES study_program_details (id);


ALTER TABLE subject_details
    ADD IF NOT EXISTS quality_control VARCHAR(4000);

ALTER TABLE subject_details_books
    ADD IF NOT EXISTS book_order INTEGER;


ALTER TABLE study_program_subject_professor
    ALTER COLUMN id TYPE varchar(255);

ALTER TABLE study_program_subject_professor
    ALTER COLUMN study_program_subject_id TYPE varchar(255);

ALTER TABLE study_program_subject
    ALTER COLUMN id TYPE varchar(255);


ALTER TABLE study_program_subject
    ALTER COLUMN id TYPE varchar(255);

ALTER TABLE study_program_subject
    ADD CONSTRAINT FK_STUDYPROGRAMSUBJECT_ON_STUDYPROGRAM FOREIGN KEY (study_program_id) REFERENCES study_program_details (id);



ALTER TABLE study_program_subject_professor
    add CONSTRAINT FK_STUDYPROGRAMSUBJECTPROFESSOR_ON_PROFESSOR FOREIGN KEY (professor_id) REFERENCES professor (id);

ALTER TABLE study_program_subject_professor
    add CONSTRAINT FK_STUDYPROGRAMSUBJECTPROFESSOR_ON_STUDYPROGRAMSUBJECT FOREIGN KEY (study_program_subject_id) REFERENCES study_program_subject (id);


alter table subject_details
    alter column cycle type varchar(255);


CREATE TABLE IF NOT EXISTS subject_details_elective_books
(
    subject_details_id VARCHAR(255) NOT NULL,
    author             VARCHAR(255),
    title              VARCHAR(255),
    publisher          VARCHAR(255),
    year               SMALLINT,
    book_order         INTEGER
);


ALTER TABLE subject_details_elective_books
    ADD CONSTRAINT fk_subjectdetails_elective_books_on_subject_details FOREIGN KEY (subject_details_id) REFERENCES subject_details (id);

ALTER TABLE weekly_subject_topic
    ALTER COLUMN lecture_topic type varchar(4000),
    ALTER COLUMN exercise_topic type varchar(4000);

ALTER TABLE study_program_subject
    DROP COLUMN study_program_id;

ALTER TABLE study_program_subject
    ADD COLUMN study_program_code varchar(255),
    ADD CONSTRAINT FK_STUDYPROGRAMSUBJECT_ON_STUDYPROGRAM FOREIGN KEY (study_program_code) REFERENCES study_program (CODE);