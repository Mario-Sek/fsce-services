alter table joined_subject
    add column if not exists mandatory_for_study_programs varchar(1000);


alter table subject_allocation_stats
    add column if not exists mandatory_for_study_programs varchar(1000),
    add column if not exists expected_mandatory_students  integer default 0;



update joined_subject js
set mandatory_for_study_programs = (select string_agg(distinct sps.study_program_code, ';')
                                    from study_program_subject sps
                                    where sps.subject_id = js.main_subject_id
                                      and sps.mandatory = true)
where js.name is not null;

update subject_allocation_stats sas
set mandatory_for_study_programs = (select js.mandatory_for_study_programs
                                    from joined_subject js
                                    where js.abbreviation = sas.subject_id)
where sas.subject_id is not null;