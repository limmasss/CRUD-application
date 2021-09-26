package crud.services;

import crud.exception.UserAlreadyExistsException;
import crud.models.User;

import java.util.List;

public interface UserService {
    void create(User user) throws UserAlreadyExistsException;

    List<User> getAll();
    User get(Long id);

    void update(Long id, User user);
    void delete(Long id);
}
