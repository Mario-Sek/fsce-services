Drop VIEW if exists events_view;

CREATE VIEW events_view AS
(
select concat('semester:', code) as id,
       'SEMESTER'                as type,
       code                      as name,
       ''                        as description,
       start_date::timestamp     as start_time,
       end_date::timestamp       as end_time,
       null                      as physical_location_name,
       null                      as online_url,
       code                      as reference_id,
       'Semester'                as reference_class
from semester
union
select concat('semester-enrollment:', code) as id,
       'SEMESTER'                           as type,
       code                                 as name,
       ''                                   as description,
       enrollment_start_date::timestamp     as start_time,
       enrollment_end_date::timestamp       as end_time,
       null                                 as physical_location_name,
       null                                 as online_url,
       code                                 as reference_id,
       'Semester'                           as reference_class
from semester);

Drop VIEW if exists teacher_allocation_stats_view;

create view teacher_allocation_stats_view as
(
select concat(tsa.professor_id, tsa.semester_code)                                      as id,
       tsa.professor_id                                                                 as professor_id,
       tsa.semester_code                                                                as semester_code,
       (select cast(SUM(case
                            when tsa1.mentorship_course = true then tsa1.number_of_lecture_groups
                            else 1
           end) as float)
        from teacher_subject_allocations tsa1
        where tsa1.professor_id = tsa.professor_id
          and tsa1.semester_code = tsa.semester_code
          and tsa1.number_of_lecture_groups > 0)                                        as number_of_lecture_subjects,
       (select cast(SUM(case
                            when tsa1.mentorship_course = true then tsa1.number_of_exercise_groups
                            else 1
           end) as float)
        from teacher_subject_allocations tsa1
        where tsa1.professor_id = tsa.professor_id
          and tsa1.semester_code = tsa.semester_code
          and tsa1.number_of_exercise_groups > 0)                                       as number_of_exercise_subjects,
       (select cast(SUM(case
                            when tsa1.number_of_lab_groups < 1 then tsa1.number_of_lab_groups
                            else 1
           end) as float)
        from teacher_subject_allocations tsa1
        where tsa1.professor_id = tsa.professor_id
          and tsa1.semester_code = tsa.semester_code
          and tsa1.number_of_lab_groups > 0)                                            as number_of_lab_subjects,
       (select cast(SUM(case
                            when tsa1.mentorship_course = true then tsa1.number_of_lecture_groups
                            else 1
           end) as float)
        from teacher_subject_allocations tsa1
        where tsa1.professor_id = tsa.professor_id
          and tsa1.semester_code = tsa.semester_code
          and tsa1.number_of_lecture_groups > 0
          and tsa1.english_group = true)                                                as number_of_lecture_en_groups,
       (select cast(SUM(number_of_lab_groups) as float)
        from teacher_subject_allocations tsa1
        where tsa1.professor_id = tsa.professor_id
          and tsa1.semester_code = tsa.semester_code
          and tsa1.english_group = true) as number_of_lab_en_groups,

       (select cast(SUM(case
                            when tsa1.number_of_lecture_groups < 1 then tsa1.number_of_lecture_groups
                            else 1
           end) as float)
        from teacher_subject_allocations tsa1
                 join teacher_subject_allocations tsa2
                      on tsa1.semester_code = tsa2.semester_code and tsa1.subject_id = tsa2.subject_id
                          and tsa1.english_group = true and tsa2.english_group = false
                          and tsa1.professor_id = tsa2.professor_id
        where tsa1.professor_id = tsa.professor_id
          and tsa1.semester_code = tsa.semester_code
          and tsa1.number_of_lecture_groups > 0)                                        as number_of_overlapping_en_subjects,
       (select cast(SUM(case
                            when tsa1.number_of_lecture_groups < 1 then tsa1.number_of_lecture_groups
                            else 1
           end) as float)
        from teacher_subject_allocations tsa1
        where tsa1.professor_id = tsa.professor_id
          and tsa1.semester_code = tsa.semester_code
          and tsa1.number_of_exercise_groups > 0
          and tsa1.english_group = true)                                                as number_of_exercise_en_groups,

       sum(tsa.number_of_exercise_groups)                                               as number_of_exercise_groups,
       sum(tsa.number_of_lab_groups)                                                    as number_of_lab_groups,
       sum(tsa.number_of_lecture_groups)                                                as number_of_lecture_groups,
       cast(sum(tsa.number_of_lecture_groups * sas.average_students_per_group) as int)  as total_lecture_students,
       cast(sum(tsa.number_of_exercise_groups * sas.average_students_per_group) as int) as total_exercise_students,

       cast(sum(
               case
                   when cp.lab_exercises_as_consultations then tsa.number_of_lab_groups * sas.average_students_per_group
                   else tsa.number_of_lab_groups * js.weekly_lab_classes * 20
                   end)
           as int)                       as total_lab_students,

       sum(tsa.number_of_lecture_groups * js.weekly_lectures_classes)                   as total_lecture_classes,
       sum(tsa.number_of_exercise_groups * js.weekly_auditorium_classes)                as total_exercise_classes,
       sum(tsa.number_of_lab_groups)     as total_lab_classes,
       sum(tsa.number_of_lecture_groups * js.weekly_lectures_classes) +
       sum(tsa.number_of_exercise_groups * js.weekly_auditorium_classes) +
       sum(tsa.number_of_lab_groups)     as total_classes
from teacher_subject_allocations tsa
         join joined_subject js on tsa.subject_id = js.abbreviation
         join subject_allocation_stats sas on js.abbreviation = sas.subject_id and sas.semester_code = tsa.semester_code
         left join course_preference cp on cp.subject_id = js.abbreviation
group by tsa.professor_id, tsa.semester_code
order by (total_classes) desc
    );



