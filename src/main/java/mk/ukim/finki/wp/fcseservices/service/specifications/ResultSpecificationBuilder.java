package mk.ukim.finki.wp.fcseservices.service.specifications;


import jakarta.persistence.criteria.*;
import mk.ukim.finki.wp.fcseservices.model.enrollments.StudentSubjectEnrollment;
import mk.ukim.finki.wp.fcseservices.model.examschedule.YearExamSession;
import mk.ukim.finki.wp.fcseservices.model.results.Results;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.model.teachingallocation.schedule.Course;

import java.util.ArrayList;
import java.util.List;

//todo hasResults

public class ResultSpecificationBuilder {

    private final List<Predicate> resultsPredicates = new ArrayList<>();
    private final List<Predicate> coursePredicates = new ArrayList<>();
    private final Root<Course> courseRoot;
    private final Root<Results> root;
    private final Subquery<JoinedSubject> subjectSubquery;
    private final Join<Results, YearExamSession> sessionJoin;
    private final CriteriaBuilder criteriaBuilder;
    private final CriteriaQuery<?> query;

    public ResultSpecificationBuilder(Root<Results> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        this.sessionJoin = root.join("session", JoinType.LEFT);
        this.subjectSubquery = query.subquery(JoinedSubject.class);
        this.courseRoot = subjectSubquery.from(Course.class);
        this.subjectSubquery.select(courseRoot.get("joinedSubject").get("abbreviation"));
        this.criteriaBuilder = criteriaBuilder;
        this.root = root;
        this.query = query;
    }

    public ResultSpecificationBuilder withCourses(List<Course> courses) {
        if (courses != null && !courses.isEmpty()) {
            coursePredicates.add(courseRoot.get("id").in(courses.stream().map(Course::getId).toArray()));
        }
        return this;
    }

    public ResultSpecificationBuilder withSemester(String semesterExamSessionName) {
        if (semesterExamSessionName != null && !semesterExamSessionName.isEmpty()) {
            resultsPredicates.add(criteriaBuilder.equal(sessionJoin.get("name"), semesterExamSessionName));
        }
        return this;
    }

    public ResultSpecificationBuilder withHasResults(String hasResults) {
            if (hasResults != null && !hasResults.isEmpty() && !hasResults.equals("Any")) {
                if(hasResults.equals("Yes")){
                    resultsPredicates.add(criteriaBuilder.isNotNull(root.get("pdfBytes")));
                } else {
                    resultsPredicates.add(criteriaBuilder.isNull(root.get("pdfBytes")));
                }
            }

        return this;
    }

    public ResultSpecificationBuilder withExamType(String examType) {
        if (examType != null && !examType.isEmpty()){
         resultsPredicates.add(criteriaBuilder.equal(root.get("resultType"), examType));
        }
        return this;
    }

    public ResultSpecificationBuilder withStudentEnrolledInCourse(String studentIndex) {
        if (studentIndex != null && !studentIndex.isEmpty()) {
            Root<StudentSubjectEnrollment> enrollmentRoot = query.from(StudentSubjectEnrollment.class);
            Path<String> enrollmentSubjectAbbreviation = enrollmentRoot.get("joinedSubject").get("abbreviation");
            Path<String> resultSubjectAbbreviation = root.get("joinedSubject").get("abbreviation");
            Predicate studentIndexPredicate = criteriaBuilder.equal(enrollmentRoot.get("student").get("index"), studentIndex);
            Predicate subjectPredicate = criteriaBuilder.equal(enrollmentSubjectAbbreviation,resultSubjectAbbreviation);
            resultsPredicates.add(criteriaBuilder.and(studentIndexPredicate, subjectPredicate));
        }

        return this;

    }

    public ResultSpecificationBuilder withProfessor(String profId) {
        if (profId != null && !profId.isEmpty()) {
            Predicate professorLike = criteriaBuilder.like(courseRoot.get("professors"), "%" + profId + "%");
            Predicate assistantLike = criteriaBuilder.like(courseRoot.get("assistants"), "%" + profId + "%");
            coursePredicates.add(criteriaBuilder.or(professorLike, assistantLike));
        }
        return this;
    }
    public ResultSpecificationBuilder withUploadedBy(String profId){
        if(profId != null && !profId.isEmpty()){
            resultsPredicates.add(criteriaBuilder.equal(root.get("uploadedBy").get("id"), profId));
        }
        return this;
    }

    public Predicate build() {
        coursePredicates.add(criteriaBuilder.equal(
                courseRoot.get("joinedSubject"),
                root.get("joinedSubject")
        ));
        subjectSubquery.where(coursePredicates.toArray(new Predicate[0]));
        resultsPredicates.add(criteriaBuilder.exists(subjectSubquery));
        resultsPredicates.add(root.get("joinedSubject").in(subjectSubquery));

        return criteriaBuilder.and(resultsPredicates.toArray(new Predicate[0]));
    }
}
