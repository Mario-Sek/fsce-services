alter table if exists "user"
    rename to auth_user;

insert into auth_user(id, name, email, role)
        (select id, name, email, 'PROFESSOR' from professor where id is not null);

update auth_user
set role='ACADEMIC_AFFAIR_VICE_DEAN'
where id = 'riste.stojanov';


update auth_user
set role='SCIENCE_AND_COOPERATION_VICE_DEAN'
where id = 'petre.lameski';

update auth_user
set role='FINANCES_VICE_DEAN'
where id = 'magdalena.kostoska';

update auth_user
set role='DEAN'
where id = 'boro.jakimovski';



update auth_user
set role='EXTERNAL'
where id in ('tome.eftimov', 'vlado.spiridonov', 'sasho.panov');

insert into auth_user(name, email, id, role)
values ('Elena Panovska', 'elena.panovska@finki.ukim.mk', 'elena.panovska', 'STUDENT_ADMINISTRATION_MANAGER'),
       ('Martina Jankulovska', 'martina.jankulovska@finki.ukim.mk', 'martina.jankulovska', 'STUDENT_ADMINISTRATION_MANAGER'),
       ('Marija Damjanoska', 'marija.damjanoska@finki.ukim.mk', 'marija.damjanoska', 'STUDENT_ADMINISTRATION'),
       ('Ruzhica Peeva', 'ruzhica.peeva@finki.ukim.mk', 'ruzhica.peeva', 'STUDENT_ADMINISTRATION'),
       ('Dragan Spiroski', 'dragan.spiroski@finki.ukim.mk', 'dragan.spiroski', 'STUDENT_ADMINISTRATION'),
       ('Jasna Bozhinova', 'jasna.bozhinova@finki.ukim.mk', 'jasna.bozhinova', 'STUDENT_ADMINISTRATION'),
       ('Liljana Gjorgjievska', 'liljana.gjorgjievska@finki.ukim.mk', 'liljana.gjorgjievska', 'ARCHIVE_ADMINISTRATION'),
       ('Aneta Deskovska', 'aneta.deskovska@finki.ukim.mk', 'aneta.deskovska', 'FINANCE_ADMINISTRATION'),
       ('Pepica Ambarkova', 'pepica.ambarkova@finki.ukim.mk', 'pepica.ambarkova', 'FINANCE_ADMINISTRATION_MANAGER'),
       ('Aleksandar Ikonomov', 'aleksandar.ikonomov@finki.ukim.mk', 'aleksandar.ikonomov', 'FINANCE_ADMINISTRATION'),
       ('Igor Cvetanovski', 'igor.cvetanovski@finki.ukim.mk', 'igor.cvetanovski', 'ADMINISTRATION_MANAGER'),
       ('Biljana Peshova', 'biljana.peshova@finki.ukim.mk', 'biljana.peshova', 'LEGAL_ADMINISTRATION'),
       ('Valentina Trajanova', 'valentina.trajanova@finki.ukim.mk', 'valentina.trajanova', 'LEGAL_ADMINISTRATION');



