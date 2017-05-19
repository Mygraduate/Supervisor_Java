package com.graduate.config.secruity;


import com.graduate.system.user.model.Role;
import com.graduate.system.user.model.User;
import com.graduate.system.user.model.UserAndRole;
import com.graduate.system.user.service.RoleService;
import com.graduate.system.user.service.UserAndRoleService;
import com.graduate.system.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService<User> userService;

    @Autowired
    private UserAndRoleService<UserAndRole> userAndRoleService;

    @Autowired
    private RoleService<Role> roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userService.findUserByname(username);
        if (user == null) {
            throw  new UsernameNotFoundException("用户名没找到");
        }
        UserAndRole uAr = userAndRoleService.findRoleByUid(user.getId());

        if(uAr  == null){
            throw  new UsernameNotFoundException("用户角色未设置");
        }

        Role role = null;
        try {
//            role = roleService.findOne(uAr.getRoleId());
            role = roleService.findOne(uAr.getRole().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (role == null) {
            throw  new UsernameNotFoundException("没有找到角色");
        }
        boolean status = (role.getStatus() == 0) ? true : false;
        if(!status){
            throw  new UsernameNotFoundException("当前用户不可用");
        }
        List<String> authorities = new ArrayList<>();
        authorities.add(role.getCode());
        return JwtUserFactory.create(user,authorities);

    }

}
