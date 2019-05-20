package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
public class HelloController {
   @Autowired
   @PreAuthorize("hasRole('USER')")
   @RequestMapping("/")
   public String helloWorld() {
      return "Hello World!";
   }

   @PreAuthorize("hasRole('Group1')")
   @RequestMapping("/Group1")
   public String groupOne() {
      return "Hello Group 1 Users!";
   }
   @PreAuthorize("hasRole('Group2')")
   @RequestMapping("/Group2")
   public String groupTwo() {
      return "Hello Group 2 Users!";
   }

   @RequestMapping("/All")
   public String groupAnon() {
      return "Hello Group all!";
   }
}