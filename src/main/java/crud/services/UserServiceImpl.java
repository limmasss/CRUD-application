package crud.services;

import crud.models.User;
import crud.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void create(User user) {
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
        if (repository.findById(id).isPresent()) {
            repository.saveAndFlush(user);
        }
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
