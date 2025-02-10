alter table subject_details
    add column placeholder bool default false;

insert into subject(id, name, semester)
values ('F23L_S___', 'Изборен предмет', 'SUMMER'),
       ('F23L_W___', 'Изборен предмет', 'WINTER');

insert into subject_details("id", "name_en", "credits", "default_semester", "cycle",
                            "accreditation_year", "placeholder")
values ('F23L_S___', 'Elective subject', '6.00', 4, 'UNDERGRADUATE', '2023', true),
       ('F23L_W___', 'Elective subject', '6.00', 3, 'UNDERGRADUATE', '2023', true);



insert into subject(id, name, semester)
values ('F23L1S__1', 'Изборен предмет со код што започнува на F23L1S', 'SUMMER'),

       ('F23L2W__1', 'Изборен предмет со код што започнува на F23L2W', 'WINTER'),
       ('F23L2W__2', 'Изборен предмет со код што започнува на F23L2W', 'WINTER'),

       ('F23L2S__1', 'Изборен предмет со код што започнува на F23L2S', 'SUMMER'),
       ('F23L2S__2', 'Изборен предмет со код што започнува на F23L2S', 'SUMMER'),

       ('F23L2_3W__1', 'Изборен предмет со код што започнува на F23L2W или F23L3W', 'WINTER'),
       ('F23L2_3W__2', 'Изборен предмет со код што започнува на F23L2W или F23L3W', 'WINTER'),
       ('F23L2_3W__3', 'Изборен предмет со код што започнува на F23L2W или F23L3W', 'WINTER'),
       ('F23L2_3S__1', 'Изборен предмет со код што започнува на F23L2S или F23L3S', 'SUMMER'),
       ('F23L2_3S__2', 'Изборен предмет со код што започнува на F23L2S или F23L3S', 'SUMMER'),
       ('F23L2_3S__3', 'Изборен предмет со код што започнува на F23L2S или F23L3S', 'SUMMER'),

       ('F23L3W__1', 'Изборен предмет со код што започнува на F23L3W', 'WINTER'),
       ('F23L3W__2', 'Изборен предмет со код што започнува на F23L3W', 'WINTER'),
       ('F23L3W__3', 'Изборен предмет со код што започнува на F23L3W', 'WINTER'),
       ('F23L3W__4', 'Изборен предмет со код што започнува на F23L3W', 'WINTER'),
       ('F23L3S__1', 'Изборен предмет со код што започнува на F23L3S', 'SUMMER'),
       ('F23L3S__2', 'Изборен предмет со код што започнува на F23L3S', 'SUMMER'),
       ('F23L3S__3', 'Изборен предмет со код што започнува на F23L3S', 'SUMMER'),
       ('F23L3S__4', 'Изборен предмет со код што започнува на F23L3S', 'SUMMER');


insert into subject_details("id", "name_en", "credits", "default_semester", "cycle",
                            "accreditation_year", "placeholder")
