create table if not exists student_semester_enrollment
(
    id                    varchar(255) not null,
    semester_code         varchar(255) not null,
    student_student_index varchar(255) not null,
    payment_amount        double precision,
    payment_confirmed     boolean default false,
    valid                 boolean default false,
    invalid_note          varchar(4000),
    constraint student_semester_enrollment_id primary key (id),
    constraint student_semester_enrollment_on_student foreign key (student_student_index) references student (student_index),
    constraint student_semester_enrollment_on_semester foreign key (semester_code) references semester (code),
    constraint student_semester_enrollment_unique_student_semester unique (student_student_index, semester_code)
);

create table if not exists student_subject_enrollment
(
    id                    varchar(255) not null,
    semester_code         varchar(255) not null,
    student_student_index varchar(255) not null,
    subject_id            varchar(255) not null,
    valid                 boolean default false,
    invalid_note          varchar(4000),
    num_enrollments       smallint,
    constraint student_subject_enrollment_id primary key (id),
    constraint student_subject_enrollment_on_student foreign key (student_student_index) references student (student_index),
    constraint student_subject_enrollment_on_semester foreign key (semester_code) references semester (code),
    constraint student_subject_enrollment_on_subject foreign key (subject_id) references subject (id),
    constraint student_subject_enrollment_unique_student_semester_subject unique (student_student_index, semester_code, subject_id)

);