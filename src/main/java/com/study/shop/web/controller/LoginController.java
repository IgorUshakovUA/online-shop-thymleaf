package com.study.shop.web.controller;

import com.study.shop.security.SecurityService;
import com.study.shop.security.entity.Session;
import com.study.shop.util.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private SecurityService securityService;

    @RequestMapping(path="/login", method= RequestMethod.GET)
    public String getLoginPage() {
        return "login";
    }

    @RequestMapping(path="/login",method = RequestMethod.POST)
    public String login(@RequestParam String name, @RequestParam String password, HttpServletResponse resp) {
        Session session = securityService.auth(name, password);

        if (session != null) {
            CookieUtil.setCookie("user-session", session.getToken(),resp);
            return "redirect:/";
        } else {
            return "redirect:/login";
        }
    }
}