values ('F23L1S__1', 'Elective subject with code starting with F23L1S', '6.00', 2, 'UNDERGRADUATE', '2023', true),

       ('F23L2W__1', 'Elective subject with code starting with F23L2W', '6.00', 3, 'UNDERGRADUATE', '2023', true),
       ('F23L2W__2', 'Elective subject with code starting with F23L2W', '6.00', 3, 'UNDERGRADUATE', '2023', true),

       ('F23L2S__1', 'Elective subject with code starting with F23L2S', '6.00', 4, 'UNDERGRADUATE', '2023', true),
       ('F23L2S__2', 'Elective subject with code starting with F23L2S', '6.00', 4, 'UNDERGRADUATE', '2023', true),

       ('F23L2_3W__1', 'Elective subject with code starting with F23L2W или F23L3W', '6.00', 5, 'UNDERGRADUATE',
        '2023', true),
       ('F23L2_3W__2', 'Elective subject with code starting with F23L2W или F23L3W', '6.00', 5, 'UNDERGRADUATE',
        '2023', true),
       ('F23L2_3W__3', 'Elective subject with code starting with F23L2W или F23L3W', '6.00', 5, 'UNDERGRADUATE',
        '2023', true),
       ('F23L2_3S__1', 'Elective subject with code starting with F23L2S или F23L3S', '6.00', 6, 'UNDERGRADUATE',
        '2023', true),
       ('F23L2_3S__2', 'Elective subject with code starting with F23L2S или F23L3S', '6.00', 6, 'UNDERGRADUATE',
        '2023', true),
       ('F23L2_3S__3', 'Elective subject with code starting with F23L2S или F23L3S', '6.00', 6, 'UNDERGRADUATE',
        '2023', true),

       ('F23L3W__1', 'Elective subject with code starting with F23L3W', '6.00', 7, 'UNDERGRADUATE', '2023', true),
       ('F23L3W__2', 'Elective subject with code starting with F23L3W', '6.00', 7, 'UNDERGRADUATE', '2023', true),
       ('F23L3W__3', 'Elective subject with code starting with F23L3W', '6.00', 7, 'UNDERGRADUATE', '2023', true),
       ('F23L3W__4', 'Elective subject with code starting with F23L3W', '6.00', 7, 'UNDERGRADUATE', '2023', true),

       ('F23L3S__1', 'Elective subject with code starting with F23L3S', '6.00', 8, 'UNDERGRADUATE', '2023', true),
       ('F23L3S__2', 'Elective subject with code starting with F23L3S', '6.00', 8, 'UNDERGRADUATE', '2023', true),
       ('F23L3S__3', 'Elective subject with code starting with F23L3S', '6.00', 8, 'UNDERGRADUATE', '2023', true),
       ('F23L3S__4', 'Elective subject with code starting with F23L3S', '6.00', 8, 'UNDERGRADUATE', '2023', true);

update study_program_subject
set semester=4
where id = 'IE23-F23L1S120';

UPDATE study_program_subject
set semester=8,
    mandatory= true
where id IN ('SIIS23-F23L3S022', 'SEIS23-F23L3S022');


