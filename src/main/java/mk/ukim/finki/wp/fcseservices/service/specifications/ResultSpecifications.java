package mk.ukim.finki.wp.fcseservices.service.specifications;


import mk.ukim.finki.wp.fcseservices.model.results.Results;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ResultSpecifications {

    public static Specification<Results> filterAll(String professorId, String semesterExamSessionName, List<Course> courses, String hasResults, String examType){
        return ((root, query, criteriaBuilder) -> {
            ResultSpecificationBuilder builder = new ResultSpecificationBuilder(root,query, criteriaBuilder);
            builder
                    .withUploadedBy(professorId)
                    .withHasResults(hasResults)
                    .withCourses(courses)
                    .withSemester(semesterExamSessionName)
                    .withExamType(examType);


            return builder.build();
        });
    }

    public static Specification<Results> filterByProfessor(String professorId, List<Course> courses){
        return ((root, query, criteriaBuilder) -> {
            ResultSpecificationBuilder builder = new ResultSpecificationBuilder(root,query, criteriaBuilder);
            return builder
                    .withProfessor(professorId)
                    .withCourses(courses)
                    .withUploadedBy(professorId)
                    .withHasResults("Yes")
                    .build();
        });

    }

    public static Specification<Results> filterByStudent(String studentId,String semesterExamSessionName, String hasResults,String examType,String professorId){
        return ((root, query, criteriaBuilder) -> {
            ResultSpecificationBuilder builder = new ResultSpecificationBuilder(root,query, criteriaBuilder);
            builder
                    .withSemester(semesterExamSessionName)
                    .withHasResults(hasResults)
                    .withStudentEnrolledInCourse(studentId)
                    .withUploadedBy(professorId)
                    .withExamType(examType);


            return builder.build();
        });

    }
}
