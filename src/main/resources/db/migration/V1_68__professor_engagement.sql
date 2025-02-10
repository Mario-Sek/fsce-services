alter table if exists professor_engagement
    add column if not exists validation_message varchar(4000);

alter table if exists professor_engagement
    add column if not exists calculated_number_of_classes float;

alter table if exists professor_engagement
    add column if not exists calculated_number_of_students smallint;