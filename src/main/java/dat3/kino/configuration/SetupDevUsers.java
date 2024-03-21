package dat3.kino.configuration;

import dat3.security.entities.Role;
import dat3.security.entities.UserWithRoles;
import dat3.security.repositories.RoleRepository;
import dat3.security.repositories.UserWithRolesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class SetupDevUsers implements ApplicationRunner, Ordered {

    UserWithRolesRepository userWithRolesRepository;
    RoleRepository roleRepository;
    PasswordEncoder pwEncoder;

    @Value("${app.admin-username}")
    String adminUsername;

    @Value("${app.admin-email}")
    String adminEmail;

    @Value("${app.admin-password}")
    String adminPassword;

    public SetupDevUsers(UserWithRolesRepository userWithRolesRepository,RoleRepository roleRepository,PasswordEncoder passwordEncoder) {
        this.userWithRolesRepository = userWithRolesRepository;
        this.roleRepository = roleRepository;
        this.pwEncoder = passwordEncoder;
    }

    @Override
    public int getOrder() {
        return 1;
    }

    public void run(ApplicationArguments args) {
        setupAllowedRoles();
        setupUserWithRoleUsers();
    }

    private void setupAllowedRoles(){
        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));
    }

    private void setupUserWithRoleUsers() {
        Role roleAdmin = roleRepository.findById("ADMIN").orElseThrow(()-> new NoSuchElementException("Role 'admin' not found"));
        UserWithRoles admin1 = new UserWithRoles(adminUsername, pwEncoder.encode(adminPassword), adminEmail);
        admin1.addRole(roleAdmin);
        userWithRolesRepository.save(admin1);

    }
}
