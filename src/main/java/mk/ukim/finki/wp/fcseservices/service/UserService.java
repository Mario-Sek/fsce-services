package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.User;
import mk.ukim.finki.wp.fcseservices.model.UserDto;
import mk.ukim.finki.wp.fcseservices.model.UserRole;
import org.springframework.data.domain.Page;

import java.util.List;


public interface UserService {
    User getUserById(String id);

    Page<User> findAllWithPagination(int pageNum, int pageSize);

    User save(String id, String name, String email, UserRole role);

    void deleteById(String id);

    List<User> findAll();

    List<UserDto> importStudents(List<UserDto> students);
    void processGroup (StringBuilder sb, User user);

    Page<User> findAllWithPaginationAndFilters(Integer pageNum, Integer results, String id, String email, String name, String role);

}