insert into study_program_subject(id, subject_id, study_program_code, mandatory, semester, "order")
values ('PIT23-F23L1S__1', 'F23L1S__1', 'PIT23', '1', '2', '10'),
       ('SIIS23-F23L1S__1', 'F23L1S__1', 'SIIS23', '1', '2', '10'),
       ('SEIS23-F23L1S__1', 'F23L1S__1', 'SEIS23', '1', '2', '10'),
       ('IMB23-F23L1S__1', 'F23L1S__1', 'IMB23', '1', '2', '10'),
       ('KI23-F23L1S__1', 'F23L1S__1', 'KI23', '1', '2', '10'),
       ('KN23-F23L1S__1', 'F23L1S__1', 'KN23', '1', '2', '10'),

       ('IE23-F23L2W__1', 'F23L2W__1', 'IE23', '1', '3', '10'),
       ('IE23-F23L2W__2', 'F23L2W__2', 'IE23', '1', '3', '10'),

       ('IMB23-F23L2W__1', 'F23L2W__1', 'IMB23', '1', '3', '10'),

       ('KI23-F23L2W__1', 'F23L2W__1', 'KI23', '1', '3', '10'),
       ('KI23-F23L2W__2', 'F23L2W__2', 'KI23', '1', '3', '10'),

       ('KN23-F23L2W__1', 'F23L2W__1', 'KN23', '1', '3', '10'),
       ('KN23-F23L2W__2', 'F23L2W__2', 'KN23', '1', '3', '10'),

       ('PIT23-F23L2W__1', 'F23L2W__1', 'PIT23', '1', '3', '10'),
       ('PIT23-F23L2W__2', 'F23L2W__2', 'PIT23', '1', '3', '10'),

       ('SIIS23-F23L2W__1', 'F23L2W__1', 'SIIS23', '1', '3', '10'),
       ('SIIS23-F23L2W__2', 'F23L2W__2', 'SIIS23', '1', '3', '10'),

       ('SEIS23-F23L2W__1', 'F23L2W__1', 'SEIS23', '1', '3', '10'),
       ('SEIS23-F23L2W__2', 'F23L2W__2', 'SEIS23', '1', '3', '10'),


       ('IE23-F23L2S__1', 'F23L2S__1', 'IE23', '1', 4, '10'),

       ('IMB23-F23L2S__1', 'F23L2S__1', 'IMB23', '1', 4, '10'),
       ('IMB23-F23L2S__2', 'F23L2S__2', 'IMB23', '1', 4, '10'),

       ('KI23-F23L2S__1', 'F23L2S__1', 'KI23', '1', 4, '10'),
       ('KI23-F23L2S__2', 'F23L2S__2', 'KI23', '1', 4, '10'),

       ('KN23-F23L2S__1', 'F23L2S__1', 'KN23', '1', 4, '10'),
       ('KN23-F23L2S__2', 'F23L2S__2', 'KN23', '1', 4, '10'),

       ('PIT23-F23L2S__1', 'F23L2S__1', 'PIT23', '1', 4, '10'),

       ('SIIS23-F23L2S__1', 'F23L2S__1', 'SIIS23', '1', 4, '10'),

       ('SEIS23-F23L2S__1', 'F23L2S__1', 'SEIS23', '1', 4, '10'),


       ('IE23-F23L2_3W__1', 'F23L2_3W__1', 'IE23', '1', 5, '10'),

       ('IMB23-F23L2_3W__1', 'F23L2_3W__1', 'IMB23', '1', 5, '10'),
       ('IMB23-F23L2_3W__2', 'F23L2_3W__2', 'IMB23', '1', 5, '10'),

       ('KI23-F23L2_3W__1', 'F23L2_3W__1', 'KI23', '1', 5, '10'),

       ('PIT23-F23L2_3W__1', 'F23L2_3W__1', 'PIT23', '1', 5, '10'),

       ('SIIS23-F23L2_3W__1', 'F23L2_3W__1', 'SIIS23', '1', 5, '10'),

       ('SEIS23-F23L2_3W__1', 'F23L2_3W__1', 'SEIS23', '1', 5, '10'),


       ('IE23-F23L2_3S__1', 'F23L2_3S__1', 'IE23', '1', 6, '10'),
       ('IE23-F23L2_3S__2', 'F23L2_3S__2', 'IE23', '1', 6, '10'),

       ('IMB23-F23L2_3S__1', 'F23L2_3S__1', 'IMB23', '1', 6, '10'),
       ('IMB23-F23L3S__1', 'F23L3S__1', 'IMB23', '1', 6, '10'),

       ('KI23-F23L2_3S__1', 'F23L2_3S__1', 'KI23', '1', 6, '10'),
       ('KI23-F23L3S__1', 'F23L3S__1', 'KI23', '1', 6, '10'),

       ('KN23-F23L2_3S__1', 'F23L2_3S__1', 'KN23', '1', 6, '10'),

       ('PIT23-F23L2_3S__1', 'F23L2_3S__1', 'PIT23', '1', 6, '10'),
       ('PIT23-F23L2_3S__2', 'F23L2_3S__2', 'PIT23', '1', 6, '10'),
       ('PIT23-F23L3S__1', 'F23L3S__1', 'PIT23', '1', 6, '10'),

       ('SIIS23-F23L2_3S__1', 'F23L2_3S__1', 'SIIS23', '1', 6, '10'),

       ('SEIS23-F23L2_3S__1', 'F23L2_3S__1', 'SEIS23', '1', 6, '10'),

       --

       ('IE23-F23L3W__1', 'F23L3W__1', 'IE23', '1', 7, '10'),
       ('IE23-F23L3W__2', 'F23L3W__2', 'IE23', '1', 7, '10'),
       ('IE23-F23L3W__3', 'F23L3W__3', 'IE23', '1', 7, '10'),
       ('IE23-F23L3W__4', 'F23L3W__4', 'IE23', '1', 7, '10'),

       ('IMB23-F23L3W__1', 'F23L3W__1', 'IMB23', '1', 7, '10'),
       ('IMB23-F23L3W__2', 'F23L3W__2', 'IMB23', '1', 7, '10'),

       ('KI23-F23L3W__1', 'F23L3W__1', 'KI23', '1', 7, '10'),
       ('KI23-F23L3W__2', 'F23L3W__2', 'KI23', '1', 7, '10'),

       ('KN23-F23L2_3W__1', 'F23L2_3W__1', 'KN23', '1', 7, '10'),
       ('KN23-F23L3W__1', 'F23L3W__1', 'KN23', '1', 7, '10'),

       ('PIT23-F23L3W__1', 'F23L3W__1', 'PIT23', '1', 7, '10'),
       ('PIT23-F23L3W__2', 'F23L3W__2', 'PIT23', '1', 7, '10'),

       ('SIIS23-F23L3W__1', 'F23L3W__1', 'SIIS23', '1', 7, '10'),
       ('SIIS23-F23L3W__2', 'F23L3W__2', 'SIIS23', '1', 7, '10'),
       ('SIIS23-F23L3W__3', 'F23L3W__3', 'SIIS23', '1', 7, '10'),
       ('SIIS23-F23L3W__4', 'F23L3W__4', 'SIIS23', '1', 7, '10'),

       ('SEIS23-F23L3W__1', 'F23L3W__1', 'SEIS23', '1', 7, '10'),
       ('SEIS23-F23L3W__2', 'F23L3W__2', 'SEIS23', '1', 7, '10'),
       ('SEIS23-F23L3W__3', 'F23L3W__3', 'SEIS23', '1', 7, '10'),
       ('SEIS23-F23L3W__4', 'F23L3W__4', 'SEIS23', '1', 7, '10'),

