package codesquad.user;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static UserRepository userRepository;
    private List<User> users = new ArrayList<>();

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }
        return userRepository;
    }

    public void addUser(User user) {
        user.inputId(users.size() + 1);
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

}
