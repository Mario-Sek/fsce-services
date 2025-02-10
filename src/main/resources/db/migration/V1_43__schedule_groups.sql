alter table if exists joined_subject_group
    add column if not exists groups varchar(1000);


INSERT INTO room (name, capacity, type)
VALUES ('Амф П', '300', 'CLASSROOM'),
       ('Амф МФ', '200', 'CLASSROOM'),
       ('223', '180', 'CLASSROOM'),
       ('225', '150', 'CLASSROOM'),
       ('Б 2.2', '120', 'CLASSROOM'),
       ('Б 3.2', '120', 'CLASSROOM'),
       ('Амф ТМФ', '120', 'CLASSROOM'),
       ('Б1', '110', 'CLASSROOM'),
       ('117', '100', 'CLASSROOM'),
       ('315', '100', 'CLASSROOM'),
       ('310', '80', 'CLASSROOM'),
       ('Б 2.1', '60', 'CLASSROOM'),
       ('Б 3.1', '60', 'CLASSROOM'),
       ('224', '60', 'CLASSROOM'),
       ('216', '60', 'CLASSROOM'),
       ('112', '60', 'CLASSROOM'),
       ('123', '60', 'CLASSROOM');


