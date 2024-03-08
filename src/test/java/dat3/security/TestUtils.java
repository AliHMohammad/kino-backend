package dat3.security;

import dat3.security.entities.Role;
import dat3.security.entities.UserWithRoles;
import dat3.security.repositories.RoleRepository;
import dat3.security.repositories.UserWithRolesRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestUtils {


  public static void setupTestUsers(UserWithRolesRepository userWithRolesRepository, RoleRepository roleRes, PasswordEncoder pwEn){
    userWithRolesRepository.deleteAll();
    String pw = "secret";
    Role userRole = new Role("USER");
    Role adminRole = new Role("ADMIN");
    roleRes.save(userRole);
    roleRes.save(adminRole);
    UserWithRoles user1 = new UserWithRoles("u1", pwEn.encode(pw), "u1@a.dk");
    UserWithRoles user2 = new UserWithRoles("u2", pwEn.encode(pw), "u2@a.dk");
    UserWithRoles user3 = new UserWithRoles("u3", pwEn.encode(pw), "u3@a.dk");
    UserWithRoles userNoRoles = new UserWithRoles("u4", pwEn.encode(pw), "u4@a.dk");
    user1.addRole(userRole);
    user1.addRole(adminRole);
    user2.addRole(userRole);
    user3.addRole(adminRole);
    userWithRolesRepository.save(user1);
    userWithRolesRepository.save(user2);
    userWithRolesRepository.save(user3);
    userWithRolesRepository.save(userNoRoles);
  }
}
