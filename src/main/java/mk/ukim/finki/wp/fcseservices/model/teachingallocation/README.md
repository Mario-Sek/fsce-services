http://194.149.135.92/nastava/raspredelba-summer-2022-23.html

This module will be used for teacher and student allocation to courses. The main goal is to decide which professors will
be assigned to which courses and which students will be assigned to which groups. The courses are still not created
here.
In this process we decide:

1. which subjects will be activated to courses,
2. how many groups will be created for each course,
3. which students will be assigned to the groups and
4. which professors will be assigned to each course group.

## Professor preferences

Prior to the allocation process, professors will be able to set their preferences for the courses they want to
teach `TeacherSubjectRequests` and will describe their preferences on how eah course should be
taught `CoursePreference`.

## Subject allocation stats initialization

The process starts with a list of student enrollment to subjects. From this information, we calculate
the `SubjectAllocationStats` with the number of students enrolled to each subject for the first time and the number of
students re-enrolled to the subject. Also, the number of groups for each subject is retrieved from the previous year.

## Subject activation and number of groups calculation

The next step is to decide which subjects will be activated to courses. The algorithm will be based on the following:

- Subjects with less than 6 students are not activated to courses.
- Subjects with less than 12 students are activated to courses by mentoring.
- All other subjects are activated and the number of groups is calculated based on the number of students enrolled to
  the subject for the first time.

Next, the vice dean for academic affairs will decide the number of groups for each course. Few filters are crucial here:

- `SubjectAllocationStats` that have different calculated groups than the number of groups in the previous year.

## Student allocation to groups

The next step is to allocate students to groups. The algorithm will be based on the following:

- The groups are created based on regex filters for student last name and by study program and study year.
- TODO: model this

## Professor allocation to courses

The next step is to allocate professors to courses. Here, we consider only the courses that differ in the number of
professors and number of groups. This process is done separately for the classes and auditorium exercises.

In this process we must assure fair allocation of professors to courses, as well that no professor has more than 3
distinct subjects (the macedonian and english version of the subjects are considered as one subject).

## Professor allocation to groups

Next, the professors are randomly assigned to the groups. For each group, we should select the rooms with capacity
larger than the number of first time students.

