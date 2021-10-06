package com.bambi.straw.faq.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/test")
    public String getTest(
            @AuthenticationPrincipal UserDetails userDetails
            ){
        System.out.println("userDetails = " + userDetails);
        return userDetails.getUsername();
    }
}
