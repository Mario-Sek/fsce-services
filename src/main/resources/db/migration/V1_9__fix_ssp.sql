update study_program_subject
set semester=3
where study_program_code = 'SSP23'
  and semester in (5, 7);


update study_program_subject
set semester=4
where study_program_code = 'SSP23'
  and semester in (6, 8);