Drop VIEW if exists joined_subject_group_size;
Drop VIEW if exists course_size;

create view course_size as
(
select concat(sgjs.semester_code, '-', sgjs.abbreviation, '-', sgjs.id, '-', sgjs.english) as id,
       sgjs.semester_code,
       sgjs.abbreviation                                                                   as joined_subject_abbreviation,
       sgjs.id                                                                             as group_id,
       sgjs.group_name,
       sgjs.english,
       sgjs.default_size,
       sum(case when num_enrollments = 1 then 1 else 0 end)                                as first_time_enrollments,
       sum(case when num_enrollments > 1 then 1 else 0 end)                                as re_enrollments
from (select sg.id,
             sg.name as group_name,
             sg.english,
             sg.default_size,
             sg.study_year,
             sg.semester_code,
             js.abbreviation
      from joined_subject js
               join subject_details sd on js.main_subject_id = sd.id
               join student_group sg on sg.study_year - 1 = ((sd.default_semester - 1) / 2)::int) as sgjs
         left join student_subject_enrollment sse
                   on sse.joined_subject_abbreviation = sgjs.abbreviation and
                      sse.group_id = sgjs.id and
                      sse.semester_code = sgjs.semester_code
group by sgjs.semester_code, sgjs.id, sgjs.abbreviation, sgjs.english, sgjs.group_name, sgjs.default_size
order by sgjs.semester_code, sgjs.id, sgjs.abbreviation);


Drop VIEW if exists professor_accreditation_stats_view;

