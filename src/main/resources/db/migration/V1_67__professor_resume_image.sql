alter table if exists professor_resume
    add column if not exists id varchar(255);

alter table if exists ProfessorResume
    add constraint fk_professor_resume_professor_id
    foreign key (id)
    references Professor(id);

alter table if exists professor_resume
alter column biography type varchar(10000);