--

       ('IE23-F23L3S__1', 'F23L3S__1', 'IE23', '1', 8, '10'),
       ('IE23-F23L3S__2', 'F23L3S__2', 'IE23', '1', 8, '10'),

       ('IMB23-F23L3S__2', 'F23L3S__2', 'IMB23', '1', 8, '10'),
       ('IMB23-F23L3S__3', 'F23L3S__3', 'IMB23', '1', 8, '10'),

       ('KI23-F23L3S__2', 'F23L3S__2', 'KI23', '1', 8, '10'),
       ('KI23-F23L3S__3', 'F23L3S__3', 'KI23', '1', 8, '10'),

       ('KN23-F23L3S__1', 'F23L3S__1', 'KN23', '1', 8, '10'),
       ('KN23-F23L3S__2', 'F23L3S__2', 'KN23', '1', 8, '10'),
       ('KN23-F23L3S__3', 'F23L3S__3', 'KN23', '1', 8, '10'),
       ('KN23-F23L3S__4', 'F23L3S__4', 'KN23', '1', 8, '10'),

       ('PIT23-F23L3S__2', 'F23L3S__2', 'PIT23', '1', 8, '10'),
       ('PIT23-F23L3S__3', 'F23L3S__3', 'PIT23', '1', 8, '10'),

       ('SIIS23-F23L2_3S__3', 'F23L2_3S__3', 'SIIS23', '1', 8, '10'),
       ('SIIS23-F23L3S__1', 'F23L3S__1', 'SIIS23', '1', 8, '10'),

       ('SEIS23-F23L2_3S__3', 'F23L2_3S__3', 'SEIS23', '1', 8, '10'),
       ('SEIS23-F23L3S__1', 'F23L3S__1', 'SEIS23', '1', 8, '10');



