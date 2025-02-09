package mk.ukim.finki.wp.fcseservices.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.fcseservices.model.UserProfessorView;
import mk.ukim.finki.wp.fcseservices.model.UserRole;
import mk.ukim.finki.wp.fcseservices.model.exceptions.UserNotFoundException;
import mk.ukim.finki.wp.fcseservices.repository.UserProfessorViewRepository;
import mk.ukim.finki.wp.fcseservices.repository.UserRepository;
import mk.ukim.finki.wp.fcseservices.service.StaffService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class StaffServiceImpl implements StaffService {

    private final UserRepository userRepository;
    private final UserProfessorViewRepository userProfessorViewRepository;


    @Override
    public Page<UserProfessorView> findAllStaff(Pageable pageable) {
        return userProfessorViewRepository.findAll((root, query, criteriaBuilder) -> {
            query.orderBy(
                    criteriaBuilder.asc(criteriaBuilder.selectCase()
                            .when(criteriaBuilder.isNull(root.get("orderingRank")), 1)
                            .otherwise(0)),
                    criteriaBuilder.asc(root.get("orderingRank"))
            );
            return null;
        }, pageable);
    }

    @Override
    public Page<UserProfessorView> findStaffByRole(UserRole role, Pageable pageable) {
        return userProfessorViewRepository.findAll((root, query, criteriaBuilder) ->{
            query.orderBy(
                    criteriaBuilder.asc(criteriaBuilder.selectCase()
                            .when(criteriaBuilder.isNull(root.get("orderingRank")), 1)
                            .otherwise(0)),
                    criteriaBuilder.asc(root.get("orderingRank"))
            );
               return criteriaBuilder.equal(root.get("role"), role);
        }, pageable);
    }

    @Override
    public UserProfessorView findById(String id) {
        return userProfessorViewRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
    }
}
