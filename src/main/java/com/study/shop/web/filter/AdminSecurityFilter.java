package com.study.shop.web.filter;

import com.study.shop.entity.User;
import com.study.shop.security.entity.UserRole;

public class AdminSecurityFilter extends AbstractSecurityFilter {

    @Override
    protected boolean hasRole(User user) {
        return user.getUserRole() == UserRole.ADMIN;
    }

}