insert into study_program_subject(id, subject_id, study_program_code, mandatory, semester, "order")
values ('PIT23_3-F23L1S__1', 'F23L1S__1', 'PIT23_3', '1', '2', '10'),
       ('SIIS23_3-F23L1S__1', 'F23L1S__1', 'SIIS23_3', '1', '2', '10'),
       ('SEIS23_3-F23L1S__1', 'F23L1S__1', 'SEIS23_3', '1', '2', '10'),
       ('IMB23_3-F23L1S__1', 'F23L1S__1', 'IMB23_3', '1', '2', '10'),
       ('KI23_3-F23L1S__1', 'F23L1S__1', 'KI23_3', '1', '2', '10'),
       ('KN23_3-F23L1S__1', 'F23L1S__1', 'KN23_3', '1', '2', '10'),


       ('IMB23_3-F23L2W__1', 'F23L2W__1', 'IMB23_3', '1', '3', '10'),

       ('KI23_3-F23L2W__1', 'F23L2W__1', 'KI23_3', '1', '3', '10'),
       ('KI23_3-F23L2W__2', 'F23L2W__2', 'KI23_3', '1', '3', '10'),

       ('KN23_3-F23L2W__1', 'F23L2W__1', 'KN23_3', '1', '3', '10'),
       ('KN23_3-F23L2W__2', 'F23L2W__2', 'KN23_3', '1', '3', '10'),

       ('PIT23_3-F23L2W__1', 'F23L2W__1', 'PIT23_3', '1', '3', '10'),
       ('PIT23_3-F23L2W__2', 'F23L2W__2', 'PIT23_3', '1', '3', '10'),

       ('SIIS23_3-F23L2W__1', 'F23L2W__1', 'SIIS23_3', '1', '3', '10'),
       ('SIIS23_3-F23L2W__2', 'F23L2W__2', 'SIIS23_3', '1', '3', '10'),

       ('SEIS23_3-F23L2W__1', 'F23L2W__1', 'SEIS23_3', '1', '3', '10'),
       ('SEIS23_3-F23L2W__2', 'F23L2W__2', 'SEIS23_3', '1', '3', '10'),


       ('IMB23_3-F23L2S__1', 'F23L2S__1', 'IMB23_3', '1', 4, '10'),
       ('IMB23_3-F23L2S__2', 'F23L2S__2', 'IMB23_3', '1', 4, '10'),

       ('KI23_3-F23L2S__1', 'F23L2S__1', 'KI23_3', '1', 4, '10'),
       ('KI23_3-F23L2S__2', 'F23L2S__2', 'KI23_3', '1', 4, '10'),

       ('KN23_3-F23L2S__1', 'F23L2S__1', 'KN23_3', '1', 4, '10'),
       ('KN23_3-F23L2S__2', 'F23L2S__2', 'KN23_3', '1', 4, '10'),

       ('PIT23_3-F23L2S__1', 'F23L2S__1', 'PIT23_3', '1', 4, '10'),

       ('SIIS23_3-F23L2S__1', 'F23L2S__1', 'SIIS23_3', '1', 4, '10'),

       ('SEIS23_3-F23L2S__1', 'F23L2S__1', 'SEIS23_3', '1', 4, '10'),


       ('IMB23_3-F23L3W__1', 'F23L3W__1', 'IMB23_3', '1', 5, '10'),
       ('IMB23_3-F23L3W__2', 'F23L3W__2', 'IMB23_3', '1', 5, '10'),

       ('KI23_3-F23L3W__1', 'F23L3W__1', 'KI23_3', '1', 5, '10'),

       ('KN23_3-F23L3W__1', 'F23L3W__1', 'KN23_3', '1', 5, '10'),

       ('PIT23_3-F23L3W__1', 'F23L3W__1', 'PIT23_3', '1', 5, '10'),
       ('PIT23_3-F23L3W__2', 'F23L3W__2', 'PIT23_3', '1', 5, '10'),

       ('SIIS23_3-F23L3W__1', 'F23L3W__1', 'SIIS23_3', '1', 5, '10'),
       ('SIIS23_3-F23L3W__2', 'F23L3W__2', 'SIIS23_3', '1', 5, '10'),

       ('SEIS23_3-F23L3W__1', 'F23L3W__1', 'SEIS23_3', '1', 5, '10'),
       ('SEIS23_3-F23L3W__2', 'F23L3W__2', 'SEIS23_3', '1', 5, '10'),


       ('IMB23_3-F23L3S__1', 'F23L3S__1', 'IMB23_3', '1', 6, '10'),
       ('IMB23_3-F23L3S__2', 'F23L3S__2', 'IMB23_3', '1', 6, '10'),
       ('IMB23_3-F23L3S__3', 'F23L3S__3', 'IMB23_3', '1', 6, '10'),

       ('KI23_3-F23L3S__1', 'F23L3S__1', 'KI23_3', '1', 6, '10'),
       ('KI23_3-F23L3S__2', 'F23L3S__2', 'KI23_3', '1', 6, '10'),
       ('KI23_3-F23L3S__3', 'F23L3S__3', 'KI23_3', '1', 6, '10'),

       ('KN23_3-F23L3S__1', 'F23L3S__1', 'KN23_3', '1', 6, '10'),
       ('KN23_3-F23L3S__2', 'F23L3S__2', 'KN23_3', '1', 6, '10'),
       ('KN23_3-F23L3S__3', 'F23L3S__3', 'KN23_3', '1', 6, '10'),

       ('PIT23_3-F23L3S__1', 'F23L3S__1', 'PIT23_3', '1', 6, '10'),
       ('PIT23_3-F23L3S__2', 'F23L3S__2', 'PIT23_3', '1', 6, '10'),
       ('PIT23_3-F23L3S__3', 'F23L3S__3', 'PIT23_3', '1', 6, '10'),

       ('SIIS23_3-F23L3S__1', 'F23L3S__1', 'SIIS23_3', '1', 6, '10'),
       ('SIIS23_3-F23L3S__2', 'F23L3S__2', 'SIIS23_3', '1', 6, '10'),
       ('SIIS23_3-F23L3S__3', 'F23L3S__3', 'SIIS23_3', '1', 6, '10'),

       ('SEIS23_3-F23L3S__1', 'F23L3S__1', 'SEIS23_3', '1', 6, '10'),
       ('SEIS23_3-F23L3S__2', 'F23L3S__2', 'SEIS23_3', '1', 6, '10'),
       ('SEIS23_3-F23L3S__3', 'F23L3S__3', 'SEIS23_3', '1', 6, '10');


