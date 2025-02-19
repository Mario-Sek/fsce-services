package mk.ukim.finki.wp.fcseservices.service.impl;

import mk.ukim.finki.wp.fcseservices.model.teachingallocation.JoinedSubject;
import mk.ukim.finki.wp.fcseservices.repository.JoinedSubjectRepository;
import mk.ukim.finki.wp.fcseservices.service.JoinedSubjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JoinedSubjectServiceImpl implements JoinedSubjectService {

    private final JoinedSubjectRepository joinedSubjectRepository;

    public JoinedSubjectServiceImpl(JoinedSubjectRepository joinedSubjectRepository) {
        this.joinedSubjectRepository = joinedSubjectRepository;
    }

    @Override
    public List<JoinedSubject> findAllJoinedSubjects() {
        return joinedSubjectRepository.findAll();
    }
}
