package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Test
    void testSaveNewUser() {
        User user = new User("jack", "12345");  // since @NonNull exists
        userService.saveNewUser(user);

        User saved = userRepository.findByUserName("jack");
        assertNotNull(saved);
        assertEquals("jack", saved.getUserName());
        assertNotEquals("12345", saved.getPassword()); // password encoded
        assertTrue(saved.getRoles().contains("USER"));
    }

    @Test
    void testSaveAdmin() {
        User admin = new User("admin", "admin123");
        userService.saveAdmin(admin);

        User saved = userRepository.findByUserName("admin");
        assertNotNull(saved);
        assertTrue(saved.getRoles().contains("ADMIN"));
        assertTrue(saved.getRoles().contains("USER"));
    }

    @Test
    void testFindById() {
        User user = new User("findme", "pwd");
        userService.saveNewUser(user);

        User saved = userRepository.findByUserName("findme");
        Optional<User> found = userService.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("findme", found.get().getUserName());
    }

    @Test
    void testDeleteById() {
        User user = new User("deleteme", "pwd");
        userService.saveNewUser(user);

        ObjectId id = userRepository.findByUserName("deleteme").getId();
        userService.deleteById(id);

        assertFalse(userRepository.findById(id).isPresent());
    }

    @Test
    void testFindByUserName() {
        User user = new User("lookup", "pass");
        userService.saveNewUser(user);

        User found = userService.findByUserName("lookup");
        assertNotNull(found);
        assertEquals("lookup", found.getUserName());
    }
}
