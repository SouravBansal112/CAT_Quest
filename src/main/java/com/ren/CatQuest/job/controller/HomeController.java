package com.ren.CatQuest.job.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() { return "index"; }

//    @GetMapping("/jobs")
//    public String jobs(){
//        return "index";
//    }

//    @GetMapping("/profile")
//    public String profile(){
//        return "profile";
//    }
//
//    @GetMapping("/tracking")
//    public String tracking(){
//        return "tracking";
//    }
//
//    @GetMapping("/calendar")
//    public String calendar(){
//        return "calendar";
//    }

}
