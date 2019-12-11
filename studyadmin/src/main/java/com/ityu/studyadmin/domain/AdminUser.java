package com.ityu.studyadmin.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

import static com.ityu.studyadmin.utils.Contans.ROLE_ADMIN;

@Data
@Accessors(chain = true)
public class AdminUser {
    private String username;
    private String password;
    private String token;
    private List<String> roles;


    public  static AdminUser getInstance(){
        List<String> roles = new ArrayList<>();
        roles.add(ROLE_ADMIN);
        return new AdminUser().setUsername(ROLE_ADMIN).setPassword("$2a$10$vsN8gbDG/EzYUxrm40nZqu1ldtBwnV6bfs9cPomH7n85dqsdWUHEy").setRoles(roles);
    }


}
