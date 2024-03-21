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

/**
 * Configures initial development users and their roles upon application startup.
 * This setup is crucial for ensuring that the application has a basic set of users
 * with predefined roles to allow for immediate usage and testing.
 */
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

    /**
     * Constructs a new SetupDevUsers instance with necessary dependencies.
     *
     * @param userWithRolesRepository The repository for user entities.
     * @param roleRepository The repository for role entities.
     * @param passwordEncoder The encoder for hashing passwords.
     */
    public SetupDevUsers(UserWithRolesRepository userWithRolesRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userWithRolesRepository = userWithRolesRepository;
        this.roleRepository = roleRepository;
        this.pwEncoder = passwordEncoder;
    }

    /**
     * Specifies the order in which this application runner should be executed.
     *
     * @return the order value, lower values have higher priority.
     */
    @Override
    public int getOrder() {
        return 1; // Order in which this runner should be executed
    }

    /**
     * Sets up the initial roles and users when the application starts.
     *
     * @param args the application arguments.
     */
    @Override
    public void run(ApplicationArguments args) {
        setupAllowedRoles();
        setupUserWithRoleUsers();
    }

    /**
     * Sets up allowed roles in the application.
     * Currently defines "USER" and "ADMIN" roles.
     */
    private void setupAllowedRoles() {
        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));
    }

    /**
     * Sets up initial users with roles, specifically an admin user.
     * Reads admin user details from application properties.
     */
    private void setupUserWithRoleUsers() {
        Role roleAdmin = roleRepository.findById("ADMIN").orElseThrow(() -> new NoSuchElementException("Role 'ADMIN' not found"));
        UserWithRoles admin1 = new UserWithRoles(adminUsername, pwEncoder.encode(adminPassword), adminEmail);
        admin1.addRole(roleAdmin);
        userWithRolesRepository.save(admin1);
    }
}
