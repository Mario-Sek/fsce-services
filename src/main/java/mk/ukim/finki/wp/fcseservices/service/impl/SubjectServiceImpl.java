package mk.ukim.finki.wp.fcseservices.service.impl;

import mk.ukim.finki.wp.fcseservices.model.base.Subject;
import mk.ukim.finki.wp.fcseservices.model.exceptions.SubjectNotFoundException;
import mk.ukim.finki.wp.fcseservices.repository.SubjectRepository;
import mk.ukim.finki.wp.fcseservices.service.SubjectService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<Subject> findAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject findById(String id) {
        return subjectRepository.findById(id).get();
    }

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public Subject getSubjectById(String mainSubjectId) {
        return subjectRepository.findById(mainSubjectId)
                .orElseThrow(() -> new SubjectNotFoundException("Main subject not found with id: " + mainSubjectId));
    }
}
