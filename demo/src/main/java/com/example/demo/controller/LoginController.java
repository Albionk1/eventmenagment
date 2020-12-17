package com.example.demo.controller;

import com.example.demo.model.Event;
import com.example.demo.model.User;
import com.example.demo.service.EventServices;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private EventServices eventServices;

    @GetMapping(value = { "/", "/login" })
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping(value = "/registration")
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping(value = "/registration")
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByUserName(user.getUserName());
        if (userExists != null) {
            bindingResult.rejectValue("userName", "error.user",
                    "There is already a user registered with the user name provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @GetMapping(value = "/admin/home")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " "
                + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    @GetMapping(value = "/admin/home/event")
    public String homeEvent(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // User user = userService.findUserByUserName(auth.getName());
        List<Event> events = eventServices.getAllevents();
        model.addAttribute("events", events);
        modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/event");
        return "event";
//        System.out.println("ttest");
//         return "Test";



    }

    @GetMapping(value = "/admin/home/event/new")
    public String GetNewEvent(Model model) {
        Event event = new Event();
        model.addAttribute("event", event);
        return "new_event";
    }

    @PostMapping(value = "/admin/home/event/save")
    public String PostSaveEvent(Model model, Event event) {
        eventServices.save(event);

        return "admin/msg";
    }

    //
    //
    // @GetMapping(value="/admin/home/event")
    // public ModelAndView homeEvent(Model model){
    // ModelAndView modelAndView = new ModelAndView();
    // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    // List<Event> events = eventServices.getAllEvents();
    //
    // model.addAttribute("events",events);
    //
    //
    // modelAndView.setViewName("admin/event");
    // return modelAndView;
    // }

}
