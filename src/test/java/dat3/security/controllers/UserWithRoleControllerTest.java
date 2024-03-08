package dat3.security.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat3.security.TestUtils;
import dat3.security.dto.LoginRequest;
import dat3.security.dto.LoginResponse;
import dat3.security.dto.UserWithRolesRequest;
import dat3.security.repositories.RoleRepository;
import dat3.security.repositories.UserWithRolesRepository;
import dat3.security.services.UserWithRolesService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//You can enable/disable these tests in you maven builds via the maven-surefire-plugin, in your pom-file
@Tag("DisabledSecurityTest")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@Import(PasswordEncoderConfig.class)
@Transactional
class UserWithRoleControllerTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  UserWithRolesRepository userWithRolesRepository;

  @Autowired
  RoleRepository roleRepository;
  @Autowired
  UserWithRolesService userWithRolesService;
  @Autowired
  PasswordEncoder passwordEncoder;

  String adminToken;
  String userToken;
  private final ObjectMapper objectMapper = new ObjectMapper();

  private boolean dataInitialized = false;

  @BeforeEach
  void setUp() throws Exception {
//    userWithRolesService = new UserWithRolesService(userWithRolesRepository, roleRepository);
    if (!dataInitialized) {
      //userWithRolesService = new UserWithRolesService(userWithRolesRepository, roleRepository);
      userWithRolesRepository.deleteAll();
      TestUtils.setupTestUsers(userWithRolesRepository,roleRepository,passwordEncoder);
      userToken = loginAndGetToken("u2", "secret");
      adminToken = loginAndGetToken("u3", "secret");
      dataInitialized = true;
    }
    userWithRolesService.setDefaultRoleName("USER"); //can also be done in the TEST application.properties
  }

  @AfterEach
  void tearDown() {
    userWithRolesService.setDefaultRoleName("USER");
  }

  String loginAndGetToken(String user, String pw) throws Exception {
    LoginRequest loginRequest = new LoginRequest(user, pw);
    MvcResult response = mockMvc.perform(post("/api/auth/login")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn();
    LoginResponse loginResponse = objectMapper.readValue(response.getResponse().getContentAsString(), LoginResponse.class);
    return loginResponse.getToken();
  }

  @Test
  void addUsersWithRolesNoRoles() throws Exception {
    UserWithRolesRequest newUserReq = new UserWithRolesRequest("u100", "secret", "u100@a.dk");
    userWithRolesService.setDefaultRoleName(null);
    mockMvc.perform(post("/api/user-with-role")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(newUserReq)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName").value("u100"))
            .andExpect(jsonPath("$.email").value("u100@a.dk"))
            .andExpect(jsonPath("$.roleNames").isEmpty());
  }

  @Test
  void addUsersWithRoles() throws Exception {
    UserWithRolesRequest newUserReq = new UserWithRolesRequest("u100", "secret", "u100@a.dk");
    userWithRolesService.setDefaultRoleName("USER");
    mockMvc.perform(post("/api/user-with-role")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(newUserReq)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName").value("u100"))
            .andExpect(jsonPath("$.email").value("u100@a.dk"))
            .andExpect(jsonPath("$.roleNames", hasSize(1)))
            .andExpect(jsonPath("$.roleNames", contains("USER")));
  }

  @Test
  void addRoleFAilsWhenNotAuthenticatedWithRole() throws Exception {
    mockMvc.perform(patch("/api/user-with-role/add-role/u4/admin")
                    .accept("application/json"))
            .andExpect(status().isUnauthorized());
  }

  @Test
  void addRole() throws Exception {
    mockMvc.perform(patch("/api/user-with-role/add-role/u4/ADMIN")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                    .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName").value("u4"))
            .andExpect(jsonPath("$.roleNames", hasSize(1)))
            .andExpect(jsonPath("$.roleNames", contains("ADMIN")));
  }

  @Test
  void addRoleFailsWithWrongRole() throws Exception {
    mockMvc.perform(patch("/api/user-with-role/add-role/u2/ADMIN")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                    .accept("application/json"))
            .andExpect(status().isForbidden());
  }

  @Test
  void removeRoleFailsWhenNotAuthenticatedWithRole() throws Exception {
    mockMvc.perform(patch("/api/user-with-role/remove-role/u2/USER")
                    .accept("application/json"))
            .andExpect(status().isUnauthorized());
  }

  @Test
  void removeRole() throws Exception {
    mockMvc.perform(patch("/api/user-with-role/remove-role/u1/USER")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                    .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName").value("u1"))
            .andExpect(jsonPath("$.roleNames", hasSize(1)))
            .andExpect(jsonPath("$.roleNames", contains("ADMIN")));
  }
}