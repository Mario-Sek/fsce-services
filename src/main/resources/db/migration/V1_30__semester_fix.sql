INSERT INTO semester(code, semester_type, year)
VALUES ('2022-23-S', 'SUMMER', '2022-23'),
       ('2022-23-W', 'WINTER', '2022-23'),
       ('2023-24-S', 'SUMMER', '2023-24'),
       ('2023-24-W', 'WINTER', '2023-24');

update teacher_subject_allocations
set semester_code='2022-23-S'
where semester_code = '2022/2023-S';

delete
from semester
where code in ('2022/2023-S', '2022/2023-W', '2023/2024-S', '2023/2024-W');