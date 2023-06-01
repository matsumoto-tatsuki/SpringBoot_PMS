package com.example.springwebtask.controller;

import com.example.springwebtask.entity.Product;
import com.example.springwebtask.entity.User;
import com.example.springwebtask.from.LoginForm;
import com.example.springwebtask.from.ProductInsertForm;
import com.example.springwebtask.service.CategoryService;
import com.example.springwebtask.service.ProductService;
import com.example.springwebtask.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebTaskController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private HttpSession session;


    @GetMapping("/user-login")
    public String loginIndex(Model model,@ModelAttribute("loginForm") LoginForm loginForm) {
        return "/index";
    }

    @PostMapping("/user-login")
    public String loginCheck(Model model, @Validated @ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult) {
        // バリデーション
        if(bindingResult.hasErrors()) {
            return "/index";
        }
        System.out.println("loginCheck:" + loginForm.getLoginId());
        System.out.println("loginCheck:" + loginForm.getPassword());
        var user = userService.loginCheck(loginForm);

        if(user == null){
            model.addAttribute("error","IDまたはパスワードが不正です");
            return "/index";
        }
        System.out.println(user.getLoginId());
        System.out.println(user.getPassword());
        System.out.println(user.getName());
        System.out.println(user.getRole());
        session.setAttribute("user", user);
        session.setAttribute("userName", user.getName());
        System.out.println(session.getAttribute("user"));
        System.out.println(session.getAttribute("userName"));
        return "redirect:/menuIndex";
    }

    @GetMapping("/user-logout")
    public String userLogout(){
        session.removeAttribute("user");
        session.removeAttribute("userName");
        return "/logout";
    }

    @GetMapping("/menuIndex")
    public String menuIndex(Model model) {
        if(session.getAttribute("user") == null){
            System.out.println("session.nullです");
            return "redirect:/user-login";
        }
        User user = (User)session.getAttribute("user");
        model.addAttribute("userRole",user.getRole());
        var list = productService.findAll();
        list.stream().forEach(System.out::println);
        model.addAttribute("productFindAll",list);
        model.addAttribute("productFindAllNum",list.size());
        return "/menu";
    }

    @GetMapping("/menuSort")
    public String menuSort(Model model,@RequestParam(name="order") String order, @RequestParam(name="keyword") String keyword) {
        if(session.getAttribute("user") == null){
            System.out.println("session.nullです");
            return "redirect:/user-login";
        }
        User user = (User)session.getAttribute("user");
        model.addAttribute("userRole",user.getRole());
        System.out.println(order);
        System.out.println(keyword);

        String[] parts = order.split("：");
        String where = null;
        String jun = null;
        String key = keyword.isEmpty() ? null : keyword;


        System.out.println(key);
        if(!order.equals("並び替え")) {
            if (parts[0].equals("商品ID")) {
                where = "productId";
            } else if (parts[0].equals("カテゴリ")) {
                where = "c.id";
            } else if (parts[0].equals("単価")) {
                where = "productPrice";
            }

            if (parts[1].equals("降順") || parts[1].equals("高い順")) {
                jun = "DESC";
            } else if (parts[1].equals("昇順") || parts[1].equals("安い順")) {
                jun = "ASC";
            }
        }
        
        System.out.println(where);
        System.out.println(jun);

        List list = new ArrayList<Product>();
        if(where != null && jun != null && key != null){
            list = productService.findOrderNameSort(where,jun,key);
        }else if(where != null && jun != null){
            list = productService.findOrderSort(where,jun);
        }else if(key != null){
            list = productService.findNameSort(key);
        }
        list = list.isEmpty() ? productService.findAll() : list;
        model.addAttribute("productFindAll",list);
        model.addAttribute("productFindAllNum",list.size());

        return "/menu";
    }



    @GetMapping("/productInsert")
    public String insert(Model model,@ModelAttribute("productInsertForm") ProductInsertForm productInsertForm) {
        if(session.getAttribute("user") == null){
            System.out.println("session.nullです");
            return "redirect:/user-login";
        }

        var list = categoryService.findAll();
        model.addAttribute("categories",list);
        return "/insert";
    }

    @PostMapping("/productInsert")
    public String insert(Model model, @Validated @ModelAttribute("productInsertForm") ProductInsertForm productInsertForm, BindingResult bindingResult) {
        // バリデーション
        if(bindingResult.hasErrors()) {
            var list = categoryService.findAll();
            model.addAttribute("categories",list);
            return "/insert";
        }


        System.out.println("商品Id:" + productInsertForm.getProductId());
        System.out.println("商品名:" + productInsertForm.getProductName());
        System.out.println("商品値段:" + productInsertForm.getProductPrice());
        System.out.println("カテゴリ名:" + productInsertForm.getCategoryName());
        System.out.println("説明:" + productInsertForm.getDescription());

        var insertNum = productService.insert(new Product(productInsertForm.getProductId(),
                                                          productInsertForm.getProductName(),
                                                          Integer.parseInt(productInsertForm.getProductPrice()),
                                                          productInsertForm.getCategoryName(),
                                                          productInsertForm.getDescription()));
        if(insertNum == 0){
            var list = categoryService.findAll();
            model.addAttribute("categories",list);
            model.addAttribute("error","商品IDが重複しています");
            return "/insert";
        }

        return "/successInsert";
    }

    @GetMapping("/productDetail/{id}")
    public String productDetail(Model model, @PathVariable("id") String id){
        if(session.getAttribute("user") == null){
            System.out.println("session.nullです");
            return "redirect:/user-login";
        }
        User user = (User)session.getAttribute("user");
        model.addAttribute("userRole",user.getRole());
        System.out.println("GetID:"+id);
        var product = productService.findById(id);
        model.addAttribute("product",product);
        model.addAttribute("productId",product.getProductId());
        return "/detail";
    }

    @GetMapping("/productUpdateInput/{id}")
    public String productUpdate(Model model, @PathVariable("id") String id){
        if(session.getAttribute("user") == null){
            System.out.println("session.nullです");
            return "redirect:/user-login";
        }
        var product = productService.findById(id);
        model.addAttribute("product",product);

        var list = categoryService.findAll();
        model.addAttribute("categories",list);

        return "/updateInput";
    }

    @PostMapping("/productUpdate/{id}")
    public String productUpdateSet(Model model,@Validated @ModelAttribute("product") ProductInsertForm productInsertForm, BindingResult bindingResult, @PathVariable("id") String id){
        if(session.getAttribute("user") == null){
            System.out.println("session.nullです");
            return "redirect:/user-login";
        }
        if(bindingResult.hasErrors()) {
            var product = productService.findById(id);
            model.addAttribute("product",productInsertForm);

            var list = categoryService.findAll();
            model.addAttribute("categories",list);

            return "/updateInput";
        }

        System.out.println("商品Id:" + productInsertForm.getProductId());
        System.out.println("商品名:" + productInsertForm.getProductName());
        System.out.println("商品値段:" + productInsertForm.getProductPrice());
        System.out.println("カテゴリ名:" + productInsertForm.getCategoryName());
        System.out.println("説明:" + productInsertForm.getDescription());
        System.out.println(id);
        var products = productService.findAll();
        boolean exists = false;
        for(var product : products){
            if(product.getProductId().equals(productInsertForm.getProductId()) && !product.getProductId().equals(id)){
                exists = true;
                break;
            }
        }

        if(!exists){
            var updateNum = productService.update(new Product(productInsertForm.getProductId(),
                    productInsertForm.getProductName(),
                    Integer.parseInt(productInsertForm.getProductPrice()),
                    productInsertForm.getCategoryName(),
                    productInsertForm.getDescription()),id);
            if(updateNum == 0){
                var product = productService.findById(id);
                model.addAttribute("product",product);

                var list = categoryService.findAll();
                model.addAttribute("categories",list);

                model.addAttribute("error","更新時にエラーが発生しました");
                System.out.println("更新時にエラーが発生しました");
                return "/updateInput";
            }
            return "/successUpdate";
        }else{
            var product = productService.findById(id);
            model.addAttribute("product",product);

            var list = categoryService.findAll();
            model.addAttribute("categories",list);

            model.addAttribute("error","商品IDが重複しています");
            System.out.println("商品IDが重複しています");
            return "/updateInput";
        }
    }

    @PostMapping("/productDelete")
    public String productDelete(Model model,@Validated @ModelAttribute("product") ProductInsertForm productInsertForm, BindingResult bindingResult){
        if(session.getAttribute("user") == null){
            System.out.println("session.nullです");
            return "redirect:/user-login";
        }
        System.out.println("商品Id:" + productInsertForm.getProductId());

        var deleteNum = productService.delete(productInsertForm.getProductId());

        if(deleteNum == 0){
            var product = productService.findById(productInsertForm.getProductId());
            model.addAttribute("product",product);
            model.addAttribute("productId",product.getProductId());
            model.addAttribute("error","削除に失敗しました");
            User user = (User)session.getAttribute("user");
            model.addAttribute("userRole",user.getRole());
            return "/detail";
        }

        return "successDelete";
    }
}

