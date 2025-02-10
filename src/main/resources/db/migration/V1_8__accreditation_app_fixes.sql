alter table subject_details_elective_books
    alter column book_order type smallint;

alter table subject_details_books
    alter column book_order type smallint;

update subject
set semester='WINTER'
where semester in ('1', '3', '5', '7', '9');
update subject
set semester='SUMMER'
where semester in ('2', '4', '6', '8', '10');

update subject_details
set credits= credits / 100
where credits > 100;