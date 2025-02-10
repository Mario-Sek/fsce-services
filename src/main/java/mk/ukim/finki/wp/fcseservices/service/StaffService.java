package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.base.UserProfessorView;
import mk.ukim.finki.wp.fcseservices.model.base.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StaffService {

    Page<UserProfessorView> findAllStaff(Pageable pageable);

    Page<UserProfessorView> findStaffByRole(UserRole role, Pageable pageable);

    UserProfessorView findById(String id);
}
