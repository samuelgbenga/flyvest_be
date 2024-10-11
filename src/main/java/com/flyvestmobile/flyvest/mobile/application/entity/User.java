package com.flyvestmobile.flyvest.mobile.application.entity;


import com.flyvestmobile.flyvest.mobile.application.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "user_tbl")
public class User extends BaseEntity implements  UserDetails {


    private String fullName;
    private String password;
    private String email;

    private boolean enabled;

    private String profilePicture;

    private String country;

    @Enumerated(EnumType.STRING)
    private Role role; // E.g., ADMIN or USER

    @OneToMany(mappedBy = "user")
    private List<Goal> goals;

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "author")
    private List<Thread> forumThreads;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name())) // Assuming role is an Enum
                .collect(Collectors.toList());
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }
}
