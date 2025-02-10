alter table student_courses
drop constraint fk_studentcourses_on_coursegroupid;

alter table results
drop constraint fk_results_on_course_group;

alter table student_courses
drop column course_group_id;

alter table results
drop column course_group_id;


drop table course_group;


alter table student_courses
add column course_id bigint;

alter table student_courses
    add constraint fk_student_courses_on_course_id
        foreign key (course_id) references course(id);


alter table results
add column course_id bigint;

alter table results
    add constraint fk_results_on_course_id
        foreign key (course_id) references course(id);

drop sequence course_group_seq;