CREATE VIEW professor_accreditation_stats_view AS
(

WITH professors_per_subject AS (SELECT subject_id                        AS subject_id,
                                       COUNT(DISTINCT spsp.professor_id) AS professor_count
                                FROM study_program_subject_professor spsp
                                         JOIN study_program_subject sps ON spsp.study_program_subject_id = sps.id
                                         JOIN subject_details s ON sps.subject_id = s.id
                                GROUP BY sps.subject_id),
     temp AS (SELECT spsp.professor_id,
                     s.cycle,
                     s.accreditation_year,
                     sb.semester,
                     COUNT(DISTINCT sps.subject_id) AS num_subjects,
                     (1.0 / pps.professor_count)    AS num_subject_parts
              FROM study_program_subject_professor spsp
                       JOIN study_program_subject sps ON spsp.study_program_subject_id = sps.id
                       JOIN subject_details s ON sps.subject_id = s.id
                       Join subject sb ON s.id = sb.id
                       JOIN professors_per_subject pps ON sps.subject_id = pps.subject_id
              GROUP BY spsp.professor_id, s.cycle, s.accreditation_year, pps.professor_count, sb.semester)
SELECT professor_id || cycle || semester || accreditation_year         AS id,
       professor_id,
       cycle,
       semester,
       accreditation_year,
       cast(sum(num_subjects) as int)                                  AS num_subjects,
       cast(sum(num_subject_parts * num_subjects) as double precision) AS num_subject_parts
FROM temp
GROUP BY professor_id, cycle, accreditation_year, semester
    );


drop view if exists stats_per_subject_view;

create view stats_per_subject_view
            (id,
             name,
             accreditation_year,
             mandatory_study_programs,
             all_study_programs,
             num_professors,
             professors,
             years_active,
             first_time_students,
             re_enrolled_students)
as
WITH manadatory_study_programs AS (SELECT sps.subject_id                                      AS subject,
                                          string_agg(DISTINCT sps.study_program_code::text, ','::text
                                                     ORDER BY (sps.study_program_code::text)) AS mandatory_study_programs
                                   FROM study_program_subject_professor spsp
                                            JOIN study_program_subject sps
                                                 ON spsp.study_program_subject_id::text = sps.id::text
                                   WHERE sps.mandatory = true
                                   GROUP BY sps.subject_id),
     all_study_programs AS (SELECT sps.subject_id                                      AS subject,
                                   string_agg(DISTINCT sps.study_program_code::text, ','::text
                                              ORDER BY (sps.study_program_code::text)) AS all_study_programs
                            FROM study_program_subject_professor spsp
                                     JOIN study_program_subject sps ON spsp.study_program_subject_id::text = sps.id::text
                            GROUP BY sps.subject_id),
     temp_professors_stats AS (SELECT sps.subject_id                                 AS subject,
                                      count(DISTINCT spsp.professor_id)              AS num_professors,
                                      string_agg(DISTINCT spsp.professor_id::text, ','::text
                                                 ORDER BY (spsp.professor_id::text)) AS professors
                               FROM study_program_subject_professor spsp
                                        JOIN study_program_subject sps ON spsp.study_program_subject_id::text = sps.id::text
                               GROUP BY sps.subject_id),
     temp_allocation_stats AS (SELECT js.main_subject_id                        AS subject,
                                      count(sas.id)                             AS years_active,
                                      avg(sas.number_of_first_time_students)    AS first_time_students,
                                      avg(sas.number_of_re_enrollment_students) AS re_enrolled_students
                               FROM subject_allocation_stats sas
                                        JOIN joined_subject js ON sas.subject_id::text = js.abbreviation::text
                               GROUP BY js.main_subject_id)
SELECT sd.id,
       s.name,
       sd.accreditation_year,
       msp.mandatory_study_programs,
       asp.all_study_programs,
       tps.num_professors,
       tps.professors,
       tas.years_active,
       tas.first_time_students::integer  AS first_time_students,
       tas.re_enrolled_students::integer AS re_enrolled_students
FROM subject_details sd
         JOIN subject s ON sd.id::text = s.id::text
         LEFT JOIN manadatory_study_programs msp ON sd.id::text = msp.subject::text
         LEFT JOIN all_study_programs asp ON sd.id::text = asp.subject::text
         LEFT JOIN temp_professors_stats tps ON sd.id::text = tps.subject::text
         LEFT JOIN temp_allocation_stats tas ON sd.id::text = tas.subject::text;



