alter table scheduled_class_session
    add column if not exists semester_code varchar(255)
        constraint fk_scheduled_class_session_on_semester_code references semester (code);