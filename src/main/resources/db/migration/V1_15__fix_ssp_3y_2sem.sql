
update study_program_subject
set semester=2,
    mandatory= true
where id in ('SSP23_3-F23L1S016', 'SSP23_3-F23L1S146', 'SSP23_3-F23L2S066',
             'SSP23_3-F23L3S025', 'SSP23_3-F23L1S026');

update study_program_subject
set semester=6,
    mandatory= true
where id in ('SSP23_3-F23L3S168');

update study_program_subject
set semester=4
where study_program_code='SSP23_3' and semester=2 and mandatory=false;

update study_program_subject set "order"=0
where mandatory=true and subject_id not like '%\_%';