package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping(value = "/admin/")
public class AdminController {
    private final UserServiceImpl userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    public AdminController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public String users(Model model) {
        List<User> users = userService.listUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/new")
    public ModelAndView newUser() {
        User user = new User();
        ModelAndView mav = new ModelAndView("new");
        mav.addObject("user", user);
        List<Role> roles = (List<Role>) roleRepository.findAll();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @PostMapping("/add")
    public String saveUser(@ModelAttribute User user, @RequestParam(name = "roleIds", required = false) List<Long> roleIds) {
        List<Role> roles = (roleIds != null && !roleIds.isEmpty()) ? roleRepository.findAllById(roleIds) : new ArrayList<>();
        user.setRoles(new HashSet<>(roles));
        userService.addSave(user);
        return "redirect:/admin/";
    }


    @PostMapping("/upd")
    public String update(@ModelAttribute User user, @RequestParam(name = "roleIds", required = false) List<Long> roleIds) {
        List<Role> roles = (roleIds != null && !roleIds.isEmpty()) ? roleRepository.findAllById(roleIds) : new ArrayList<>();
        user.setRoles(new HashSet<>(roles));
        userService.addSave(user);
        return "redirect:/admin/";
    }

    @GetMapping("/update")
    public ModelAndView updateForm(Model model, @RequestParam("id") Long id) {
        User user = userService.findById(id);
        ModelAndView mav = new ModelAndView("update");
        mav.addObject("user", user);
        List<Role> roles = (List<Role>) roleRepository.findAll();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin/";
    }
}
