package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.base.Subject;
import mk.ukim.finki.wp.fcseservices.model.exceptions.SubjectNotFoundException;

import java.util.List;

public interface SubjectService {
    List<Subject> findAllSubjects();
    Subject findById(String id);

    List<Subject> getAllSubjects();

    Subject getSubjectById(String mainSubjectId) throws SubjectNotFoundException;


}
