package com.study.shop.web.filter;

import com.study.shop.entity.User;
import com.study.shop.security.entity.UserRole;

public class UserSecurityFilter extends AbstractSecurityFilter {

    @Override
    protected boolean hasRole(User user) {
        UserRole userRole = user.getUserRole();
        return userRole == UserRole.ADMIN || userRole == UserRole.USER;
    }
}
