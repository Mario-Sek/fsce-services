alter table internship
    add column if not exists start_date            date,
    add column if not exists end_date              date;

alter table internship_week
add column if not exists number_of_hours INTEGER;

