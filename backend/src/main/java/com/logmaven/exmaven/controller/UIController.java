package com.logmaven.exmaven.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class UIController {


    @GetMapping(value = "/access-denied")
    public  String accessdenied(){
        return "Accessdenied 404";
    }


}
