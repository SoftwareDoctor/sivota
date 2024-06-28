package it.softwaredoctor.sivota.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Builder
@ToString
@Table(name = "user")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid_user", nullable = false, unique = true)
    private UUID uuidUser;

    @NotNull
    @Column(name = "username")
    private String username;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "surname")
    private String surname;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Votazione> votazione = new ArrayList<>();

    @Column(name = "registrato")
    private boolean registrato;

    @NotEmpty
    @Column(name = "role_name")
    private String roleName;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "token_creation_date")
    private LocalDate tokenCreationDate;

    @Column(name = "accesso")
    private boolean accesso;

    @PrePersist
    private void onCreateAbstractBaseEntity() {
        this.uuidUser = UUID.randomUUID();
        this.enabled = false;
        this.tokenCreationDate = LocalDate.now();
        this.accesso = false;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singleton(new SimpleGrantedAuthority("ROLE_admin"));
//    }

//    @Override
//    @JsonIgnore
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Set<GrantedAuthority> authorities = new HashSet<>();
//
//        if (this.roleName.equals("admin")) {
//            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        } else {
//            throw new IllegalArgumentException("Ruolo sconosciuto: " + this.roleName);
//        }
//        return authorities;
//    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        if (this.roleName.equals("admin")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_admin"));
        } else {
            throw new IllegalArgumentException("Ruolo sconosciuto: " + this.roleName);
        }
        return authorities;
//        return null;
    }
//
//    @Override
//    public String getPassword() {
//        return "";
//    }
//
//    @Override
//    public String getUsername() {
//        return "";
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}