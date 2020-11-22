package com.security.freemarker.controller;

import com.security.freemarker.dto.Users;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello";
    }


    @GetMapping("index")
    @ResponseBody
    public String index() {
        return "hello index";
    }

    @GetMapping("update")
    //@Secured({"ROLE_sale","ROLE_manager"})
    //@PreAuthorize("hasAnyAuthority('admins')")
    @PostAuthorize("hasAnyAuthority('admins')")
    @ResponseBody
    public String update() {
        System.out.println("update......");
        return "hello update";
    }

    @GetMapping("getAll")
    @PostAuthorize("hasAnyAuthority('admins')")
    @PostFilter("filterObject.username == 'admin1'")
    @ResponseBody
    public List<Users> getAllUser() {
        ArrayList<Users> list = new ArrayList<>();
        list.add(new Users(11, "admin1", "6666"));
        list.add(new Users(21, "admin2", "888"));
        System.out.println(list);
        return list;
    }

    @RequestMapping("/unauth")
    public String unauth() {
        return "unauth";
    }

    @RequestMapping("/success")
    public String success() {
        return "success";
    }
}
