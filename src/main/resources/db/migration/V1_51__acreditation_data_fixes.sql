update study_program_subject
set mandatory= false,
    "order" = 100,
    subject_group='факултет-з'
where id = 'KN23_1-KN-Z-03';

delete
from study_program_subject
where id like '%del%';