drop view if exists subject_needs_view;

create view subject_needs_view
            (id,
             semester_code,
             subject_name,
             n_calc_groups,
             n_groups,
             n_assigned_professors,
             n_req_professors,
             n_assigned_assistants,
             n_req_assistants,
             n_calc_lab_groups,
             n_covered_lab_groups)
as
SELECT DISTINCT sas.semester_code || '-' || js.abbreviation             AS id,
                sas.semester_code,
                (js.name)                                               AS subjectName,
                sas.calculated_number_of_groups                         AS n_calc_groups,
                sas.number_of_groups                                    AS n_groups,
                (SELECT COUNT(DISTINCT tsa.professor_id)
                 FROM professor prof
                          join teacher_subject_allocations tsa on tsa.professor_id = prof.id
                 WHERE tsa.subject_id = js.abbreviation
                   and tsa.semester_code = sas.semester_code
                   and prof.title IN ('PROFESSOR', 'ASSOCIATE_PROFESSOR',
                                      'ASSISTANT_PROFESSOR')) ::int     as n_assigned_professors,
                (SELECT COUNT(DISTINCT prof.id)
                 FROM professor prof
                          join teacher_subject_requests tsr on prof.id = tsr.professor_id
                 WHERE tsr.subject_id = js.abbreviation
                   and prof.title IN ('PROFESSOR', 'ASSOCIATE_PROFESSOR',
                                      'ASSISTANT_PROFESSOR'))::int      as n_req_professors,
                (SELECT COUNT(DISTINCT prof.id)
                 FROM professor prof
                          join teacher_subject_allocations tsa on tsa.professor_id = prof.id
                 WHERE tsa.subject_id = js.abbreviation
                   and tsa.semester_code = sas.semester_code
                   and prof.title NOT IN ('PROFESSOR', 'ASSOCIATE_PROFESSOR',
                                          'ASSISTANT_PROFESSOR'))::int  as n_assigned_assistants,
                (SELECT COUNT(DISTINCT prof.id)
                 FROM professor prof
                          join teacher_subject_requests tsr on prof.id = tsr.professor_id
                 WHERE tsr.subject_id = js.abbreviation
                   and prof.title NOT IN ('PROFESSOR', 'ASSOCIATE_PROFESSOR',
                                          'ASSISTANT_PROFESSOR'))::int  as n_req_assistants,
                (SELECT CASE
                            WHEN cp is null or cp.lab_exercises_as_consultations = true
                                THEN 0
                            ELSE (sas.number_of_first_time_students / 40)::int * js.weekly_lab_classes END
                 FROM course_preference cp
                 WHERE cp.subject_id = sas.subject_id) ::int            AS n_calc_lab_groups,
                (SELECT SUM(tsa.number_of_lab_groups)
                 FROM professor prof
                          join teacher_subject_allocations tsa on prof.id = tsa.professor_id
                 WHERE tsa.subject_id = js.abbreviation
                   and prof.title NOT IN ('PROFESSOR', 'ASSOCIATE_PROFESSOR',
                                          'ASSISTANT_PROFESSOR')) ::int as n_covered_lab_groups
FROM joined_subject js
         JOIN subject_allocation_stats sas on js.abbreviation = sas.subject_id;


CREATE OR REPLACE VIEW internship_coordinator_assignments_view AS
SELECT
    p.id AS professor_id,
    CAST(COUNT(i.id) AS INTEGER) AS assignments_count
FROM
    professor p
        LEFT JOIN
    internship i ON p.id = i.professor_id
GROUP BY
    p.id;

drop view if exists user_professor_view;
create or replace view user_professor_view as
select au.*,
       p.office_name,
       p.ordering_rank,
       p.title
from auth_user au
    left join professor p on au.id = p.id
WHERE au.role <> 'STUDENT';

