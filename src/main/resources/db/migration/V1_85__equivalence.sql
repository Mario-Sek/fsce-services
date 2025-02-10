alter table if exists student_grade
    drop column if exists equivalented,
    drop column if exists equivalent_grade_codes,
    add column if not exists active boolean default true;