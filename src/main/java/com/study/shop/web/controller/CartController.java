package com.study.shop.web.controller;

import com.study.shop.entity.CartProduct;
import com.study.shop.security.SecurityService;
import com.study.shop.security.entity.Session;
import com.study.shop.service.ProductService;
import com.study.shop.util.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private ProductService productService;
    @Autowired
    private SecurityService securityService;

    @RequestMapping(path="/cart", method = RequestMethod.GET)
    public String getCart(HttpServletRequest req, Model model) {
        String userToken = CookieUtil.getCookieValue("user-session", req);
        Session session = securityService.getSession(userToken);

        List<CartProduct> products = productService.getByCart(session.getCart());
        for (CartProduct cartProduct : products) {
            cartProduct.setCount(session.getCart().getCountById(cartProduct.getProduct().getId()));
        }

        model.addAttribute("products", products);

        return "cart";
    }


    @RequestMapping(path="/cart/add/{id}", method=RequestMethod.GET)
    public String addToCart(@PathVariable int id, HttpServletRequest req) {
        String userToken = CookieUtil.getCookieValue("user-session", req);
        Session session = securityService.getSession(userToken);

        session.getCart().addProduct(id,1);

        return "redirect:/products";
    }

    @RequestMapping(path="/cart/delete/{id}", method = RequestMethod.GET)
    public String deleteFromCart(@PathVariable int id, HttpServletRequest req) {
        String userToken = CookieUtil.getCookieValue("user-session", req);
        Session session = securityService.getSession(userToken);

        session.getCart().deleteProduct(id);

        return "redirect:/cart";
    }

    @RequestMapping(path="/cart/plus/{id}", method = RequestMethod.GET)
    public String plusOneFromCart(@PathVariable int id, HttpServletRequest req) {
        String userToken = CookieUtil.getCookieValue("user-session", req);
        Session session = securityService.getSession(userToken);

        session.getCart().addProduct(id,1);

        return "redirect:/cart";
    }

    @RequestMapping(path="/cart/minus/{id}", method = RequestMethod.GET)
    public String minusOneFromCart(@PathVariable int id, HttpServletRequest req) {
        String userToken = CookieUtil.getCookieValue("user-session", req);
        Session session = securityService.getSession(userToken);

        session.getCart().decreaseCount(id);

        return "redirect:/cart";
    }
}
