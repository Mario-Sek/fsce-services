alter table results
    add column if not exists joined_subject_abbreviation varchar(255),
    add column if not exists note                        varchar(1000);



DO
$$
    BEGIN
        alter table results
            drop constraint fk_results_on_course_id;

        alter table results
            drop column if exists course_id;

    EXCEPTION
        WHEN undefined_column THEN raise notice 'date not exist';
    END;
$$;