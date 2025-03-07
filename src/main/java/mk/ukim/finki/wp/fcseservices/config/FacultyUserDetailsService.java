package mk.ukim.finki.wp.fcseservices.config;

import mk.ukim.finki.wp.fcseservices.model.base.Student;
import mk.ukim.finki.wp.fcseservices.model.base.User;
import mk.ukim.finki.wp.fcseservices.model.base.UserRole;
import mk.ukim.finki.wp.fcseservices.model.exceptions.InvalidUsernameException;
import mk.ukim.finki.wp.fcseservices.model.base.Professor;
import mk.ukim.finki.wp.fcseservices.repository.StudentRepository;
import mk.ukim.finki.wp.fcseservices.repository.UserRepository;
import mk.ukim.finki.wp.fcseservices.service.ProfessorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FacultyUserDetailsService implements UserDetailsService {

    @Value("${system.authentication.password}")
    String systemAuthenticationPassword;

    final UserRepository userRepository;

    final ProfessorService professorService;

    final StudentRepository studentRepository;

    final PasswordEncoder passwordEncoder;

    public FacultyUserDetailsService(UserRepository userRepository, ProfessorService professorService,
                                     StudentRepository studentRepository,
                                     PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.professorService = professorService;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username).orElse(null);
        if (user == null) {
            Student student = studentRepository.findById(username).orElseThrow(InvalidUsernameException::new);
            User user1 = new User();
            user1.setId(username);
            user1.setRole(UserRole.STUDENT);
            return new FacultyUserDetails(user1, student, passwordEncoder.encode(systemAuthenticationPassword));
        } else if (user.getRole().isProfessor()) {
            Professor professor = professorService.findById(username);
            return new FacultyUserDetails(user, professor, passwordEncoder.encode(systemAuthenticationPassword));
        } else {
            return new FacultyUserDetails(user, passwordEncoder.encode(systemAuthenticationPassword));
        }
    }
}
