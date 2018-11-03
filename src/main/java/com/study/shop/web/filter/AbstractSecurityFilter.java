package com.study.shop.web.filter;

import com.study.shop.entity.User;
import com.study.shop.security.SecurityService;
import com.study.shop.security.entity.Session;
import com.study.shop.util.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configurable
public class AbstractSecurityFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AbstractSecurityFilter.class);

    @Autowired
    private SecurityService securityService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        boolean isAuth = false;

        String userToken = CookieUtil.getCookieValue("user-session", httpServletRequest);
        if (userToken != null) {
            Session session = securityService.getSession(userToken);
            if (session != null) {
                if (hasRole(session.getUser())) {
                    isAuth = true;
                }
            }
        }

        if (isAuth) {
            chain.doFilter(request, response);
        } else {
            httpServletResponse.sendRedirect("/login");
        }

    }

    protected boolean hasRole(User user) {
        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        securityService = WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext()).getBean(SecurityService.class);
    }

    @Override
    public void destroy() {

    }

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

}
