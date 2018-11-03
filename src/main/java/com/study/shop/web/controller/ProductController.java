package com.study.shop.web.controller;

import com.study.shop.entity.Product;
import com.study.shop.security.SecurityService;
import com.study.shop.security.entity.Session;
import com.study.shop.service.ProductService;
import com.study.shop.util.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;
    @Autowired
    private SecurityService securityService;

    @RequestMapping(path = {"/", "/products"}, method = RequestMethod.GET)
    public String getAll(HttpServletRequest req, HttpServletResponse resp, Model model) {
        String userToken = CookieUtil.getCookieValue("user-session", req);
        Session session = securityService.getSession(userToken);
        List<Product> products = productService.getAll();

        if (session != null) {
            model.addAttribute("count", session.getCart().getItemCount());
        } else {
            model.addAttribute("count",0);
        }
        model.addAttribute("products",products);

        return "products";
    }

    @RequestMapping(path = "/product/add", method = RequestMethod.GET)
    public String addProductPage() {
        return "addProduct";
    }

    @RequestMapping(path = "/product/add", method = RequestMethod.POST)
    public String addProduct(@RequestParam String name, @RequestParam double price, @RequestParam String picturePath) {
        int id = productService.add(name, price, picturePath);

        return "redirect:/products";
    }

    @RequestMapping(path="/product/edit/{id}", method = RequestMethod.GET)
    public String editProductPage(@PathVariable int id, Model model) {
        try {
            List<Product> products = productService.getById(id);

            Product product = products.get(0);
            model.addAttribute("id", product.getId());
            model.addAttribute("name", product.getName());
            model.addAttribute("price", String.format("%.2f",product.getPrice()).replace(",","."));
            model.addAttribute("picturePath", product.getPicturePath());

            return "editProduct";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/products";
    }

    @RequestMapping(path = "/product/edit", method = RequestMethod.POST)
    public String editProduct(@RequestParam int id, @RequestParam String name, @RequestParam double price, @RequestParam String picturePath) {
        LocalDateTime addDate = LocalDateTime.now();

        productService.update(id, name, price, addDate, picturePath);

        return "redirect:/products";
    }

    @RequestMapping(path="/product/delete/{id}", method=RequestMethod.GET)
    public String deleteProduct(@PathVariable int id) {
        productService.delete(id);

        return "redirect:/products";
    }
}
