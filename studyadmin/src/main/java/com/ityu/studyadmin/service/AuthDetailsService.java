package com.ityu.studyadmin.service;

import com.ityu.studyadmin.domain.AdminUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ityu.studyadmin.utils.Contans.ROLE_ADMIN;

@Service
//@Transactional
public class AuthDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority(ROLE_ADMIN));
        AdminUser user = AdminUser.getInstance();
        return new User(user.getUsername(), user.getPassword(), simpleGrantedAuthorities);
    }
}
