package dcom.focuz.api.domain.user;

import dcom.focuz.api.domain.group.UserGroup;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(length = 64, nullable = false)
    private String name;

    @Column(length = 64, nullable = false)
    private String nickname;

    @Column
    private String motto;

    @Column(name = "profile_image")
    private String profileImage;

    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column
    private Role role;

    @OneToMany(mappedBy = "user")
    private Set<UserGroup> groups = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(this.role.getKey()));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return String.valueOf(this.id);
    }

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
        return this.role == Role.USER;
    }
}
