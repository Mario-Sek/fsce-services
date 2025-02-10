alter table study_program_subject
    add column dependencies_override varchar(5000),
    add column subject_group         varchar(255);

update study_program_subject
set subject_group = "left"(subject_id, 6)
where subject_id is not null;

update study_program_subject
set subject_group='F23L2W;F23L3W'
where subject_id like '%2\_3W%';

update study_program_subject
set subject_group='F23L2S;F23L3S'
where subject_id like '%2\_3S%';

update study_program_subject
set subject_group='F23L_S'
where mandatory = false
  and study_program_code in ('SSP23', 'SSP23_3')
  and semester in (2, 4, 6, 8);

update study_program_subject
set subject_group='F23L_W'
where mandatory = false
  and study_program_code in ('SSP23', 'SSP23_3')
  and semester in (1, 3, 5, 9);

update study_program_subject
set dependencies_override=''
where mandatory = true
  and study_program_code in ('SSP23', 'SSP23_3')

