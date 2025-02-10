alter table course_enrollment_without_requirements_student_request
drop constraint fkskcy5qqe0pa2kfpmo578pew8s;

alter table course_group_change_student_request
drop constraint fkbh23g6f06r4tfsh4b3e9e7fed;

alter table course_professor_evaluation
drop constraint fkt05dm8g18bgphhbd6aamt5jb9;

alter table late_course_enrollment_student_request
drop constraint fk3pbt4gv245o4nchx6gwlhi1yu;

alter table disciplinary_record
drop constraint fk_disciplinaryrecord_on_courseid;

alter table course_group
drop constraint fk9rq50frjy5ol4a1wleykjaqv2;

drop table if exists course;

alter table joined_subject_group
    rename to course;


alter table course_enrollment_without_requirements_student_request
    alter column course_id type bigint
    using course_id::bigint;

alter table course_enrollment_without_requirements_student_request
    add constraint fk_course_enrollment_without_requirements_student_request_on_course_id
        foreign key (course_id) references course(id);


alter table course_group_change_student_request
    alter column course_id type bigint
    using course_id::bigint;

alter table course_group_change_student_request
    add constraint fk_course_group_change_student_request_on_course_id
        foreign key (course_id) references course(id);


alter table course_professor_evaluation
    alter column course_id type bigint
    using course_id::bigint;

alter table course_professor_evaluation
    add constraint fk_course_professor_evaluation_on_course_id
        foreign key (course_id) references course(id);


alter table late_course_enrollment_student_request
    alter column course_id type bigint
    using course_id::bigint;

alter table late_course_enrollment_student_request
    add constraint fk_late_course_enrollment_student_request_on_course_id
        foreign key (course_id) references course(id);


alter table disciplinary_record
    alter column course_id type bigint
    using course_id::bigint;

alter table disciplinary_record
    add constraint fk_disciplinary_record_on_course_id
        foreign key (course_id) references course(id);


alter table course_group
    alter column course_id type bigint
    using course_id::bigint;

alter table course_group
    add constraint fk_course_group_on_course_id
        foreign key (course_id) references course(id);


alter table joined_subject_group_rooms rename to course_rooms;

alter table course_rooms
    rename column joined_subject_group_id to course_id;

alter table course_rooms
drop constraint fk_joined_subject_group_room_on_jsg;

alter table course_rooms
    add constraint fk_course_room_on_c
        foreign key (course_id) references course(id);



alter table joined_subject_group_student_groups rename to course_student_groups;

alter table course_student_groups
    rename column joined_subject_group_id to course_id;

alter table course_student_groups
drop constraint fk_joined_subject_group_student_groups_on_jsg;

alter table course_student_groups
    add constraint fk_course_student_groups_on_csg
        foreign key (course_id)
            references course(id);


drop sequence joined_subject_group_seq;

create sequence course_seq
    increment by 50
    start with 1;

alter sequence course_seq owner to finki_admin;