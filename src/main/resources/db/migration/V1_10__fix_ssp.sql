
update study_program_subject
set semester=4
where study_program_code = 'SSP23'
  and semester=2 and mandatory=false;

update study_program_subject
set semester=1,
    mandatory= true
where id in ('SSP23-F23L1W020',
             'SSP23-F23L2W201',
             'SSP23-F23L2W109',
             'SSP23-F23L1W003',
             'SSP23-F23L1W007');

update study_program_subject
set semester=2,
    mandatory= true
where id in ('SSP23-F23L1S016', 'SSP23-F23L1S146', 'SSP23-F23L2S066',
             'SSP23-F23L3S025', 'SSP23-F23L1S026');

update study_program_subject
set semester=3,
    mandatory= true
where id in ('SSP23-F23L3W024', 'SSP23-F23L3W103', 'SSP23-F23L3W033', 'SSP23-F23L2W167');

update study_program_subject
set semester=4,
    mandatory= true
where id in ('SSP23-F23L3S100', 'SSP23-F23L2S110');
