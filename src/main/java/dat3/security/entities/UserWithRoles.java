package dat3.security.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Configurable
@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DISCRIMINATOR_TYPE")
public class UserWithRoles implements UserDetails {

  @Transient
  static final int PASSWORD_MIN_LENGTH = 60;  // BCrypt encoded passwords always have length 60

  @Id
  @Column(nullable = false,length = 50,unique = true)
  String username;

  @Column(nullable = false,length = 50,unique = true)
  String email;

  //60 = length of a bcrypt encoded password
  @Column(nullable = false, length = 60)
  String password;

  private boolean enabled= true;

  @CreationTimestamp
  private LocalDateTime created;

  @UpdateTimestamp
  private LocalDateTime edited;


  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles",
          joinColumns = {@JoinColumn(name = "user_username", referencedColumnName = "username")},
          inverseJoinColumns = {@JoinColumn(name = "role_roleName", referencedColumnName = "roleName")})
  Set<Role> roles = new HashSet<>();

  public UserWithRoles() {}

  public UserWithRoles(String user, String password, String email){
    this.username = user;
    setPassword(password);
    this.email = email;
  }

  public void setPassword(String pw){
    if(pw.length()<60){
      throw new IllegalArgumentException("Password is not encoded");
    }
    this.password = pw;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).toList();
  }

  public void addRole(Role roleToAdd) {
    if (!roles.contains(roleToAdd)) {
      roles.add(roleToAdd);
      roleToAdd.addUser(this);
    }
  }

  public void removeRole(Role roleToRemove) {
    if (roles.contains(roleToRemove)) {
      roles.remove(roleToRemove);
    }
  }

  //You can, but are NOT expected to use the fields below
  @Override
  public boolean isAccountNonExpired() {return enabled;}

  @Override
  public boolean isAccountNonLocked() { return enabled;}

  @Override
  public boolean isCredentialsNonExpired() { return enabled; }
}