insert into study_program_subject(id, subject_id, study_program_code, mandatory, semester, "order")
values ('SSP23-F23L_W__1', 'F23L_W___', 'SSP23', '1', 3, '10'),
       ('SSP23-F23L_S__1', 'F23L_S___', 'SSP23', '1', 4, '10'),
       ('SSP23-F23L_S__2', 'F23L_S___', 'SSP23', '1', 4, '10'),
       ('SSP23-F23L_S__3', 'F23L_S___', 'SSP23', '1', 4, '10');


insert into study_program_subject(id, subject_id, study_program_code, mandatory, semester, "order")
values ('SSP23_3-F23L_W__1', 'F23L_W___', 'SSP23_3', '1', 3, '10'),
       ('SSP23_3-F23L_S__1', 'F23L_S___', 'SSP23_3', '1', 4, '10'),
       ('SSP23_3-F23L_S__2', 'F23L_S___', 'SSP23_3', '1', 4, '10'),
       ('SSP23_3-F23L_S__3', 'F23L_S___', 'SSP23_3', '1', 4, '10'),

       ('SSP23_3-F23L_W__4', 'F23L_W___', 'SSP23_3', '1', 5, '10'),
       ('SSP23_3-F23L_W__5', 'F23L_W___', 'SSP23_3', '1', 5, '10'),
       ('SSP23_3-F23L_W__6', 'F23L_W___', 'SSP23_3', '1', 5, '10'),

       ('SSP23_3-F23L_S__7', 'F23L_S___', 'SSP23_3', '1', 6, '10'),
       ('SSP23_3-F23L_S__8', 'F23L_S___', 'SSP23_3', '1', 6, '10');


update study_program_subject
set semester=1,
    mandatory= true
where id in ('SSP23_3-F23L1W020',
             'SSP23_3-F23L2W201',
             'SSP23_3-F23L2W109',
             'SSP23_3-F23L1W003',
             'SSP23_3-F23L1W007');

update study_program_subject
set "order"=0
where study_program_code='SSP23' AND mandatory=true and subject_id <> 'F23L_S___' and subject_id <> 'F23L_W___';

update study_program_subject
set "order"=0
where study_program_code='SSP23_3' AND mandatory=true and subject_id <> 'F23L_S___' and subject_id <> 'F23L_W___';
