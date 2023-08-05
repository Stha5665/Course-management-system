package com.example.Group_project.COntroller;

import com.example.Group_project.HElper.Messages;
import com.example.Group_project.REpository.UserREpository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

@Controller
public class HomeCOntroller {

// controller will handle request
        @Autowired
        private BCryptPasswordEncoder passwordEncoder; // This for encoding password into hash code
        @Autowired
        private UserREpository userREpository;// user repository
// request /
        @RequestMapping("/")
        public String home(Model model){
            model.addAttribute("title","Home-this is homepage");
            //setting dynamic title of home page

//       //returning home.html
            return "home";

        }
// handle request /about
        @RequestMapping("/about")
        public String about(Model model){// return title
            model.addAttribute("title","Home-this is about page");
            //setting dynamic title

//       //returning about.html
            return "about";

        }


        // Handler for login page
        @RequestMapping("/sign-in")
        public String loginView(Model model){
            model.addAttribute("title", "Login page");// title
            return "login";// return login page when sign-in is requested
        }

    }//


