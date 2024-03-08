package dat3.security.services;

import dat3.security.TestUtils;
import dat3.security.dto.UserWithRolesRequest;
import dat3.security.dto.UserWithRolesResponse;
import dat3.security.entities.UserWithRoles;
import dat3.security.repositories.RoleRepository;
import dat3.security.repositories.UserWithRolesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

//You can enable/disable these tests in you maven builds via the maven-surefire-plugin, in your pom-file
@Tag("DisabledSecurityTest")
@DataJpaTest
class UserWithRolesServiceTest {

  //@Autowired
  UserWithRolesService userWithRolesService;

  @Autowired
  UserWithRolesRepository userWithRolesRepository;

  @Autowired
  RoleRepository roleRepository;

  //@Autowired
  @MockBean
  PasswordEncoder passwordEncoder;

  private boolean dataInitialized = false;

  @BeforeEach
  void setUp() {
    Mockito.when(passwordEncoder.encode("secret")).thenReturn("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

    //userWithRolesService.setDefaultRoleName("USER"); //Could also be done in the TEST application.properties
    if(!dataInitialized) {

      userWithRolesService = new UserWithRolesService(userWithRolesRepository,roleRepository,passwordEncoder);
      userWithRolesRepository.deleteAll();
      TestUtils.setupTestUsers(userWithRolesRepository,roleRepository,passwordEncoder);
      dataInitialized = true;
    }
  }


  @Test
  void getUserWithRoles() {
    UserWithRolesResponse user = userWithRolesService.getUserWithRoles("u1");
    assertEquals(2, user.getRoleNames().size());
    assertTrue(user.getRoleNames().contains("USER"));
    assertTrue(user.getRoleNames().contains("ADMIN"));
  }

  @Test
  void addRole() {
    UserWithRolesResponse user = userWithRolesService.addRole("u4", "USER");
    assertEquals(1, user.getRoleNames().size());
    assertTrue(user.getRoleNames().contains("USER"));
  }

  @Test
  void removeRole() {
    UserWithRolesResponse user = userWithRolesService.removeRole("u1", "USER");
    assertEquals(1, user.getRoleNames().size());
    assertTrue(user.getRoleNames().contains("ADMIN"));
    user = userWithRolesService.removeRole("u1", "ADMIN");
    assertEquals(0, user.getRoleNames().size());
  }

  @Test
  void editUserWithRoles() {
    Mockito.when(passwordEncoder.encode("new_Password")).thenReturn("aaaxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
    String originalPassword = userWithRolesRepository.findById("u1").get().getPassword();
    UserWithRolesRequest user1 = new UserWithRolesRequest("u1New", "new_Password", "newMail@a.dk");
    UserWithRolesResponse user = userWithRolesService.editUserWithRoles("u1",user1);
    assertEquals("u1", user.getUserName());  //IMPORTANT: The username should not be changed
    assertEquals("newMail@a.dk", user.getEmail());
    UserWithRoles editedUser = userWithRolesRepository.findById("u1").get();
    assertNotEquals(originalPassword, editedUser.getPassword());
  }

  @Test
  void addUserWithRolesWithNoRole() {
    Mockito.when(passwordEncoder.encode("new_Password")).thenReturn("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
    userWithRolesService.setDefaultRoleName(null);
    UserWithRolesRequest user = new UserWithRolesRequest("u5", "new_Password", "xx@x.dk");
    UserWithRolesResponse newUser = userWithRolesService.addUserWithRoles(user);
    assertEquals(0, newUser.getRoleNames().size());
    assertEquals("u5", newUser.getUserName());
    assertEquals("xx@x.dk", newUser.getEmail());
    //Verify that the password is hashed
    UserWithRoles userFromDB = userWithRolesRepository.findById("u5").get();
    assertEquals(60,userWithRolesRepository.findById("u5").get().getPassword().length());
  }
  @Test
  void addUserWithRolesWithRole() {
    Mockito.when(passwordEncoder.encode("new_Password")).thenReturn("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
    userWithRolesService.setDefaultRoleName("USER");
    UserWithRolesRequest user = new UserWithRolesRequest("u5", "new_Password", "xx@x.dk");
    UserWithRolesResponse newUser = userWithRolesService.addUserWithRoles(user);
    assertEquals(1, newUser.getRoleNames().size());
    assertTrue(newUser.getRoleNames().contains("USER"));
    assertEquals("u5", newUser.getUserName());
    assertEquals("xx@x.dk", newUser.getEmail());
  }

}