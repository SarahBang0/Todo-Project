package miniProject.todo_list.user.Repository;

import miniProject.todo_list.user.Entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryUserRepository implements UserRepository {

    Map<Long, User> store = new HashMap<>();

    @Override
    public User createUser(User user) {
        store.put(user.getId(), user);
        return user;
    }

    @Override
    public User deleteUser(Long id) {
        return store.remove(id);
    }

    @Override
    public User findById(Long id) {
        return store.get(id);
    }

    @Override
    public User findByEmail(String email) {
        for(User u : store.values()) {
            if(u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public List<User> findByName(String name) {
        List<User> users = new ArrayList<>();
        for(User u : store.values()) {
            if(u.getUserName().contains(name)) {
                users.add(u);
            }
        }
        return users;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
