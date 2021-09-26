package crud.services;

import crud.exception.UserAlreadyExistsException;
import crud.models.Role;
import crud.models.Status;
import crud.models.User;
import crud.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository repository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void create(User user) throws UserAlreadyExistsException {
        if (repository.findByName(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        if (user.getStatus() == null) {
            user.setStatus(Status.ACTIVE);
        }
        if (user.getRoles().isEmpty()) {
            Role role = Role.ROLE_USER;
            user.getRoles().add(role);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.saveAndFlush(user);
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public User get(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void update(Long id, User user) {
        Optional<User> userFromDb = repository.findById(id);
        if (userFromDb.isPresent()) {
            if (user.getUsername().isEmpty() || user.getUsername() == null) {
                user.setName(userFromDb.get().getName());
            }
            if (user.getPassword().isEmpty() || user.getPassword() == null) {
                user.setPassword(userFromDb.get().getPassword());
            } else if (!user.getPassword().equals(userFromDb.get().getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            if (user.getAge() == 0) {
                user.setAge(userFromDb.get().getAge());
            }
            if (user.getRoles().isEmpty() || user.getRoles() == null) {
                user.setRoles(userFromDb.get().getRoles());
            }
            if (user.getStatus() == null) {
                user.setStatus(userFromDb.get().getStatus());
            }
            repository.saveAndFlush(user);
        }
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByName(username).
                orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
        return user;
    }
}
