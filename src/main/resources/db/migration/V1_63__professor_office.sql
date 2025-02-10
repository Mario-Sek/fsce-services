alter table if exists professor
    add column if not exists office_name varchar(255),
    add constraint fk_professor_office_id foreign key (office_name) references room (name);