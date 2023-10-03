package com.hot6.pnureminder.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "members", schema = "jwtpractice")
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(updatable = false,unique = true,nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    private char gender;

    @OneToMany(mappedBy = "fromMember")
    private List<Like> myLikeList; // 내가 좋아요를 준 사람들 리스트

    @OneToMany(mappedBy = "toMember")
    private List<Like> likeMeList; // 나한테 좋아요 준 사람들 리스트

    @OneToMany(mappedBy = "member1")
    private List<Matching> myMatchingList;

    @Column(nullable = false)
    private boolean enabled;

//현재는 메일 전송 방식으로 임시 비밀번호를 발급해주고 있음
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private VerificationToken verificationToken;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "members_roles",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    //    유저가 가지고 있는 Role 객체를 이용하여 SimpleGrantedAuthority 객체를 생성하고, Stream을 이용하여 Collection 형태로 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
//    Collectors.toList() 메서드를 이용하여 반환되는 Stream 객체를 List 형태로 변환
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.username;
    }
//    GrantedAuthority 객체를 생성할 때 문자열 변환이 필요하지 않기 때문에 유연성이 높아지며, roles 필드를 추가적으로 변경해야 할 경우, 해당 필드만 수정하면 되므로 유지보수가 용이


    // 계정이 만료됐는지 리턴 -> true는 만료X를 의미
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //  계정이 잠겨있는지 리턴 -> true는 잠기지 않음
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호가 만료됐는지 리턴 -> true는 만료X를 의미
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화돼 있는지 리턴 -> true는 활성화O 의미
    @Override
    public boolean isEnabled() {
        return enabled;
    }

}