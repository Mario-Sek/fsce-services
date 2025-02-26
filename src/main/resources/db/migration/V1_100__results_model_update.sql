alter table results
    add column if not exists result_type    varchar(255),
    add column if not exists uploaded_by_id varchar(255)
        constraint fk_results_on_uploaded_by_id references professor (id);